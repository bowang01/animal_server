#!/usr/bin/env bash
set -euo pipefail

# One-time Hostinger VPS bootstrap for the animal project.
# Existing nginx keeps 80/443. This script only adds a local :18080 vhost.

if [[ "${EUID}" -ne 0 ]]; then
  echo "Please run as root: sudo bash deploy/setup-vps.sh"
  exit 1
fi

APP_ROOT=/opt/animal
DEPLOY_DIR="$(cd "$(dirname "$0")" && pwd)"
MYSQL_PASSWORD="${MYSQL_PASSWORD:-$(openssl rand -hex 12)}"
MYSQL_ROOT_PASSWORD="${MYSQL_ROOT_PASSWORD:-$(openssl rand -hex 16)}"

export DEBIAN_FRONTEND=noninteractive
apt-get update -y
apt-get install -y openjdk-17-jre-headless rsync curl ca-certificates
apt-get install -y nginx || true

if ! command -v docker >/dev/null 2>&1; then
  curl -fsSL https://get.docker.com | sh
fi

id -u animal >/dev/null 2>&1 || useradd --system --home "${APP_ROOT}/server" --shell /usr/sbin/nologin animal

mkdir -p "${APP_ROOT}/front" "${APP_ROOT}/server/uploads" "${APP_ROOT}/mysql"
chown -R animal:animal "${APP_ROOT}/server"
chown -R www-data:www-data "${APP_ROOT}/front" || chown -R animal:animal "${APP_ROOT}/front"

if [[ ! -f "${APP_ROOT}/server/.env" ]]; then
  sed \
    -e "s/SPRING_DATASOURCE_PASSWORD=change_me/SPRING_DATASOURCE_PASSWORD=${MYSQL_PASSWORD}/" \
    "${DEPLOY_DIR}/.env.example" > "${APP_ROOT}/server/.env"
  chmod 600 "${APP_ROOT}/server/.env"
  chown animal:animal "${APP_ROOT}/server/.env"
fi

if [[ ! -f "${APP_ROOT}/mysql/.env" ]]; then
  cat > "${APP_ROOT}/mysql/.env" <<EOF
MYSQL_PASSWORD=${MYSQL_PASSWORD}
MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
EOF
  chmod 600 "${APP_ROOT}/mysql/.env"
fi
cp "${DEPLOY_DIR}/docker-compose.mysql.yml" "${APP_ROOT}/mysql/docker-compose.yml"

cd "${APP_ROOT}/mysql"
if ss -lnt 2>/dev/null | grep -q ':3306 ' || netstat -lnt 2>/dev/null | grep -q ':3306 '; then
  echo "Port 3306 is already in use. Skipping Docker MySQL."
  echo "Edit ${APP_ROOT}/server/.env if you want the app to use the existing database."
else
  docker compose up -d || docker-compose up -d
fi

cp "${DEPLOY_DIR}/animal-server.service" /etc/systemd/system/animal-server.service
systemctl daemon-reload
systemctl enable animal-server

if [[ -d /etc/nginx/sites-available ]]; then
  cp "${DEPLOY_DIR}/nginx-animal.conf" /etc/nginx/sites-available/animal.conf
  ln -sfn /etc/nginx/sites-available/animal.conf /etc/nginx/sites-enabled/animal.conf
  nginx -t
  systemctl enable nginx
  systemctl reload nginx || systemctl restart nginx
elif [[ -d /etc/nginx/conf.d ]]; then
  cp "${DEPLOY_DIR}/nginx-animal.conf" /etc/nginx/conf.d/animal.conf
  nginx -t
  systemctl enable nginx
  systemctl reload nginx || systemctl restart nginx
else
  echo "Nginx conf dir not found. Copy deploy/nginx-animal.conf into your existing nginx and reload."
fi

echo
echo "VPS bootstrap finished."
echo "MySQL user: animal"
echo "MySQL password: ${MYSQL_PASSWORD}"
echo "MySQL root password: ${MYSQL_ROOT_PASSWORD}"
echo "Passwords are also in ${APP_ROOT}/server/.env and ${APP_ROOT}/mysql/.env"
echo
echo "In your existing nginx domain mapping, add:"
echo "  animal.bowang.tech  ->  127.0.0.1:18080"
echo "A file-based example is in deploy/nginx-host-mapping.conf"
echo
echo "Then add GitHub Actions secrets on both repos:"
echo "  VPS_HOST, VPS_USER, VPS_SSH_KEY"
echo "Push master (or run the workflow manually) to publish frontend and backend."

package com.example.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.controller.dto.UserDTO;
import com.example.controller.dto.UserPasswordDTO;
import com.example.exception.ServiceException;
import com.example.common.Constants;
import com.example.common.RoleEnum;
import com.example.entity.Menu;
import com.example.entity.User;
import com.example.mapper.RoleMapper;
import com.example.mapper.RoleMenuMapper;
import com.example.mapper.UserMapper;
import com.example.service.IMenuService;
import com.example.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  Service implementation
 * </p>
 *
 * @author
 * @since 2022-01-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log LOG = Log.get();

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    @Override
    public UserDTO login(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if (one != null) {
            BeanUtil.copyProperties(one, userDTO, true);
            // Set token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userDTO.setToken(token);

            String role = one.getRole(); // ROLE_ADMIN
            // Set user menu list
            List<Menu> roleMenus = getRoleMenus(role);
            userDTO.setMenus(roleMenus);
            return userDTO;
        } else {
            throw new ServiceException(Constants.CODE_600, "Incorrect username or password");
        }
    }

    @Override
    public User register(UserDTO userDTO) {
        User one = getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, userDTO.getUsername()));
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(userDTO, one, true);
            // Default to regular user role
            one.setRole(RoleEnum.ROLE_USER.toString());
            save(one);  // Save copied user object to database
        } else {
            throw new ServiceException(Constants.CODE_600, "User already exists");
        }
        return one;
    }

    @Override
    public void updatePassword(UserPasswordDTO userPasswordDTO) {
        int update = userMapper.updatePassword(userPasswordDTO);
        if (update < 1) {
            throw new ServiceException(Constants.CODE_600, "Incorrect password");
        }
    }

    private User getUserInfo(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        User one;
        try {
            one = getOne(queryWrapper); // Query user from database
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "System error");
        }
        return one;
    }

    /**
     * Get menu list for current role
     * @param roleFlag
     * @return
     */
    private List<Menu> getRoleMenus(String roleFlag) {
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        // Menu id set for current role
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

        // Load all system menus as tree
        List<Menu> menus = menuService.findMenus("");
        // Create filtered result list
        List<Menu> roleMenus = new ArrayList<>();
        // Filter menus for current user role
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf()  Remove children not in menuIds
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }

}

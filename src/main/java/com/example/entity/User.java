package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since 2022-01-26
 */
@Getter
@Setter
@TableName("sys_user")
@Schema(name = "User Object", description = "")
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "Username")
    private String username;

    @Schema(description = "Password")
    private String password;

    @Schema(description = "Nickname")
    private String nickname;

    @Schema(description = "Email")
    private String email;

    @Schema(description = "Phone")
    private String phone;

    @Schema(description = "Address")
    private String address;

    @Schema(description = "Create time")
    private Date createTime;

    @Schema(description = "Avatar")
    private String avatarUrl;

    @Schema(description = "Role")
    private String role;
    private String sex;
    private String birth;

}

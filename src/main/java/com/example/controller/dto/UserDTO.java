package com.example.controller.dto;

import com.example.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * Login request parameters from frontend
 */
@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private String role;
    private List<Menu> menus;
}

package com.example.healthassistantbackend.controller;

import com.example.healthassistantbackend.dto.UserDTO;
import com.example.healthassistantbackend.entity.User;
import com.example.healthassistantbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 【修改】获取当前登录用户的完整个人信息
     * @param userDetails Spring Security自动注入的当前用户信息
     * @return 返回包含所有公开信息的UserDTO
     */
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        // 通过Service层找到用户实体
        User user = userService.findByUsername(userDetails.getUsername());
        // 通过Service层的辅助方法，将实体转换为DTO，避免暴露密码等敏感信息
        UserDTO userDTO = userService.convertToDto(user);
        return ResponseEntity.ok(userDTO);
    }

    /**
     * 【新增】更新当前登录用户的个人信息
     * @param userDetails Spring Security自动注入的当前用户信息
     * @param userDTO 从前端请求体中获取的、包含更新后信息的用户数据
     * @return 返回更新后的用户信息DTO
     */
    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateUserProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserDTO userDTO) {
        User updatedUser = userService.updateUserProfile(userDetails.getUsername(), userDTO);
        UserDTO updatedDto = userService.convertToDto(updatedUser);
        return ResponseEntity.ok(updatedDto);
    }


    /**
     * 【修改】上传用户头像接口，参数统一使用UserDetails
     * @param avatar 图片文件, 'avatar' 必须和客户端上传时使用的 part name 一致
     * @param userDetails Spring Security 自动注入的认证信息
     * @return 返回成功信息和新的头像URL
     */
    @PostMapping("/avatar")
    public ResponseEntity<?> uploadAvatar(
            @RequestParam("avatar") MultipartFile avatar,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String avatarUrl = userService.updateUserAvatar(userDetails.getUsername(), avatar);
            // 返回一个包含消息和新URL的Map
            return ResponseEntity.ok(Map.of("message", "头像上传成功", "avatarUrl", avatarUrl));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件上传失败: " + e.getMessage());
        }
    }
}
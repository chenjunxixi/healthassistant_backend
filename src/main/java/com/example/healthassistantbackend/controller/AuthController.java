package com.example.healthassistantbackend.controller;

import com.example.healthassistantbackend.dto.LoginRequest;
import com.example.healthassistantbackend.dto.LoginResponse;
import com.example.healthassistantbackend.entity.User;
import com.example.healthassistantbackend.service.UserService;
import com.example.healthassistantbackend.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // 【新增】导入 HttpStatus
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            registeredUser.setPassword(null);
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) { // 【推荐】返回类型明确为 LoginResponse
        try {
            // 1. 使用AuthenticationManager进行用户认证
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (Exception e) {
            // 2. 如果认证失败，返回符合前端结构的、带有错误信息的JSON
            LoginResponse errorResponse = new LoginResponse(401, "用户名或密码错误", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        // 3. 如果认证成功，生成JWT
        final String token = jwtUtil.generateToken(loginRequest.getUsername());

        // 4. 创建符合前端期望的、包含 code, message, data 的完整结构
        //    首先创建内部的 data 对象
        LoginResponse.UserData userData = new LoginResponse.UserData(token, loginRequest.getUsername());
        //    然后创建最外层的 LoginResponse 对象
        LoginResponse successResponse = new LoginResponse(200, "登录成功", userData);

        // 5. 返回这个结构完整的对象
        return ResponseEntity.ok(successResponse);
    }
}
package com.example.healthassistantbackend.service;

import com.example.healthassistantbackend.dto.UserDTO;
import com.example.healthassistantbackend.entity.User;
import com.example.healthassistantbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Path fileStorageLocation = Paths.get("D:/health_assistant/uploads/avatars").toAbsolutePath().normalize();

    public UserService() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("无法创建用于存储上传文件的目录！", ex);
        }
    }

    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("错误: 用户名已被占用!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);

        // 为用户创建一个包含 "ROLE_USER" 的权限列表
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities // 使用带有权限的列表
        );
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("找不到用户: " + username));
    }

    public String updateUserAvatar(String username, MultipartFile file) throws IOException {
        User user = findByUsername(username);
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension;
        try {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        } catch (Exception e) {
            throw new IOException("无效的文件扩展名", e);
        }
        String newFileName = username + fileExtension;
        Path targetLocation = this.fileStorageLocation.resolve(newFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        String avatarUrl = "/images/avatars/" + newFileName;
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);
        return avatarUrl;
    }
    public User updateUserProfile(String username, UserDTO userDTO) {
        User user = findByUsername(username);
        // 更新非空的字段
        if (userDTO.getNickname() != null) user.setNickname(userDTO.getNickname());
        if (userDTO.getGender() != null) user.setGender(userDTO.getGender());
        if (userDTO.getAge() != null) user.setAge(userDTO.getAge());
        if (userDTO.getHeightCm() != null) user.setHeightCm(userDTO.getHeightCm());
        if (userDTO.getWeightKg() != null) user.setWeightKg(userDTO.getWeightKg());
        return userRepository.save(user);
    }

    // 一个将User实体转换为DTO的辅助方法
    public UserDTO convertToDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setNickname(user.getNickname());
        dto.setGender(user.getGender());
        dto.setAge(user.getAge());
        dto.setHeightCm(user.getHeightCm());
        dto.setWeightKg(user.getWeightKg());
        return dto;
    }
}

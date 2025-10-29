package com.example.healthassistantbackend.config.filter;

import com.example.healthassistantbackend.service.UserService;
import com.example.healthassistantbackend.util.JwtUtil;
import io.jsonwebtoken.JwtException; // 导入JwtException
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger; // 使用SLF4J日志框架
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    
    private static final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            log.info("提取到 Bearer Token: {}", jwt);
        } else {
            // 尝试从参数获取，如果您的App不使用，可以忽略此日志
            jwt = request.getParameter("token");
            if (jwt != null) {
                log.info("从URL参数提取到 Token: {}", jwt);
            }
        }

        if (jwt != null) {
            try {
                username = jwtUtil.getUsernameFromToken(jwt);
                log.info("成功从Token中解析出用户名: {}", username);
            } catch (JwtException e) {
                // 【关键日志】捕获所有JWT相关的异常，例如签名错误、过期等
                log.error("JWT Token 解析失败: {}, Token: {}", e.getMessage(), jwt);
            } catch (Exception e) {
                log.error("在解析Token时发生未知错误: {}", e.getMessage());
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("SecurityContext为空，准备为用户 '{}' 进行验证", username);
            UserDetails userDetails = this.userService.loadUserByUsername(username);

            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                log.info("Token验证成功，为用户 '{}' 设置SecurityContext", username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                // 【关键日志】如果isTokenValid返回false，我们也需要知道
                log.warn("Token对用户 '{}' 验证失败 (isTokenValid返回false)", username);
            }
        }

        filterChain.doFilter(request, response);
    }
}
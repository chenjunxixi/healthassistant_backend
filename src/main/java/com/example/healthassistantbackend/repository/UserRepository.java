package com.example.healthassistantbackend.repository;

import com.example.healthassistantbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // 这个注解告诉Spring，这个接口是一个数据仓库组件，请Spring创建并管理它的实例
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA 的魔法：我们不需要写实现！
    // 只需要按照规范定义方法名，Spring就会自动生成查询SQL。
    // 这个方法会生成类似 "SELECT * FROM users WHERE username = ?" 的查询。
    // 使用 Optional 来包装返回结果是一个好习惯，它可以优雅地处理可能找不到用户的情况，避免空指针异常。
    Optional<User> findByUsername(String username);
}
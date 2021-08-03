package com.csw.rediscachedemo;

import com.csw.rediscachedemo.entity.User;
import com.csw.rediscachedemo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest
class RedisCacheDemoApplicationTests {
    @Autowired
    private UserService userService;

    @Test
    void insert() {
        User user = new User();
        user.setId(UUID.randomUUID().toString().replace("-", ""));
        user.setName("111");
        userService.insertUser(user);
    }

    @Test
    void update() {
        String userId = "f95733c21df8483b908405e7ba68fbf3";
        User user = userService.selectUserById(userId);
        user.setName("小xiao");
       User user0= userService.updateUser(user);
    }

    @Test
    void delete() {
        String userId = "f95733c21df8483b908405e7ba68fbf3";
        userService.deleteUser(userId);
    }

    @Test
    void select() {
        String userId = "f95733c21df8483b908405e7ba68fbf3";
        User user = userService.selectUserById(userId);
        System.out.println(user);
    }

    @Test
    void selectAll() {/*单表集合带参查询*/
        String userId = "f95733c21df8483b908405e7ba68fbf3";
        List<User> userList = userService.selectAllUser(userId);
        System.out.println(userList);

    }

    @Test
    void selectTable() {//模拟是联表集合，测试，多个keys匹配删除
        String userId = "f95733c21df8483b908405e7ba68fbf3";
        List<User> userList = userService.selectTable(userId);
        System.out.println(userList);

    }


}

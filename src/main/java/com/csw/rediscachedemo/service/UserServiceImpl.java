package com.csw.rediscachedemo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csw.rediscachedemo.annotation.DeleteAnnotation;
import com.csw.rediscachedemo.dao.UserDao;
import com.csw.rediscachedemo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "user")
public class UserServiceImpl implements UserService {
    //简单法：增删改删除空间所有缓存，查找建立缓存，按照表关联进行缓存空间建立
    /*稍微高效点的：
    增加：删除当前表的list缓存和联合list查询缓存
    删除和修改：删除当前条数据的缓存，当前表的list缓存和联合list查询缓存(修改的时候可以更新单条缓存)
    查询：创建缓存
    按照表关联进行缓存空间建立
     */
    @Autowired
    private UserDao userDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @DeleteAnnotation
    @Caching(evict = {
            @CacheEvict(key = "'selectAllUser*'"),
            @CacheEvict(key = "'selectTable*'")
    })
    public void insertUser(User user) {
        userDao.insert(user);
    }

    @Override
    @DeleteAnnotation
    @Caching(evict = {
            @CacheEvict(key = "'selectAllUser*'"),
            @CacheEvict(key = "'selectTable*'")
    })
    @CachePut(key = "'selectUserById'+#user.id")//更新需要返回对象
    public User updateUser(User user) {
        userDao.updateById(user);
        return user;
    }

    @Override
    @DeleteAnnotation
    @Caching(evict = {
            @CacheEvict(key = "'selectAllUser*'"),
            @CacheEvict(key = "'selectTable*'"),
            @CacheEvict(key = "'selectUserById'+#userId")
    })
    public void deleteUser(String userId) {
        userDao.deleteById(userId);
    }


    @Override
    @Cacheable(key = "'selectUserById'+#userId")
    public User selectUserById(String userId) {
        return userDao.selectById(userId);
    }

    @Override
    @Cacheable(key = "'selectAllUser'+#userId")
    public List<User> selectAllUser(String userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        return userDao.selectList(queryWrapper);
    }

    @Override
    @Cacheable(key = "'selectTable'+#userId")//模拟是联表集合，测试，多个keys匹配删除
    public List<User> selectTable(String userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        return userDao.selectList(queryWrapper);
    }

}

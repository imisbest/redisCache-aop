package com.csw.rediscachedemo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 功能描述:清除缓存切面类
 */
@Component
@Aspect
public class CacheRemoveAspect {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    //截获标有@DeleteAnnotation的方法
    @Pointcut(value = "(execution(* *.*(..)) && @annotation(com.csw.rediscachedemo.annotation.DeleteAnnotation))")
    private void pointcut() {
    }

    /**
     * 功能描述: 切面在截获方法返回值之后
     */
    @Before(value = "pointcut()")
    private void process(JoinPoint joinPoint) {
        //获取被代理的类
        Object target = joinPoint.getTarget();
        //获取切入方法的数据
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入方法
        Method method = signature.getMethod();
        //获得注解
        Caching caching = method.getAnnotation(Caching.class);
        CacheEvict[] evict = caching.evict();
        for (CacheEvict cacheEvict : evict) {
            String key = cacheEvict.key();
            char c = key.charAt(key.length() - 2);
            if ("*".equals(c + "")) {
                //获取前缀命名空间
                CacheConfig annotation = target.getClass().getAnnotation(CacheConfig.class);
                String[] strings = annotation.cacheNames();
                String cacheNames = strings[0];
                //获取后缀方法名
                String substring1 = key.substring(1, key.length() - 1);
                System.out.println(substring1);
                String resKeys = cacheNames + "::" + substring1;
                Set<String> keys = stringRedisTemplate.keys(resKeys);
                assert keys != null;
                stringRedisTemplate.delete(keys);
            }
        }
    }
}

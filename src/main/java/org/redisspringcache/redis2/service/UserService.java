package org.redisspringcache.redis2.service;

import org.redisspringcache.redis2.entity.User;
import org.redisspringcache.redis2.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    //日志对象
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    //键
    //private static final String CACHE_KEY_USER = "user:";

    @Autowired
    private UserMapper userMapper;

//    @Autowired
//    private RedisTemplate redisTemplate;

    //创建用户
    @CachePut(cacheNames = {"user"} ,key="#obj.id")
    public void createUser(User obj){
        //存进去
        this.userMapper.insertSelective(obj);
        //拿到key
        //String key = CACHE_KEY_USER + obj.getId();
        //拿到对象
        //obj = this.userMapper.selectByPrimaryKey(obj.getId());
        //缓存进redis
        //redisTemplate.opsForValue().set(key,obj);
    }

    //修改用户
    @CachePut(cacheNames = {"user"} , key="#obj.id")
    public User updateUser(User obj){

        //存进去
        this.userMapper.updateByPrimaryKeySelective(obj);
        //拿到key
        //String key = CACHE_KEY_USER + obj.getId();
        //拿到对象
        //obj = this.userMapper.selectByPrimaryKey(obj.getId());
        //缓存进redis
        //redisTemplate.opsForValue().set(key,obj);
        return this.userMapper.selectByPrimaryKey(obj.getId());

    }

    //删除用户
    @CacheEvict(cacheNames = {"user"} ,key="#userId")
    public void deleteUser(Integer userId){
        //让redis缓存失效
//        ValueOperations<String ,User> valueOperations = redisTemplate.opsForValue();
//        User user = valueOperations.get(CACHE_KEY_USER+userId);
//        if(user != null){
//            redisTemplate.delete(CACHE_KEY_USER+userId);
//        }
        this.userMapper.deleteByPrimaryKey(userId);
    }

    //查询用户
    @Cacheable(cacheNames = {"user"},key="#userId")
    public User findUser(Integer userId){
//        //先查询redis
//        ValueOperations<String ,User> valueOperations = redisTemplate.opsForValue();
//        User user = valueOperations.get(CACHE_KEY_USER+userId);
//        if(user == null){
//            user = this.userMapper.selectByPrimaryKey(userId);
//
//            //更新redis
//            valueOperations.set(CACHE_KEY_USER+userId, user);
//        }
//        return user;
        return this.userMapper.selectByPrimaryKey(userId);
    }

}

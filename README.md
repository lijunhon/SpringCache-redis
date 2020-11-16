# 说明
本项目是用springcache 和redis写的，主要用于实际项目中做参考
# 为什么用springcache实现
springcache是spring 3.1发布的，对缓存进行封装和抽象，通过在方法上使用注解能操作缓存，解决了业务代码和缓存代码耦合度问题，即不再侵入业务代码的基础上让现有代码即刻支持缓存。
对于redis缓存，springcache只支持String，其他的Hash、list、set、ZSet都不支持
# 实现步骤
## 第一步 pom文件加入依赖
```
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-cache</artifactId>
        </dependency>
        <!-- spring cache 连接池 -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
            <version>2.6.2</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
```
## 第二步 配置文件加入配置信息
```
##redis数据库索引
spring.redis.database=0
## redis服务器
spring.redis.host=xxxxx
## redis端口
spring.redis.port=6379
##redis密码
spring.redis.password=

# 连接池最大连接数
spring.redis.lettuce.pool.max-active=8
# 最大阻塞等待时间
spring.redis.lettuce.pool.max-wait=-1ms
# 最大空闲连接
spring.redis.lettuce.pool.max-idle=8
# 最小空闲连接
spring.redis.lettuce.pool.min-idle=0
#连接超时时间
spring.redis.timeout=5000ms
```
## 第三步 开启缓存，设置序列化方式
```
@Configuration
@EnableCaching
public class RedisConfig {
    @Primary
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory){
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration
                //设置30分钟过期
                .entryTtl(Duration.ofMinutes(30L))
                //空值不缓存
                .disableCachingNullValues()
                //设置key序列化器
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                //设置value序列化器
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()));

        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

    /**
     * key序列化器
     */
    private RedisSerializer<String> keySerializer(){
        return new StringRedisSerializer();
    }

    /**
     * value序列化器
     */
    private RedisSerializer<Object> valueSerializer(){
        return new GenericJackson2JsonRedisSerializer();
    }
}
```
# 用法
由于使用了swagger，浏览器访问 127.0.0.1:8080/swagger-ui.html#即可
# SpringCache的坑
1. 对于redis缓存，只支持string，对于hash,list等用redistemplate
2. springcache不支持多表查询的数据缓存，只能用redistemplate

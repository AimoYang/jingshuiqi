package com.jingshuiqi.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;


@Service
public class RedisService {

	 @Autowired
	    private StringRedisTemplate stringRedisTemplate; 
	    @Autowired
	    private RedisTemplate redisTemplate; 
	    
	    /**
	     * 写入缓存
	     * @param key
	     * @param value
	     * @return
	     */
	    public boolean set(final String key, Object value) {
	        boolean result = false;
	        try {
	            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
	            operations.set(key, value);
	            result = true;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return result;
	    }
	    /**
	     * 写入缓存设置时效时间
	     * @param key
	     * @param value
	     * @return
	     */
	    public boolean set(final String key, Object value, Long expireTime) {
	        boolean result = false;
	        try {
	            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
	            operations.set(key, value);
	            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
	            result = true;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return result;
	    }
	    
	    /**
	     * 设置缓存时效
	     * @author miao
	     * @param k
	     * @param expireTime
	     * @return
	     */
	    public Boolean expireTime(String k,Long expireTime){
	    	boolean result = false;
	        try {
	            redisTemplate.expire(k, expireTime, TimeUnit.SECONDS);
	            result = true;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return result;
	    }
	    
	    /**
	     * 批量删除对应的value
	     * @param keys
	     */
	    public void remove(final String... keys) {
	        for (String key : keys) {
	            remove(key);
	        }
	    }

	    /**
	     * 批量删除key
	     * @param pattern
	     */
	    public void removePattern(final String pattern) {
	        Set<Serializable> keys = redisTemplate.keys(pattern);
	        if (keys.size() > 0)
	            redisTemplate.delete(keys);
	    }
	    /**
	     * 删除对应的value
	     * @param key
	     */
	    public void remove(final String key) {
	        if (exists(key)) {
	            redisTemplate.delete(key);
	        }
	    }
	    /**
	     * 判断缓存中是否有对应的value
	     * @param key
	     * @return
	     */
	    public boolean exists(final String key) {
	        return redisTemplate.hasKey(key);
	    }
	    /**
	     * 读取缓存
	     * @param key
	     * @return
	     */
	    public Object get(final String key) {
	        Object result = null;
	        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
	        result = operations.get(key);
	        return result;
	    }
	    /**
	     * 哈希 添加
	     * @param key
	     * @param hashKey
	     * @param value
	     */
	    public void hmSet(String key, Object hashKey, Object value){
	        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
	        hash.put(key,hashKey,value);
	    }

	    /**
	     * 哈希获取数据
	     * @param key
	     * @param hashKey
	     * @return
	     */
	    public Object hmGet(String key, Object hashKey){
	        HashOperations<String, Object, Object>  hash = redisTemplate.opsForHash();
	        return hash.get(key,hashKey);
	    }

	    /**
	     * 列表添加
	     * 将所有指定的值插入存储在键的列表的尾部。如果键不存在，则在执行推送操作之前将其创建为空列表。（从右边插入）
	     * @param k
	     * @param v
	     */
	    public void lRightPush(String k,Object v){
	        ListOperations<String, Object> list = redisTemplate.opsForList();
	        list.rightPush(k,v);
	    }
	    
	    /**
	     * 列表添加
	     * 将所有指定的值插入存储在键的列表的头部。如果键不存在，则在执行推送操作之前将其创建为空列表。（从左边插入）
	     * @param k
	     * @param v
	     */
	    public void lLeftPush(String k,Object v){
	        ListOperations<String, Object> list = redisTemplate.opsForList();
	        list.leftPush(k,v);
	    }

	    /**
	     * 列表获取
	     * @param k
	     * @param l
	     * @param l1
	     * @return
	     */
	    public List<Object> lRange(String k, long l, long l1){
	        ListOperations<String, Object> list = redisTemplate.opsForList();
	        return list.range(k,l,l1);
	    }
	    
	    /**
	     * 列表截切
	     * @author miao
	     * @param key
	     * @param start
	     * @param end
	     * @return
	     */
	    public void lTrim(String key,long start,long end){
	    	//void trim(K key, long start, long end);
	    	//修剪现有列表，使其只包含指定的指定范围的元素，起始和停止都是基于0的索引
	    	ListOperations<String, Object> list = redisTemplate.opsForList();
	    	list.trim(key, start, end);
	    }
	    
	    /**
	     * 列表删除，指定元素
	     * @author miao
	     * @param k
	     * @param count
	     * @param value
	     */
	    public Long lrem(String k,long count,Object value){
	    	/*
	    	 * 从存储在键中的列表中删除等于值的元素的第一个计数事件。
				计数参数以下列方式影响操作：
				count> 0：删除等于从头到尾移动的值的元素。(1则删除正数第一次出现value的元素)
				count <0：删除等于从尾到头移动的值的元素。
				count = 0：删除等于value的所有元素。
	    	 */
	    	Long l =redisTemplate.opsForList().remove(k, count, value);
	    	
	    	return 1L;
	    }
	    
	    

	    /**
	     * 集合添加
	     * @param key
	     * @param value
	     */
	    public void add(String key,Object value){
	        SetOperations<String, Object> set = redisTemplate.opsForSet();
	        set.add(key,value);
	    }

	    /**
	     * 集合获取
	     * @param key
	     * @return
	     */
	    public Set<Object> setMembers(String key){
	        SetOperations<String, Object> set = redisTemplate.opsForSet();
	        return set.members(key);
	    }

	    /**
	     * 有序集合添加
	     * @param key
	     * @param value
	     * @param scoure
	     */
	    public void zAdd(String key,Object value,double scoure){
	        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
	        zset.add(key,value,scoure);
	    }

	    /**
	     * 有序集合获取
	     * @param key
	     * @param scoure
	     * @param scoure1
	     * @return
	     */
	    public Set<Object> rangeByScore(String key,double scoure,double scoure1){
	        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
	        return zset.rangeByScore(key, scoure, scoure1);
	    }
}

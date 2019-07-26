package com.airshop.user.service.impl;

import com.airshop.user.mapper.UserMapper;
import com.airshop.user.pojo.User;
import com.airshop.user.service.UserService;
import com.airshop.utils.CodecUtils;
import com.airshop.utils.NumberUtils;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author ouyanggang
 * @Date 2019/7/26 - 14:25
 */
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserMapper userMapper;

    private static final String KEY_PREFIX = "user:code:phone";
    private static final String KEY_PREFIX2 = "airshop:user:info";

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public Boolean checkData(String data, Integer type) {
        User user = new User();
        switch (type){
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                return null;

        }
        return this.userMapper.selectCount(user)==0;
    }

    @Override
    public Boolean sendVertifyCode(String phone) {
        String code = NumberUtils.generateCode(6);

        Map<String, String> msg = new HashMap();
        try{
            msg.put("phone", phone);
            msg.put("code", code);

            System.err.println("---------------验证码："+code);

            this.stringRedisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);

            return true;
        }catch (Exception e){
            log.error("发送短信失败。phone：{}，code：{}", phone, code);
            return false;
        }

    }

    @Override
    public User queryUser(String username, String password) {

        BoundHashOperations<String, String, String> hashOps = this.stringRedisTemplate.boundHashOps(KEY_PREFIX2);

        String userJson = hashOps.get(username);
        User user;
        if (StringUtils.isEmpty(userJson)){
            User record = new User();
            record.setUsername(username);
            user = this.userMapper.selectOne(record);
        }else {
            user = JSON.parseObject(userJson, User.class);
        }

        if (user == null){
            return null;
        }

        hashOps.put(user.getUsername(), JSON.toJSONString(user));

        Boolean result = CodecUtils.passwordConfirm(username + password, user.getPassword());

        if (!result){
            return null;
        }

        return user;
    }

    @Override
    public Boolean register(User user, String code) {
        String key = KEY_PREFIX + user.getPhone();
        String codeCache = this.stringRedisTemplate.opsForValue().get(key);

        if (!codeCache.equals(code)){
            return false;
        }

        user.setId(null);
        user.setCreated(new Date());

        String encodePassword = CodecUtils.passwordBcryptEncode(user.getUsername().trim(), user.getPassword().trim());
        user.setPassword(encodePassword);

        boolean result = this.userMapper.insertSelective(user) == 1;

        if (result){
            try{
                this.stringRedisTemplate.delete(KEY_PREFIX + user.getPhone());
            }catch (Exception e){
                log.error("删除缓存验证码失败，code:{}", code, e);
            }
        }

        return result;
    }
}

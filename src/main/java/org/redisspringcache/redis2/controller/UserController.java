package org.redisspringcache.redis2.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.redisspringcache.redis2.entity.User;
import org.redisspringcache.redis2.service.UserService;
import org.redisspringcache.redis2.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Random;

@Api(value = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @ApiOperation("/初始化数据库 100条")
    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public void init(){
        for( int i = 0;i <100; i++){
            Random rand = new Random();
            User user = new User();
            String temp = "user" + i;
            user.setUsername(temp);
            user.setPassword(temp);
            user.setSex((byte) rand.nextInt(2));
            user.setCreateTime(new Date(new Date().getTime()));
            userService.createUser(user);
        }
    }

    @ApiOperation("/修改单个用户")
    @PostMapping(value = "/update")
    public void update(@RequestBody UserVo obj){
        User user = new User();
        BeanUtils.copyProperties(obj, user);
        userService.updateUser(user);
    }

    @ApiOperation("/查询单个用户，参数是主键 userid")
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public UserVo findById(@PathVariable int id) {
        User user = this.userService.findUser(id);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

    @ApiOperation("/删除单个用户，参数是userid")
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public void deleteById(@PathVariable int id){
        userService.deleteUser(id);
    }

}

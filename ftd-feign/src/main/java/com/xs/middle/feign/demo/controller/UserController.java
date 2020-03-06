package com.xs.middle.feign.demo.controller;

import com.google.gson.Gson;
import com.xs.middle.api.domain.User;
import com.xs.middle.feign.demo.api.UserServiceRemote;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class UserController {

    @Resource
    UserServiceRemote userServiceRemote;

    @RequestMapping("/user/get/{name}")
    public String getUser(@PathVariable String name){
        log.info("user-get-:{}",name);
        User user = userServiceRemote.getUser(name);
        return new Gson().toJson(user);
    }


}

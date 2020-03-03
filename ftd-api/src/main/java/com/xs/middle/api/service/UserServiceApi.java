package com.xs.middle.api.service;

import com.xs.middle.api.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/user")
public interface UserServiceApi {

    @RequestMapping("/getUser")
    User getUser(@RequestParam String name);

}

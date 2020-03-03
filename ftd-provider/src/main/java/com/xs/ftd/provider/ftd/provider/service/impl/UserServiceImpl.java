package com.xs.ftd.provider.ftd.provider.service.impl;

import com.xs.middle.api.domain.User;
import com.xs.middle.api.service.UserServiceApi;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaos
 * @date 2019/11/11 17:06
 */
@RestController
public class UserServiceImpl implements UserServiceApi {

    @Override
    public User getUser(String name) {
        return new User("xxq",25);
    }
}

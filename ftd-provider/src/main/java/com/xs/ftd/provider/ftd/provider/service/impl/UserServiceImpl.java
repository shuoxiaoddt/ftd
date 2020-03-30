package com.xs.ftd.provider.ftd.provider.service.impl;

import com.xs.middle.api.domain.User;
import com.xs.middle.api.service.UserServiceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaos
 * @date 2019/11/11 17:06
 */
@RestController
@Slf4j
public class UserServiceImpl implements UserServiceApi {

    @Override
    public User getUser(String name) {
        log.info("provider : user-get-:{}",name);
        return new User("xxq",25);
    }
}

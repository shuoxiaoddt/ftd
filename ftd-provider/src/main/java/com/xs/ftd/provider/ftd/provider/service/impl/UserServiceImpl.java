package com.xs.ftd.provider.ftd.provider.service.impl;

import com.xs.ftd.provider.ftd.provider.service.UserService;

/**
 * @author xiaos
 * @date 2019/11/11 17:06
 */
public class UserServiceImpl implements UserService {

    @Override
    public String getName(String userId) {
        return "name:"+userId;
    }

}

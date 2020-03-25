package com.xs.middle.compent.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaos
 * @date 2019/11/11 17:06
 */
@RestController
@RequestMapping("/user")
public interface UserService {

    @GetMapping("/name")
    public String getName(@RequestParam("userId") String userId);

}

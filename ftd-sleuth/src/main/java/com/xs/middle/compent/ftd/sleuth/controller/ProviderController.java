package com.xs.middle.compent.ftd.sleuth.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaos
 * @date 2020/1/9 16:48
 */
@RestController
@RequestMapping("/provider")
public class ProviderController {

    @RequestMapping("/getRealName/{name}")
    public String getRealName(@PathVariable String name){
        return "realName:"+name;
    }
}

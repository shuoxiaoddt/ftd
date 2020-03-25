package com.xs.middle.compent.ftdspringboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaos
 * @date 2020/1/3 14:21
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/{name}")
    public String hello(@PathVariable("name") String name){
        return "hello->"+name;
    }
}

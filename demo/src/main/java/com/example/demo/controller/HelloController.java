package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaos
 * @date 2020/1/3 16:26
 */
@RequestMapping("/hello")
@RestController
public class HelloController {

    @GetMapping("/{name}")
    public String say(@PathVariable String name){
        return "hello->"+name;
    }
}

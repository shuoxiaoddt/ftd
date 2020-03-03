package com.xs.middle.feign.demo.api;

import com.xs.middle.api.service.UserServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${com.xs.middle.provider:}")
public interface UserServiceRemote extends UserServiceApi {
}

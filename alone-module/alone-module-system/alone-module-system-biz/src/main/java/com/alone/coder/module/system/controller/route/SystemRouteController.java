package com.alone.coder.module.system.controller.route;

import com.alone.coder.module.seepro.api.SeeProApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "管理后台 - 路由")
@RestController
public class SystemRouteController {

    @Resource
    private SeeProApi seeProApi;

    @GetMapping(value = "/test")
    public void request() {
        System.out.println("请求路由");
        String body = seeProApi.getWebSocketUrl("", "1", "{}");
        System.out.println(body);
        System.out.println("请求结束");
    }
}

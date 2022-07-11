package com.hot.place.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hot.place.controller.ApiResult.OK;

@RestController
@RequestMapping("/api/_hcheck")
public class HealthCheckRestController {

    @GetMapping
    public ApiResult<Long> healthCheck() {
        return OK(System.currentTimeMillis());
    }
}

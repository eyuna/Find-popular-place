package com.hot.place.controller.user;

import com.hot.place.controller.ApiResult;
import com.hot.place.error.NotFoundException;
import com.hot.place.model.user.Email;
import com.hot.place.security.JwtAuthentication;
import com.hot.place.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.jni.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hot.place.controller.ApiResult.OK;

@RestController
@RequestMapping("api")
@Api(tags = "사용자 APIs")
public class UserRestController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("user/me")
    @ApiOperation(value = "내 정보")
    public ApiResult<UserDto> me(@AuthenticationPrincipal JwtAuthentication authentication) {
        return OK(
                userService.findByEmail(authentication.email)
                .map(UserDto::new)
                .orElseThrow(() -> new NotFoundException(User.class, authentication.email))
        );
    }
}

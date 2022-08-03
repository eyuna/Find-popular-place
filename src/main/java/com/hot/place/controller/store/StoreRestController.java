package com.hot.place.controller.store;

import com.hot.place.controller.ApiResult;
import com.hot.place.model.store.Store;
import com.hot.place.security.JwtAuthentication;
import com.hot.place.service.store.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.hot.place.controller.ApiResult.OK;

@RestController
@RequestMapping("api/store")
@Api(tags = "가게 APIs")
public class StoreRestController {

    private final StoreService storeService;

    public StoreRestController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping(path = "/reg")
    @ApiOperation(value = "가게 등록")
    public ApiResult<RegistrationResult> register(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @RequestBody RegistrationRequest request
    ) {
        return OK(
                new RegistrationResult(
                    new StoreDto(
                        storeService.register(request.getName(), request.getZipCode(), request.getAddress(), authentication.id)
                    )
                )
        );
    }
}

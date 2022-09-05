package com.hot.place.controller.user;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.hot.place.aws.S3Client;
import com.hot.place.controller.ApiResult;
import com.hot.place.error.NotFoundException;
import com.hot.place.model.commons.AttachedFile;
import com.hot.place.model.user.Email;
import com.hot.place.model.user.Role;
import com.hot.place.model.user.User;
import com.hot.place.security.Jwt;
import com.hot.place.security.JwtAuthentication;
import com.hot.place.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

import static com.hot.place.controller.ApiResult.OK;
import static com.hot.place.model.commons.AttachedFile.toAttachedFile;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@RestController
@RequestMapping("api")
@Api(tags = "사용자 APIs")
public class UserRestController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Jwt jwt;

    private final UserService userService;

    private final S3Client s3Client;

    public UserRestController(Jwt jwt, UserService userService, S3Client s3Client) {
        this.jwt = jwt;
        this.userService = userService;
        this.s3Client = s3Client;
    }

    @PostMapping(path = "user/exists")
    @ApiOperation(value = "이메일 중복확인 (API 토큰 필요없음)")
    public ApiResult<Boolean> checkEmail(
            @RequestBody @ApiParam(value = "example: {\"address\": \"test00@gmail.com\"}") Map<String, String> request
    ) {
        Email email = new Email(request.get("address"));
        return OK(
                userService.findByEmail(email).isPresent()
        );
    }

    @PostMapping(path = "user/join", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "회원가입 (API 토큰 필요없음)")
    public ApiResult<JoinResult> join(
            @ModelAttribute JoinRequest joinRequest,
            @RequestPart(required = false) MultipartFile file
    ) {
        User user = userService.join(
                joinRequest.getName(),
                new Email(joinRequest.getPrincipal()),
                joinRequest.getCredentials()
        );

        toAttachedFile(file).ifPresent(attachedFile ->
                supplyAsync(() ->
                        uploadProfileImage(attachedFile)
                ).thenAccept(opt ->
                        opt.ifPresent(profileImageUrl ->
                                userService.updateProfileImage(user.getSeq(), profileImageUrl)
                        )
                )
        );

        String apiToken = user.newApiToken(jwt, new String[]{Role.USER.value()});
        return OK(
                new JoinResult(apiToken, new UserDto(user))
        );
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

    public Optional<String> uploadProfileImage(AttachedFile profileFile) {
        String profileImageUrl = null;
        if (profileFile != null) {
            String key = profileFile.randomName("profiles", "jpeg");
            try {
                profileImageUrl = s3Client.upload(profileFile.inputStream(), profileFile.length(), key, profileFile.getContentType(), null);
            } catch (AmazonS3Exception e) {
                log.warn("Amazon S3 error (key: {}): {}", key, e.getMessage(), e);
            }
        }
        return Optional.ofNullable(profileImageUrl);
    }
}

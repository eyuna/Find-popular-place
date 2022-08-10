package com.hot.place.controller.post;

import com.hot.place.controller.ApiResult;
import com.hot.place.security.JwtAuthentication;
import com.hot.place.service.post.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hot.place.controller.ApiResult.OK;

@RestController
@RequestMapping("api")
@Api(tags = "POST APIs")
public class PostRestController {

    private final PostService postService;

    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("post")
    @ApiOperation(value = "글 작성")
    public ApiResult<PostDto> posting(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @RequestBody PostingRequest postingRequest
    ) {
        return OK(
                new PostDto(
                        postService.write(
                                postingRequest.newPost(authentication.id)
                        )
                )
        );
    }
}

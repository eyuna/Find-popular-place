package com.hot.place.controller.post;

import com.hot.place.configure.support.Pageable;
import com.hot.place.controller.ApiResult;
import com.hot.place.security.JwtAuthentication;
import com.hot.place.service.post.PostService;
import io.swagger.annotations.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hot.place.controller.ApiResult.OK;
import static java.util.stream.Collectors.toList;

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

    @GetMapping(path = "user/{userId}/post/list")
    @ApiOperation(value = "포스트 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "offset", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "limit", dataType = "integer", paramType = "query", defaultValue = "20", value = "최대 조회 갯수")
    })
    public ApiResult<List<PostDto>> posts(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable @ApiParam(value = "조회 대상자 PK (본인 또는 친구)", example = "1") Long userId,
            Pageable pageable
    ) {
        return OK(
                postService.findAll(userId, authentication.id, pageable.offset(), pageable.limit()).stream()
                        .map(PostDto::new)
                        .collect(toList())
        );
    }
}

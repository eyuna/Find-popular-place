package com.hot.place.configure.support;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.apache.commons.lang3.math.NumberUtils.toInt;
import static org.apache.commons.lang3.math.NumberUtils.toLong;

/**
 *  RequestBody 어노테이션을 사용해 Request 의 Body 값을 받아올 때
 * PathVariable 어노테이션을 사용해 Request 의 Path Parameter 값을 받아올 때
 * */
public class SimpleOffsetPageableHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final int DEFAULT_OFFSET = 0;

    private static final int DEFAULT_LIMIT = 5;

    private final String offsetParam;

    private final String limitParam;

    public SimpleOffsetPageableHandlerMethodArgumentResolver() {
        this("offset", "limit");
    }

    public SimpleOffsetPageableHandlerMethodArgumentResolver(String offsetParam, String limitParam) {
        this.offsetParam = offsetParam;
        this.limitParam = limitParam;
    }

    // 어떤 파라미터에 대해 작업을 수행할 것인지를 정의
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Pageable.class.isAssignableFrom(parameter.getParameterType());
    }

    // parameter 에 전달할 객체에 대한 조작을 자유롭게 진행한 뒤 해당 객체를 리턴
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String offsetString = webRequest.getParameter(offsetParam);
        String limitString = webRequest.getParameter(limitParam);

        long offset = toLong(offsetString, DEFAULT_OFFSET);
        int limit = toInt(limitString, DEFAULT_LIMIT);

        if (offset < 0) {
            offset = DEFAULT_OFFSET;
        }
        if (limit < 1 || limit > 5) {
            limit = DEFAULT_LIMIT;
        }

        return new SimpleOffsetPageRequest(offset, limit);
    }
}

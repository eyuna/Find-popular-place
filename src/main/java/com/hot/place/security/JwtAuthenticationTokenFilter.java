package com.hot.place.security;

import com.hot.place.model.user.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * HTTP Request-Header 에서 JWT 값을 추출하고, JWT 값이 올바르다면 인증정보 {@link JwtAuthenticationToken}를 생성한다.
 * 생성된 {@link JwtAuthenticationToken} 인스턴스는 {@link SecurityContextHolder}를 통해 Thread-Local 영역에 저장된다.
 *
 * 인증이 완료된 {@link JwtAuthenticationToken#principal}부분 에는 {@link JwtAuthentication} 인스턴스가 set 된다.
 */
public class JwtAuthenticationTokenFilter extends GenericFilterBean {

    private static final Pattern BEARER = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);
    // 일반적으로 토큰은 요청 헤더의 Authorization 필드에 담아져 보내짐
    // Authorization: <type> <credentials>, bearer 는 type 에 해당
    // CASE_INSENSITIVE : 대소문자 구분 x

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final String headerKey;

    private final Jwt jwt;

    public JwtAuthenticationTokenFilter(String headerKey, Jwt jwt) {
        this.headerKey = headerKey;
        this.jwt = jwt;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String authorizationToken = obtainAuthorizationToken(request); // HTTP header 에서 가져오기
            // JWT 값이 있다면, JWT 값을 검증하고 인증정보를 생성해 SecurityContextHolder 에 추가한다.
            if (authorizationToken != null) {
                try {
                    Jwt.Claims claims = verify(authorizationToken);
                    log.debug("Jwt parse result: {}", claims);

                    // 만료 10분 전이면 refresh
                    if (canRefresh(claims, 6000 * 10)) {
                        String refreshedToken = jwt.refreshToken(authorizationToken);
                        response.setHeader(headerKey, refreshedToken);
                    }

                    Long userKey = claims.userKey;
                    String name = claims.name;
                    Email email = claims.email;

                    List<GrantedAuthority> authorities = obtainAuthorities(claims);

                    if (nonNull(userKey) && isNotEmpty(name) && nonNull(email) && authorities.size() > 0) {
                        JwtAuthenticationToken authentication =
                                new JwtAuthenticationToken(new JwtAuthentication(userKey, email, name), null, authorities);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (Exception e) {
                    log.warn("Jwt processing failed: {}", e.getMessage());
                }
            }
        } else {
            log.debug("SecurityContextHolder not populated with security token, as it already contained: '{}'",
                    SecurityContextHolder.getContext().getAuthentication());
        }
        filterChain.doFilter(request, response);
    }

    private boolean canRefresh(Jwt.Claims claims, long refreshRangeMillis) {
        long exp = claims.exp();
        if (exp > 0) {
            long remain = exp - System.currentTimeMillis();
            return remain < refreshRangeMillis;
        }
        return false;
    }

    private List<GrantedAuthority> obtainAuthorities(Jwt.Claims claims) {
        String[] roles = claims.roles;
        return roles == null || roles.length == 0 ?
                Collections.emptyList() :
                Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(toList());
    }

    private String obtainAuthorizationToken(HttpServletRequest request) {
        String token = request.getHeader(headerKey);
        if (token != null) {
            if(log.isDebugEnabled()) // 레벨이 info 로 되어있으면 아래 문장을 실행하지 않아서 새로운 객체가 생성되는 등 오버헤드 방지
                log.debug("Jwt authorization api detected: {}", token);
            token = URLDecoder.decode(token, StandardCharsets.UTF_8);
            String[] parts = token.split(" ");
            if (parts.length == 2) {
                String scheme = parts[0];
                String credentials = parts[1];
                return BEARER.matcher(scheme).matches() ? credentials : null;
            }
        }
        return null;
    }

    private Jwt.Claims verify(String token) {
        return jwt.verify(token);
    }
}

package com.hot.place.configure;

import com.hot.place.model.user.Role;
import com.hot.place.security.*;
import com.hot.place.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.math.NumberUtils.toLong;

@Configuration
@EnableWebSecurity // SpringSecurityFilterChain 포함
public class WebSecurityConfigure {

    private final Jwt jwt;

    private final JwtTokenConfigure jwtTokenConfigure;

    private final JwtAccessDeniedHandler accessDeniedHandler;

    private final EntryPointUnauthorizedHandler unauthorizedHandler;

    public WebSecurityConfigure(Jwt jwt, JwtTokenConfigure jwtTokenConfigure, JwtAccessDeniedHandler accessDeniedHandler, EntryPointUnauthorizedHandler unauthorizedHandler) {
        this.jwt = jwt;
        this.jwtTokenConfigure = jwtTokenConfigure;
        this.accessDeniedHandler = accessDeniedHandler;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder builder, JwtAuthenticationProvider authenticationProvider) {
        // AuthenticationManager 는 AuthenticationProvider 목록을 지니고 있다.
        // 이 목록에 JwtAuthenticationProvider 를 추가한다.
        builder.authenticationProvider(authenticationProvider);
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(Jwt jwt, UserService userService) {
        return new JwtAuthenticationProvider(jwt, userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public ConnectionBasedVoter connectionBasedVoter() {
        final String regex = "^/api/user/(\\d+)/post/.*$";
        final Pattern pattern = Pattern.compile(regex);
        RequestMatcher requiresAuthorizationRequestMatcher = new RegexRequestMatcher(pattern.pattern(), null);
        return new ConnectionBasedVoter(
                requiresAuthorizationRequestMatcher,
                (String url) -> {
                    /* url에서 targetId를 추출하기 위해 정규식 처리 */
                    Matcher matcher = pattern.matcher(url);
                    long id = matcher.matches() ? toLong(matcher.group(1), -1) : -1;
                    return id;
                }
        );
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() { // 권한 위원회
        List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
        decisionVoters.add(new WebExpressionVoter()); // hasRole~~
        // voter 목록에 connectionBasedVoter 를 추가함
        decisionVoters.add(connectionBasedVoter());
        // 모든 voter 승인해야 해야함
        return new UnanimousBased(decisionVoters);
    }

    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter(jwtTokenConfigure.getHeader(), jwt);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/swagger-resources", "/webjars/**", "/static/**", "/templates/**", "/v2/**", "/h2/**");
    }

//    @Bean
//    @Order(0)
//    public SecurityFilterChain resources(HttpSecurity http) throws Exception {
//        return http.requestMatchers(matchers -> matchers
//                        .antMatchers("/swagger-resources", "/webjars/**", "/static/**", "/templates/**", "/v2/**", "/h2/**"))
//                .authorizeHttpRequests(authorize -> authorize
//                        .anyRequest().permitAll())
//                .build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf()
                .disable()
                .headers()
                .disable()
                .exceptionHandling()
                // 액세스가 가능한지 권한 체크 후 액세스 할 수 없는 요청인 경우
                .accessDeniedHandler(accessDeniedHandler)
                // 인증되지 않은 유저의 요청인 경우
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                // JWT 인증을 사용하므로 무상태(STATELESS) 전략 설정
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/_hcheck").permitAll()
                .antMatchers("/api/auth").permitAll()
                .antMatchers("/api/user/join").permitAll()
                .antMatchers("/api/user/exists").permitAll()
                .antMatchers("/api/**").hasRole(Role.USER.name())
                // 인가
                .accessDecisionManager(accessDecisionManager())
                .anyRequest().permitAll()
                .and()
                // JWT 인증을 사용하므로 form 로긴은 비활성처리
                .formLogin()
                .disable();
        http
                // 필터 체인 변경
                // UsernamePasswordAuthenticationFilter 앞에 jwtAuthenticationTokenFilter 를 추가한다.
                .addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

package nana.TrialTrove.config;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import nana.TrialTrove.service.CustomOAuth2UserService;
import nana.TrialTrove.service.MemberDetailsService;
import org.apache.catalina.security.SecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SpringSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final MemberDetailsService memberDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;

    // 스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/static/**");
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService) throws Exception {
        http
                .authorizeHttpRequests((authorizeRequests) -> {

                    authorizeRequests.requestMatchers("/member/myInfo","/member/updateMyInfo","/board/write","/board/detail/**","/product/favorite/**","/product/apply/**", "/ws/**")
                            .hasAnyRole("ADMIN", "USER");

                    authorizeRequests.requestMatchers("/board/reply/**","/product/enroll","/product/update")
                            .hasRole("ADMIN");
                    authorizeRequests.requestMatchers("/uploads/**","/product/trialDetail/**").permitAll();
                    authorizeRequests.anyRequest().permitAll();
                })
                .formLogin((formLogin) -> {
                    /* 권한이 필요한 요청은 해당 url로 리다이렉트 */
                    formLogin.loginPage("/member/login").permitAll();
                    formLogin.defaultSuccessUrl("/");
                })
                .httpBasic(withDefaults());
        http
                .csrf(csrf -> csrf
                //.csrfTokenRequestHandler()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers("/", "/member/**", "/board/**", "/product/**", "/admin/**", "/ws/**", "/messages/**")
        );
        http
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.ALWAYS) //스프링시큐리티가 항상 세션을 생성
                );

        http
                .oauth2Login(oauth2 -> {
                oauth2.defaultSuccessUrl("/"); // 로그인 성공 후 이동할 페이지 URL
                oauth2.userInfoEndpoint(userInfo -> {
                    userInfo.userService(customOAuth2UserService);
                });
            });

        http
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID","remember-me")
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("password")
                .roles("ADMIN", "USER")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    // 인증 관리자 관련 설정
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(memberDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());

        return daoAuthenticationProvider;
    }



    // 패스워드 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

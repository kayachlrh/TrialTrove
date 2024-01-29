//package nana.TrialTrove.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .formLogin(Customizer.withDefaults())
//                .authorizeHttpRequests(authorizeRequest ->
//                        authorizeRequest
//                                .requestMatchers(
//                                        AntPathRequestMatcher.antMatcher("/auth/**")
//                                ).authenticated()
//                                .requestMatchers(
//                                        AntPathRequestMatcher.antMatcher("/h2-console/**")
//                                ).permitAll()
//                )
//                .headers(
//                        headersConfigurer ->
//                                headersConfigurer
//                                        .frameOptions(
//                                                HeadersConfigurer.FrameOptionsConfig::sameOrigin
//                                        )
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        // 정적 리소스 spring security 대상에서 제외
//        return (web) ->
//                web
//                        .ignoring()
//                        .requestMatchers(
//                                PathRequest.toStaticResources().atCommonLocations()
//                        );
//    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//
//}
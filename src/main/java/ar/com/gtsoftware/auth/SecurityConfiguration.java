package ar.com.gtsoftware.auth;

import static org.springframework.security.config.Customizer.withDefaults;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final JwtUnAuthorizedResponseAuthenticationEntryPoint
      jwtUnAuthorizedResponseAuthenticationEntryPoint;

  private final UserDetailsService userDetailsService;

  private final JwtTokenAuthorizationOncePerRequestFilter jwtAuthenticationTokenFilter;

  @Value("${jwt.get.token.uri}")
  private String authenticationPath;

  @Bean
  public PasswordEncoder passwordEncoderBean() {
    return new HashPasswordEncoder();
  }

  //
  //  /*~~(Migrate manually based on
  // https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter)~~>*/@Bean
  //  @Override
  //  public AuthenticationManager authenticationManagerBean() throws Exception {
  //    return super.authenticationManagerBean();
  //  }
  //
  //  /*~~(Migrate manually based on
  // https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter)~~>*/@Override
  //  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
  //    super.configure(auth);
  //    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoderBean());
  //  }
  //
  //  @Override
  //  protected void configure(HttpSecurity httpSecurity) throws Exception {
  //    httpSecurity
  //        .csrf(csrf -> csrf
  //            .disable())
  //        .exceptionHandling(handling -> handling
  //            .authenticationEntryPoint(jwtUnAuthorizedResponseAuthenticationEntryPoint))
  //        .sessionManagement(management -> management
  //            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
  //        .authorizeHttpRequests(requests -> requests
  //            .requestMatchers("/**/*.{js,html,css,ico,svg,png}")
  //            .permitAll()
  //            .anyRequest()
  //            .authenticated());
  //
  //    httpSecurity.addFilterBefore(
  //        jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
  //
  //    httpSecurity
  //        .headers(headers -> headers
  //            .frameOptions(options -> options
  //                .sameOrigin() // H2 Console Needs this setting
  //                .cacheControl(withDefaults()))); // disable caching
  //  }
  //
  //  @Override
  //  public void configure(WebSecurity webSecurity) throws Exception {
  //    webSecurity
  //        .ignoring()
  //        .requestMatchers(HttpMethod.POST, authenticationPath)
  //        .requestMatchers(HttpMethod.OPTIONS, "/**")
  //        .and()
  //        .ignoring()
  //        .requestMatchers(HttpMethod.GET, "/" // Other Stuff You want to Ignore
  //            )
  //        .and()
  //        .ignoring()
  //        .requestMatchers("/swagger-ui***", "/swagger-resources/**", "/webjars/**",
  // "/v2/api-docs", "/actuator/**", "/index.html**"); // Should not be in Production!
  //    // image/webp,*/*
  //  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            (authz) ->
                authz
                    .requestMatchers(HttpMethod.POST, authenticationPath)
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .httpBasic(withDefaults())
        .exceptionHandling(
            handling ->
                handling.authenticationEntryPoint(jwtUnAuthorizedResponseAuthenticationEntryPoint))
        .sessionManagement(
            management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .csrf(AbstractHttpConfigurer::disable)
        .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}

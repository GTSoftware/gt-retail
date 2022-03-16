package ar.com.gtsoftware.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class JWTWebSecurityConfig extends WebSecurityConfigurerAdapter {

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

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    super.configure(auth);
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoderBean());
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf()
        .disable()
        .exceptionHandling()
        .authenticationEntryPoint(jwtUnAuthorizedResponseAuthenticationEntryPoint)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/**/*.{js,html,css,ico,svg,png}")
        .permitAll()
        .anyRequest()
        .authenticated();

    httpSecurity.addFilterBefore(
        jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

    httpSecurity
        .headers()
        .frameOptions()
        .sameOrigin() // H2 Console Needs this setting
        .cacheControl(); // disable caching
  }

  @Override
  public void configure(WebSecurity webSecurity) throws Exception {
    webSecurity
        .ignoring()
        .antMatchers(HttpMethod.POST, authenticationPath)
        .antMatchers(HttpMethod.OPTIONS, "/**")
        .and()
        .ignoring()
        .antMatchers(
            HttpMethod.GET, "/" // Other Stuff You want to Ignore
            )
        .and()
        .ignoring()
        .antMatchers(
            "/swagger-ui***",
            "/swagger-resources/**",
            "/webjars/**",
            "/v2/api-docs",
            "/actuator/**",
            // "/gtretail/***",
            "/index.html**"); // Should not be in Production!
    // image/webp,*/*
  }
}

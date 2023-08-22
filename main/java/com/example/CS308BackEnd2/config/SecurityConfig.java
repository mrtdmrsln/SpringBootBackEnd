package com.example.CS308BackEnd2.config;


import com.example.CS308BackEnd2.service.CustomUserServiceDetail;
import com.example.CS308BackEnd2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Configuration
@EnableWebSecurity

public class SecurityConfig extends WebSecurityConfigurerAdapter {

   private UserDetailsService userDetailsService;
   private AuthEntryPointJwt unauthorizedHandler;
   
*/
    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, AuthEntryPointJwt unauthorizedHandler) {
        
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().cors().configurationSource(corsConfigurationSource()).and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests((authorize) ->

                {
                    try {
                        authorize
                                //.anyRequest().permitAll()
                                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .antMatchers("/api/user/createDTO").permitAll()
                                .antMatchers("/api/auth/login").permitAll()
                                .antMatchers("/api/book/create").hasRole("ADMIN")
                                .mvcMatchers("/api/order/getAll").hasAnyRole("ADMIN", "SELLER")
                                .mvcMatchers("/api/order/*/update/**").hasRole("ADMIN")
                                .mvcMatchers("/api/order/update/**").hasRole("ADMIN")
                                .antMatchers("/api/order/**").authenticated()
                                .antMatchers("/api/wishlist/**").authenticated()
                                .antMatchers("/api/review/comment/*/update/**").hasRole("ADMIN")
                                .antMatchers("/api/category/create").hasRole("ADMIN")
                                //.antMatchers("/api/ref-request/create/**").permitAll()
                                .antMatchers("/api/ref-request/admin/**").hasAnyRole("SELLER", "ADMIN")
                                .antMatchers("/api/product/changeDiscountRates").hasRole("SELLER")
                                .antMatchers("/api/product/changePrice").hasRole("SELLER")
                                .antMatchers("/api/product/changeStock").hasRole("ADMIN")
                                .antMatchers("/api/ref-request/**").authenticated()
                                .antMatchers("/api/**").permitAll()
                                .antMatchers("/api/auth/**").permitAll()
                                .antMatchers("/api/review/comment/create/**").permitAll()
                                .anyRequest().authenticated().and()
                                .addFilterBefore(new TokenFilter(), SecurityContextPersistenceFilter.class)
                                //.addFilterBefore(getJwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                                .httpBasic();
                                //.addFilter(new JwtAuthenticationFilter(authenticationManager()));
                                //.sessionManagement().disable();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }


        );
    }

    @Bean
    public JwtAuthenticationFilter getJwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(authenticationManager());
        //filter.setFilterProcessesUrl("/login");
        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

   



    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must
        // not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type",
                "Access-Control-Allow-Origin: http://localhost:3000", "Access-Control-Allow-Headers", "Origin", "Accept",
                "X-Requested-With", "Access-Control-Request-Method", "Access-Control-Request-Headers",
                "Access-Control-Allow-Credentials","Access-Control-Expose-Headers: Set-Cookie",
                "Set-Cookie","Access-Control-Expose-Headers: Authorization"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

package com.example.fuzzycommitment.configuration;

import com.example.fuzzycommitment.auth.filter.InitialLoginFilter;
import com.example.fuzzycommitment.auth.filter.JwtFilter;
import com.example.fuzzycommitment.auth.provider.EmailAndPasswordAuthenticationProvider;
import com.example.fuzzycommitment.auth.provider.JwtAuthenticationProvider;
import com.example.fuzzycommitment.auth.provider.OtpAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class WebAuthorizationConfig extends WebSecurityConfigurerAdapter {
    private InitialLoginFilter initialLoginFilter;
    private JwtFilter jwtFilter;
    private OtpAuthenticationProvider otpAuthenticationProvider;
    private EmailAndPasswordAuthenticationProvider emailAndPasswordAuthenticationProvider;
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    public WebAuthorizationConfig(@Lazy InitialLoginFilter initialLoginFilter, JwtFilter jwtFilter, OtpAuthenticationProvider otpAuthenticationProvider,
                                  EmailAndPasswordAuthenticationProvider emailAndPasswordAuthenticationProvider,  JwtAuthenticationProvider jwtAuthenticationProvider) {
        this.initialLoginFilter = initialLoginFilter;
        this.jwtFilter = jwtFilter;
        this.otpAuthenticationProvider = otpAuthenticationProvider;
        this.emailAndPasswordAuthenticationProvider = emailAndPasswordAuthenticationProvider;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(otpAuthenticationProvider)
                .authenticationProvider(emailAndPasswordAuthenticationProvider)
                .authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAt(
                        initialLoginFilter,
                        BasicAuthenticationFilter.class)
                .addFilterAfter(
                        jwtFilter,
                        BasicAuthenticationFilter.class
                );

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.applyPermitDefaultValues();
        corsConfiguration.setAllowedOrigins(List.of("*"));
//        corsConfiguration.setAllowCredentials(true); //conflicts with *
        corsConfiguration.setExposedHeaders(List.of("Authorization"));

        http
                .authorizeRequests().mvcMatchers("/register**", "/post/**").permitAll()
                .and().cors().configurationSource(request -> corsConfiguration)
                .and().csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated();
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}

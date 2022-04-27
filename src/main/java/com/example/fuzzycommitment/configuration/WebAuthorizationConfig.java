package com.example.fuzzycommitment.configuration;

import com.example.fuzzycommitment.auth.filter.InitialLoginFilter;
import com.example.fuzzycommitment.auth.filter.JwtFilter;
import com.example.fuzzycommitment.auth.provider.OtpAuthenticationProvider;
import com.example.fuzzycommitment.auth.provider.UsernameAndPasswordAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class WebAuthorizationConfig extends WebSecurityConfigurerAdapter {
    private InitialLoginFilter initialLoginFilter;
    private JwtFilter jwtFilter;
    private UsernameAndPasswordAuthenticationProvider usernameAndPasswordAuthenticationProvider;
    private OtpAuthenticationProvider otpAuthenticationProvider;

    public WebAuthorizationConfig(@Lazy InitialLoginFilter initialLoginFilter, JwtFilter jwtFilter, UsernameAndPasswordAuthenticationProvider usernameAndPasswordAuthenticationProvider, OtpAuthenticationProvider otpAuthenticationProvider) {
        this.initialLoginFilter = initialLoginFilter;
        this.jwtFilter = jwtFilter;
        this.usernameAndPasswordAuthenticationProvider = usernameAndPasswordAuthenticationProvider;
        this.otpAuthenticationProvider = otpAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(otpAuthenticationProvider)
                .authenticationProvider(usernameAndPasswordAuthenticationProvider);
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

        http.authorizeRequests().mvcMatchers("/register**", "/post/**").permitAll()
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

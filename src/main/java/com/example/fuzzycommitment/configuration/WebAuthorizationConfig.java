package com.example.fuzzycommitment.configuration;

import com.example.fuzzycommitment.auth.filter.InitialFilter;
import com.example.fuzzycommitment.auth.filter.JwtFilter;
import com.example.fuzzycommitment.auth.provider.OtpAuthenticationProvider;
import com.example.fuzzycommitment.auth.provider.UsernameAndPasswordAuthenticationProvider;
import lombok.AllArgsConstructor;
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
    private InitialFilter initialFilter;
    private JwtFilter jwtFilter;
    private UsernameAndPasswordAuthenticationProvider usernameAndPasswordAuthenticationProvider;
    private OtpAuthenticationProvider otpAuthenticationProvider;

    public WebAuthorizationConfig(@Lazy InitialFilter initialFilter, JwtFilter jwtFilter, UsernameAndPasswordAuthenticationProvider usernameAndPasswordAuthenticationProvider, OtpAuthenticationProvider otpAuthenticationProvider) {
        this.initialFilter = initialFilter;
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
                        initialFilter,
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

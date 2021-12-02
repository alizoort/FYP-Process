package com.example.workflow.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationProviderService authenticationProvider;
    @Bean
    public BCryptPasswordEncoder bCryptEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SCryptPasswordEncoder sCryptPasswordEncoder(){
        return new SCryptPasswordEncoder();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(authenticationProvider);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.formLogin().defaultSuccessUrl("/submitted",true);
        http.authorizeRequests().mvcMatchers(HttpMethod.GET,"/a").authenticated().mvcMatchers(HttpMethod.POST,"/a")
                .authenticated().mvcMatchers(HttpMethod.POST,"/submission/**").hasAnyRole("ADMIN","STUDENT")
                .mvcMatchers(HttpMethod.PUT,"/submission/**").hasAnyRole("ADMIN","PROF")
                .mvcMatchers(HttpMethod.DELETE,"/submission/**").hasAnyRole("ADMIN","PROF").anyRequest().authenticated();
        /**http.authorizeRequests().mvcMatchers(HttpMethod.GET,"/a")
         .authenticated().mvcMatchers(HttpMethod.POST,"/a").permitAll().anyRequest().denyAll();**/
        //http.authorizeRequests().anyRequest().hasRole("MANAGER");
        http.csrf().disable();
    }
}

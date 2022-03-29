package org.fgai4h.ap;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/**").permitAll();
        //http.csrf()
        //        .and()
        //        .authorizeRequests(authz -> authz.mvcMatchers("/")
        //                .permitAll()
                        //.anyRequest()
                        //.authenticated()
                //);
                //.oauth2Login()
                //.and()
                //.logout()
                //.logoutSuccessUrl("/");

        http.headers().frameOptions().disable();
        http.cors();
        http.oauth2ResourceServer().jwt();
    }

}

package org.fgai4h.ap;

import com.nimbusds.jose.shaded.json.JSONArray;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/**").permitAll();


        http.headers().frameOptions().disable();
        http.cors();
        http.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));
    }

    private JwtAuthenticationConverter grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
                    String[] scopes;
                    if (jwt.getClaims().containsKey("cognito:groups")) {
                        scopes = ((JSONArray) jwt.getClaims().get("cognito:groups")).toArray(new String[0]);
                    } else {
                        scopes = ((String) jwt.getClaims().getOrDefault("scope", "")).split(" ");
                    }
                    return Arrays.stream(scopes)
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase(Locale.ROOT)))
                            .collect(Collectors.toSet());
                }

        );

        return jwtAuthenticationConverter;
    }

}

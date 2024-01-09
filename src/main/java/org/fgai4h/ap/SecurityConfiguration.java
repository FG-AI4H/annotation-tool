package org.fgai4h.ap;

import org.fgai4h.ap.domain.user.entity.UserEntity;
import org.fgai4h.ap.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserRepository userRepository;

    private static final String[] ALLOWED_RESOURCES = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                .requestMatchers(ALLOWED_RESOURCES).permitAll();
        http.cors();
        http.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));

        return http.build();
    }

    private JwtAuthenticationConverter grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {

                    Optional<UserEntity> user = userRepository.findByIdpId(jwt.getClaims().get("sub").toString());
                    if(!user.isPresent()){
                        UserEntity newUser = new UserEntity();
                        newUser.setIdpId(jwt.getClaims().get("sub").toString());
                        newUser.setUsername(jwt.getClaims().get("username").toString());
                        userRepository.save(newUser);
                    }

                    String[] scopes;
                    if (jwt.getClaims().containsKey("cognito:groups")) {
                        scopes = (String[]) ((ArrayList) jwt.getClaims().get("cognito:groups")).toArray(new String[0]);
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

package edu.bbte.projectbluebook.datacatalog.versioning.config.security;

import edu.bbte.projectbluebook.datacatalog.versioning.client.AssetServiceClient;
import edu.bbte.projectbluebook.datacatalog.versioning.model.dto.AssetResponse;
import edu.bbte.projectbluebook.datacatalog.versioning.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Autowired
    JwtAuthenticationManager authenticationManager;

    @Autowired
    JwtServerAuthenticationConverter authenticationConverter;

    @Autowired
    private AssetServiceClient assetServiceClient;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(authenticationConverter);

        return http
                .cors().disable()
                .csrf().disable()
                .formLogin().disable()
                .logout().disable()
                .httpBasic().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.POST, "/assets/{assetId}/versions").access(this::isOwnerOrAdmin)
                .pathMatchers(HttpMethod.DELETE, "/assets/{assetId}/versions/*").access(this::isOwnerOrAdmin)
                .pathMatchers(HttpMethod.GET, "/assets/{assetId}/versions/**").permitAll()
                .pathMatchers("/assets/*/versions").permitAll()
                .anyExchange().authenticated()
                .and()
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    private Mono<AuthorizationDecision> isOwner(Mono<Authentication> authentication, AuthorizationContext context) {
        Mono<String> assetOwnerId = assetServiceClient
                .fetchAsset(context.getVariables().get("assetId").toString())
                .switchIfEmpty(Mono.error(new NotFoundException("Asset not found.")))
                .map(AssetResponse::getOwnerId);

        Mono<String> principal = authentication.map(Authentication::getPrincipal).cast(String.class);

        return assetOwnerId.zipWith(principal)
                .map(tuple -> tuple.getT1().equals(tuple.getT2()))
                .defaultIfEmpty(false)
                .map(AuthorizationDecision::new);
    }

    private Mono<AuthorizationDecision> isAdmin(Mono<Authentication> authentication) {
        return authentication
                .map(Authentication::getAuthorities)
                .map(authorities -> authorities.stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")))
                .defaultIfEmpty(false)
                .map(AuthorizationDecision::new);
    }

    private Mono<AuthorizationDecision> isOwnerOrAdmin(Mono<Authentication> authentication,
                                                       AuthorizationContext context) {
        return isAdmin(authentication)
                .zipWith(isOwner(authentication, context))
                .map(tuple -> tuple.getT1().isGranted() || tuple.getT2().isGranted())
                .map(AuthorizationDecision::new);
    }
}

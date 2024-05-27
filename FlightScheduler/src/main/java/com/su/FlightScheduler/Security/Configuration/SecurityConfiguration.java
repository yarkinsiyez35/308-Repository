package com.su.FlightScheduler.Security.Configuration;

import com.su.FlightScheduler.Security.Utils.RSAKeyProperties;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.cors.CorsConfiguration;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

    private final RSAKeyProperties keys;

    public SecurityConfiguration(RSAKeyProperties keys){
        this.keys = keys;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService detailsService){
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(detailsService);
        daoProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoProvider);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    //request matchers for authentication controller
                    auth.requestMatchers("/auth/**").permitAll();
                    //request matchers for admin controller
                    auth.requestMatchers("/admin/**").hasRole("Admin");

                    //request matchers for pilot controller
                    auth.requestMatchers("/api/pilots/createPilot").hasRole("Admin");
                    auth.requestMatchers(HttpMethod.POST, "/api/pilots/{pilotId}").hasRole("Admin");
                    auth.requestMatchers(HttpMethod.PUT, "/api/pilots/{pilotId}").hasAnyRole("Admin", "PilotCrew");
                    auth.requestMatchers("/api/pilots/**").hasAnyRole("Admin", "PilotCrew");

                    //request matcher for attendant controller
                    auth.requestMatchers("/api/attendants/createAttendant").hasRole("Admin");
                    auth.requestMatchers(HttpMethod.POST, "/api/attendants/{attendantId}").hasRole("Admin");
                    auth.requestMatchers(HttpMethod.PUT, "/api/attendants/{attendantId}").hasAnyRole("Admin", "CabinCrew");
                    auth.requestMatchers("/api/attendants/**").hasAnyRole("Admin", "CabinCrew");

                    //request matcher for passenger controller
                    auth.requestMatchers("/api/passengers/**").hasAnyRole("Admin", "Passenger");

                    //request matcher for flight controller
                    auth.requestMatchers("/api/flights/createFlight/{adminId}").hasRole("Admin");
                    auth.requestMatchers("/api/flights/updateFlight/{adminId}").hasRole("Admin");
                    auth.requestMatchers("/api/flights/**").hasAnyRole("Admin","Passenger", "PilotCrew", "CabinCrew");  //FIX THIS

                    //request matchers for main controller
                    auth.requestMatchers("/main/pilot/{pilotId}/assignToFlight/{flightId}").hasRole("Admin");
                    auth.requestMatchers("/main/pilot/**").hasAnyRole("Admin","PilotCrew");
                    auth.requestMatchers("/main/attendant/{attendantId}/assignToFlight/{flightId}").hasRole("Admin");
                    auth.requestMatchers("/main/attendant/**").hasAnyRole("Admin","CabinCrew");
                    auth.requestMatchers("/main/flight/{flightId}/getAvailableAttendants").hasRole("Admin");
                    auth.requestMatchers("/main/flight/{flightId}/getAvailablePilots").hasRole("Admin");
                    auth.requestMatchers("/main/flight/{flightId}/getPilots").hasAnyRole("Admin","PilotCrew");
                    auth.requestMatchers("/main/flight/{flightId}/getAttendants").hasAnyRole("Admin", "CabinCrew", "PilotCrew");
                    auth.requestMatchers("/main/flight/{flightId}/getPassengers").hasAnyRole("Admin","CabinCrew","Passenger", "PilotCrew");
                    auth.requestMatchers("/main/passenger/{passengerId}/getFlights").hasAnyRole("Admin","Passenger");
                    auth.requestMatchers("/main/passenger/{passengerId}/cancelBooking/{bookingId}").hasRole("Passenger");
                    auth.requestMatchers("/main/passenger/{passengerId}/bookFlight/{flightNumber}/{isParent}").hasRole("Passenger");
                    auth.requestMatchers("/main/passenger/{passengerId}/bookFlightAuto/{flightNumber}/{isParent}/{isEconomy}").hasRole("Passenger");
                    auth.requestMatchers("test/**").permitAll();        //this will be deleted later on
                    auth.anyRequest().authenticated();
                });

        http.oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter());
        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(keys.getPublicKey()).privateKey(keys.getPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtConverter;
    }

}

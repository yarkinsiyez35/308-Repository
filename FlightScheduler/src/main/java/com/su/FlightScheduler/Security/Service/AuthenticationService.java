package com.su.FlightScheduler.Security.Service;

import com.su.FlightScheduler.Security.DTO.LoginResponseDTO;
import com.su.FlightScheduler.Security.Model.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class AuthenticationService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    /*
    public ApplicationUser registerUser(String username, String password){

        String encodedPassword = passwordEncoder.encode(password);
        //Role userRole = roleRepository.findByAuthority("USER").get();

        Set<GrantedAuthority> authorities = new HashSet<>();

        //authorities.add(userRole);

        return userRepository.save(new ApplicationUser(0, username, encodedPassword, authorities));
    }
     */

    public LoginResponseDTO loginUser(String username, String password){

        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = tokenService.generateJwt(auth);

            ApplicationUser userDetails = (ApplicationUser) auth.getPrincipal();

            return new LoginResponseDTO(userDetails, token);

        } catch(AuthenticationException e){
            return new LoginResponseDTO(null, "");
        }
    }

}

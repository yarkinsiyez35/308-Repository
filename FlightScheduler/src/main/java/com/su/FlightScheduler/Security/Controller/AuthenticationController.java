package com.su.FlightScheduler.Security.Controller;

import com.su.FlightScheduler.Security.DTO.LoginResponseDTO;
import com.su.FlightScheduler.Security.DTO.RegistrationDTO;
import com.su.FlightScheduler.Security.Service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    /*
    @PostMapping("/register")
    public ApplicationUser registerUser(@RequestBody RegistrationDTO body){
        return authenticationService.registerUser(body.getUsername(), body.getPassword());
    }

     */

    @PostMapping("/login")
    public LoginResponseDTO loginUser(@RequestBody RegistrationDTO body){
        LoginResponseDTO loginResponseDTO = authenticationService.loginUser(body.getUsername(), body.getPassword());
        return loginResponseDTO;
    }
}

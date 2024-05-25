package com.su.FlightScheduler.Security.Controller;

import com.su.FlightScheduler.Security.DTO.LoginResponseDTO;
import com.su.FlightScheduler.Security.DTO.RegistrationDTO;
import com.su.FlightScheduler.Security.Model.ApplicationUser;
import com.su.FlightScheduler.Security.Service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//TESTING: this controller should be tested
@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody RegistrationDTO body){
       try
       {
           ApplicationUser applicationUser = authenticationService.registerUser(body);
           return ResponseEntity.ok(applicationUser);
       }
       catch (RuntimeException e)
       {
           return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
       }
    }
    //    @CrossOrigin(value = "http://127.0.0.1:5500", )
    @PostMapping("/forgetPassword")
    public ResponseEntity<Object> forgetPassword(@RequestBody RegistrationDTO body)
    {
        try
        {
            ApplicationUser updatedApplicationUser = authenticationService.forgetPassword(body.getUsername(), body.getPassword());
            return ResponseEntity.ok(updatedApplicationUser);
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody RegistrationDTO body){
        try
        {
            LoginResponseDTO loginResponseDTO = authenticationService.loginUser(body.getUsername(), body.getPassword());
            return ResponseEntity.ok(loginResponseDTO);
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }
}

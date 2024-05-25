package com.su.FlightScheduler.Security.Service;

import com.su.FlightScheduler.Entity.PassengerEntity;
import com.su.FlightScheduler.Repository.AdminRepository;
import com.su.FlightScheduler.Repository.PassengerRepository;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotRepository;
import com.su.FlightScheduler.Security.DTO.LoginResponseDTO;
import com.su.FlightScheduler.Security.DTO.RegistrationDTO;
import com.su.FlightScheduler.Security.Model.ApplicationAuthority;
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

import java.util.HashSet;
import java.util.Set;

//TESTING: this controller should be tested
@Service
@Transactional
public class AuthenticationService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private PilotRepository pilotRepository;
    @Autowired
    AdminRepository adminRepository;


    public ApplicationUser registerUser(RegistrationDTO body){

        Set<ApplicationAuthority> roles = new HashSet<>();
        roles.add(new ApplicationAuthority("PASSENGER"));

        //create an application user
        ApplicationUser applicationUser = new ApplicationUser(body.getUsername(), body.getPassword(), roles);

        //does not allow a passenger to register with an existing mail
        if (passengerRepository.findPassengerEntityByEmail(applicationUser.getUsername()).isPresent())
        {
            throw new RuntimeException("Cannot create user!");
        }
        if (pilotRepository.findPilotEntityByEmail(applicationUser.getUsername()).isPresent())
        {
            throw new RuntimeException("Cannot create user!");
        }
        if (adminRepository.findAdminEntityByEmail(applicationUser.getUsername()).isPresent())
        {
            throw new RuntimeException("Cannot create user!");
        }

        //save the passenger
        PassengerEntity passengerEntity = new PassengerEntity(body.getUsername(), body.getPassword(), body.getFirstName(),body.getLastName(), body.getAge(), body.getGender(), body.getNationality());
        PassengerEntity savedPassenger = passengerRepository.save(passengerEntity);
        applicationUser.setId(savedPassenger.getPassengerId());
        //return the application user
        return applicationUser;
    }


    public LoginResponseDTO loginUser(String username, String password){

        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = tokenService.generateJwt(auth);
            ApplicationUser userDetails = (ApplicationUser) auth.getPrincipal();
            return new LoginResponseDTO(userDetails, token);
        } catch(AuthenticationException e){
            throw new RuntimeException(e.getMessage());
        }
    }

}

package com.su.FlightScheduler.Security.Service;

import java.util.Optional;

import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewEntity;
import com.su.FlightScheduler.Entity.PassengerEntity;
import com.su.FlightScheduler.Entity.PilotEntity;
import com.su.FlightScheduler.Repository.AdminRepository;
import com.su.FlightScheduler.Repository.CabinCrewRepositories.CabinCrewRepository;
import com.su.FlightScheduler.Repository.PassengerRepository;
import com.su.FlightScheduler.Repository.PilotRepositories.PilotRepository;
import com.su.FlightScheduler.Security.Model.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserService implements UserDetailsService {

    private PasswordEncoder encoder;

    private PilotRepository pilotRepository;
    private CabinCrewRepository cabinCrewRepository;
    private PassengerRepository passengerRepository;
    private AdminRepository adminRepository;

    @Autowired
    public UserService(PasswordEncoder encoder, PilotRepository pilotRepository, CabinCrewRepository cabinCrewRepository, PassengerRepository passengerRepository, AdminRepository adminRepository) {
        this.encoder = encoder;
        this.pilotRepository = pilotRepository;
        this.cabinCrewRepository = cabinCrewRepository;
        this.passengerRepository = passengerRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<PilotEntity> pilotEntity = pilotRepository.findPilotEntityByEmail(username);
        if (pilotEntity.isPresent())
        {
            ApplicationUser applicationUser = new ApplicationUser(pilotEntity.get());
            return applicationUser;
        }
        Optional<CabinCrewEntity> cabinCrewEntity = cabinCrewRepository.findCabinCrewEntityByEmail(username);
        if (cabinCrewEntity.isPresent())
        {
            ApplicationUser applicationUser = new ApplicationUser(cabinCrewEntity.get());
            return applicationUser;
        }
        Optional<PassengerEntity> passengerEntity = passengerRepository.findPassengerEntityByEmail(username);
        if (passengerEntity.isPresent())
        {
            ApplicationUser applicationUser = new ApplicationUser(passengerEntity.get());
            return applicationUser;
        }



        //edit later
        throw new RuntimeException("User not found!");
    }

}

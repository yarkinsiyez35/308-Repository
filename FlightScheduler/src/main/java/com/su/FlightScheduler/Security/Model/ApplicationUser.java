package com.su.FlightScheduler.Security.Model;

import com.su.FlightScheduler.Entity.CabinCrewEntites.CabinCrewEntity;
import com.su.FlightScheduler.Entity.PassengerEntity;
import com.su.FlightScheduler.Entity.PilotEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class ApplicationUser implements UserDetails {

    private String username;
    private String password;
    private Set<ApplicationAuthority> authorities;

    public ApplicationUser() {
        super();
        authorities = new HashSet<>();
    }

    public ApplicationUser(String username, String password, Set<ApplicationAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }


    public ApplicationUser(PilotEntity pilotEntity)
    {
        this.username = pilotEntity.getEmail();
        this.password = pilotEntity.getPassword();
        ApplicationAuthority applicationAuthority = new ApplicationAuthority("PILOT");
        this.authorities = new HashSet<>();
        this.authorities.add(applicationAuthority);
    }

    public ApplicationUser(CabinCrewEntity cabinCrewEntity)
    {
        this.username = cabinCrewEntity.getEmail();
        this.password = cabinCrewEntity.getPassword();
        ApplicationAuthority applicationAuthority = new ApplicationAuthority("ATTENDANT");
        this.authorities = new HashSet<>();
        this.authorities.add(applicationAuthority);
    }

    public ApplicationUser(PassengerEntity passengerEntity)
    {
        this.username = passengerEntity.getEmail();
        this.password = passengerEntity.getPassword();
        ApplicationAuthority applicationAuthority = new ApplicationAuthority("PASSENGER");
        this.authorities = new HashSet<>();
        this.authorities.add(applicationAuthority);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return this.authorities;
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /* If you want account locking capabilities create variables and ways to set them for the methods below */
    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

}

package com.su.FlightScheduler.Security.Model;

import com.su.FlightScheduler.Entity.AdminEntity;
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
    private int id;
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

    public ApplicationUser(String username, String password, int id, Set<ApplicationAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.id = id;
        this.authorities = authorities;
    }
    //add id here to constructor
    public ApplicationUser(PilotEntity pilotEntity)
    {
        this.username = pilotEntity.getEmail();
        this.password = pilotEntity.getPassword();
        this.id = pilotEntity.getPilotId();
        ApplicationAuthority applicationAuthority = new ApplicationAuthority("Pilot");
        this.authorities = new HashSet<>();
        this.authorities.add(applicationAuthority);
    }

    public ApplicationUser(CabinCrewEntity cabinCrewEntity)
    {
        this.username = cabinCrewEntity.getEmail();
        this.password = cabinCrewEntity.getPassword();
        this.id = cabinCrewEntity.getAttendantId();
        ApplicationAuthority applicationAuthority = new ApplicationAuthority("CabinCrew");
        this.authorities = new HashSet<>();
        this.authorities.add(applicationAuthority);
    }

    public ApplicationUser(PassengerEntity passengerEntity)
    {
        this.username = passengerEntity.getEmail();
        this.password = passengerEntity.getPassword();
        this.id = passengerEntity.getPassengerId();
        ApplicationAuthority applicationAuthority = new ApplicationAuthority("Passenger");
        this.authorities = new HashSet<>();
        this.authorities.add(applicationAuthority);
    }

    public ApplicationUser(AdminEntity adminEntity)
    {
        this.username = adminEntity.getEmail();
        this.password = adminEntity.getPassword();
        this.id = adminEntity.getAdminId();
        ApplicationAuthority applicationAuthority = new ApplicationAuthority("Admin");
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

    public int getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(int id) {
        this.id = id;
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

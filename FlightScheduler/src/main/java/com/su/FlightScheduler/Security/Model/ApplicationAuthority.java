package com.su.FlightScheduler.Security.Model;

import org.springframework.security.core.GrantedAuthority;

public class ApplicationAuthority implements GrantedAuthority {

    String authority;

    public ApplicationAuthority() {

    }

    public ApplicationAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}

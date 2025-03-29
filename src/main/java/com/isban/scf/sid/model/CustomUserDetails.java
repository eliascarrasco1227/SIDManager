package com.isban.scf.sid.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	private final ClientUser clientUser;

    public CustomUserDetails(ClientUser clientUser) {
        this.clientUser = clientUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Aquí se puede configurar el rol, en este caso solo USER
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return clientUser.getPassword();
    }

    @Override
    public String getUsername() {
        return clientUser.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return clientUser.getStatus() == 1;
    }
}


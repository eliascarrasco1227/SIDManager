package com.isban.scf.sid.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.isban.scf.sid.model.ClientUser;
import com.isban.scf.sid.model.CustomUserDetails;
import com.isban.scf.sid.repository.ClientUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final ClientUserRepository clientUserRepository;
	
    public CustomUserDetailsService(ClientUserRepository userRepository) {
        this.clientUserRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ClientUser user = clientUserRepository.findByEmail(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        
        return customUserDetails;
    }
}

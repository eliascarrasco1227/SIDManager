package com.isban.scf.sid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isban.scf.sid.model.ClientUser;

@Repository
public interface ClientUserRepository extends JpaRepository<ClientUser, String> {
	
	ClientUser findByEmailAndConfirmationCodeAndStatus(String email, String confirmationCode, int status);
	
	ClientUser findByEmail(String email);
	
	ClientUser findByEmailAndStatus(String email, int status);

}

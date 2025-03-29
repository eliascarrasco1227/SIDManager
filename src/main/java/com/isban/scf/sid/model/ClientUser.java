package com.isban.scf.sid.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CLIENT_USER") // Nombre de la tabla en la base de datos
public class ClientUser {
	
    @Id
    @Column(name = "EMAIL")
    private String email;
    
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "CONFIRMATION_CODE")
    private String confirmationCode;
    
    @Column(name = "STATUS")
    private int status;
    
    @Column(name = "CREATION_DATE")
    private Date creationDate;
    
    @Column(name = "CONFIRMATION_DATE")
    private Date confirmationDate;
    
    

    // Constructor sin argumentos
    public ClientUser() {}

    // Getters y setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getConfirmationCode() {
        return confirmationCode;
    }

	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}

    public int getStatus() {
        return status;
    }
	
	public void setStatus(int status) {
		this.status = status;
	}
	
    public Date getConfirmationDate() {
        return confirmationDate;
    }
	
	public void setConfirmationDate(Date confirmationDate) {
		this.confirmationDate = confirmationDate;
	}

    public Date getCreationDate() {
        return creationDate;
    }
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
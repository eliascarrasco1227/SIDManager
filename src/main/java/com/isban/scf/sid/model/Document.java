package com.isban.scf.sid.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "documents") // Nombre de la tabla en la base de datos
public class Document {

    @Id
    @Column(name = "CLIENT_DOC_ID")
    private Long clientDocId; // esta es la primary key

    @Column(name = "CLIENT_APP_ID")
    private Long clientAppId; // es un int

    @Column(name = "PROVIDER_ID")
    private String providerId; 

    @Column(name = "SENT_AT")
    private String sentAt;

    @Column(name = "RECEIVED_AT")
    private String receivedAt;

    // Getters y setters
    public Long getClientDocId() {
        return clientDocId;
    }

    public void setClientDocId(Long clientDocId) {
        this.clientDocId = clientDocId;
    }

    public Long getClientAppId() {
        return clientAppId;
    }

    public void setClientAppId(Long clientAppId) {
        this.clientAppId = clientAppId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public String getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(String receivedAt) {
        this.receivedAt = receivedAt;
    }
}

    

    


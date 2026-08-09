package com.jonwilla.disasterresponse.responder;

import com.jonwilla.disasterresponse.user.UserAccount;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "responders")
public class Responder {

    @Id
    @UuidGenerator
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private UserAccount user;

    @Column(
            name = "specialization",
            nullable = false,
            length = 120
    )
    private String specialization;

    @Column(
            name = "phone_number",
            length = 40
    )
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private ResponderStatus status;

    protected Responder() {
    }

    public Responder(
            UserAccount user,
            String specialization,
            String phoneNumber,
            ResponderStatus status
    ) {
        this.user = user;
        this.specialization = specialization;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(
            UserAccount user
    ) {
        this.user = user;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(
            String specialization
    ) {
        this.specialization =
                specialization;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(
            String phoneNumber
    ) {
        this.phoneNumber =
                phoneNumber;
    }

    public ResponderStatus getStatus() {
        return status;
    }

    public void setStatus(
            ResponderStatus status
    ) {
        this.status = status;
    }
}
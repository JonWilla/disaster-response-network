package com.jonwilla.disasterresponse.responder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ResponderRepository
        extends JpaRepository<Responder, UUID> {

    List<Responder> findByStatus(
            ResponderStatus status
    );
}
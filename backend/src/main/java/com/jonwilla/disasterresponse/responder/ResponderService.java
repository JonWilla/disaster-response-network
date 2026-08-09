package com.jonwilla.disasterresponse.responder;

import com.jonwilla.disasterresponse.exception.DomainNotFoundException;
import com.jonwilla.disasterresponse.responder.dto.CreateResponderRequest;
import com.jonwilla.disasterresponse.responder.dto.ResponderResponse;
import com.jonwilla.disasterresponse.user.UserAccount;
import com.jonwilla.disasterresponse.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ResponderService {

    private final ResponderRepository responderRepository;
    private final UserRepository userRepository;

    public ResponderService(
            ResponderRepository responderRepository,
            UserRepository userRepository
    ) {
        this.responderRepository = responderRepository;
        this.userRepository = userRepository;
    }

    public ResponderResponse create(
            CreateResponderRequest request
    ) {
        UserAccount user = userRepository
                .findById(request.userId())
                .orElseThrow(() ->
                        new DomainNotFoundException(
                                "User not found with id: "
                                        + request.userId()
                        )
                );

        Responder responder = new Responder(
                user,
                request.specialization(),
                request.phoneNumber(),
                ResponderStatus.AVAILABLE
        );

        return toResponse(
                responderRepository.save(responder)
        );
    }

    @Transactional(readOnly = true)
    public List<ResponderResponse> findAll(
            ResponderStatus status
    ) {
        List<Responder> responders =
                status == null
                        ? responderRepository.findAll()
                        : responderRepository.findByStatus(status);

        return responders.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ResponderResponse findById(UUID id) {
        return toResponse(findResponder(id));
    }

    private Responder findResponder(UUID id) {
        return responderRepository
                .findById(id)
                .orElseThrow(() ->
                        new DomainNotFoundException(
                                "Responder not found with id: " + id
                        )
                );
    }

    private ResponderResponse toResponse(
            Responder responder
    ) {
        UserAccount user = responder.getUser();

        return new ResponderResponse(
                responder.getId(),
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                responder.getSpecialization(),
                responder.getPhoneNumber(),
                responder.getStatus()
        );
    }
}
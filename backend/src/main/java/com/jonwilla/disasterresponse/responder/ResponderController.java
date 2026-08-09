package com.jonwilla.disasterresponse.responder;

import com.jonwilla.disasterresponse.responder.dto.CreateResponderRequest;
import com.jonwilla.disasterresponse.responder.dto.ResponderResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/responders")
public class ResponderController {

    private final ResponderService responderService;

    public ResponderController(
            ResponderService responderService
    ) {
        this.responderService = responderService;
    }

    @PostMapping
    public ResponseEntity<ResponderResponse> create(
            @Valid
            @RequestBody
            CreateResponderRequest request
    ) {
        ResponderResponse response =
                responderService.create(request);

        return ResponseEntity
                .created(
                        URI.create(
                                "/api/responders/"
                                        + response.id()
                        )
                )
                .body(response);
    }

    @GetMapping
    public List<ResponderResponse> findAll(
            @RequestParam(required = false)
            ResponderStatus status
    ) {
        return responderService.findAll(status);
    }

    @GetMapping("/{id}")
    public ResponderResponse findById(
            @PathVariable UUID id
    ) {
        return responderService.findById(id);
    }
}
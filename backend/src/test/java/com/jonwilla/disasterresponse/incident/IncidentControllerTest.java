package com.jonwilla.disasterresponse.incident;

import com.jonwilla.disasterresponse.exception.GlobalExceptionHandler;
import com.jonwilla.disasterresponse.exception.IncidentNotFoundException;
import com.jonwilla.disasterresponse.incident.dto.CreateIncidentRequest;
import com.jonwilla.disasterresponse.incident.dto.IncidentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class IncidentControllerTest {

    @Mock
    private IncidentService incidentService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        IncidentController controller =
                new IncidentController(incidentService);

        LocalValidatorFactoryBean validator =
                new LocalValidatorFactoryBean();

        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(
                        new GlobalExceptionHandler()
                )
                .setValidator(validator)
                .build();
    }

    @Test
    void createShouldReturn201() throws Exception {
        UUID id = UUID.randomUUID();

        OffsetDateTime now =
                OffsetDateTime.now(ZoneOffset.UTC);

        IncidentResponse response =
                new IncidentResponse(
                        id,
                        "Major Flood",
                        "Emergency response required",
                        IncidentSeverity.CRITICAL,
                        IncidentStatus.REPORTED,
                        "Laurel, MD",
                        now,
                        now
                );

        when(
                incidentService.create(
                        any(CreateIncidentRequest.class)
                )
        ).thenReturn(response);

        String json = """
                {
                  "title": "Major Flood",
                  "description": "Emergency response required",
                  "severity": "CRITICAL",
                  "location": "Laurel, MD"
                }
                """;

        mockMvc.perform(
                        post("/api/incidents")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(json)
                )
                .andExpect(
                        status().isCreated()
                )
                .andExpect(
                        jsonPath("$.title")
                                .value("Major Flood")
                )
                .andExpect(
                        jsonPath("$.description")
                                .value(
                                        "Emergency response required"
                                )
                )
                .andExpect(
                        jsonPath("$.severity")
                                .value("CRITICAL")
                )
                .andExpect(
                        jsonPath("$.status")
                                .value("REPORTED")
                )
                .andExpect(
                        jsonPath("$.location")
                                .value("Laurel, MD")
                );
    }

    @Test
    void createShouldReturn400WhenRequestIsInvalid()
            throws Exception {

        String json = """
                {
                  "title": "",
                  "description": "",
                  "severity": "HIGH",
                  "location": ""
                }
                """;

        mockMvc.perform(
                        post("/api/incidents")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(json)
                )
                .andExpect(
                        status().isBadRequest()
                )
                .andExpect(
                        jsonPath("$.error")
                                .value(
                                        "Validation Failed"
                                )
                )
                .andExpect(
                        jsonPath("$.fieldErrors.title")
                                .value(
                                        "Title is required"
                                )
                )
                .andExpect(
                        jsonPath(
                                "$.fieldErrors.description"
                        )
                                .value(
                                        "Description is required"
                                )
                )
                .andExpect(
                        jsonPath("$.fieldErrors.location")
                                .value(
                                        "Location is required"
                                )
                );
    }

    @Test
    void findByIdShouldReturn404WhenIncidentDoesNotExist()
            throws Exception {

        UUID id =
                UUID.fromString(
                        "00000000-0000-0000-0000-000000000001"
                );

        when(
                incidentService.findById(id)
        ).thenThrow(
                new IncidentNotFoundException(id)
        );

        mockMvc.perform(
                        get(
                                "/api/incidents/{id}",
                                id
                        )
                )
                .andExpect(
                        status().isNotFound()
                )
                .andExpect(
                        jsonPath("$.error")
                                .value("Not Found")
                )
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Incident not found with id: "
                                                + id
                                )
                );
    }
}
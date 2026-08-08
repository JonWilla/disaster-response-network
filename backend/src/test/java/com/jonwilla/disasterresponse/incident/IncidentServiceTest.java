package com.jonwilla.disasterresponse.incident;

import com.jonwilla.disasterresponse.exception.IncidentNotFoundException;
import com.jonwilla.disasterresponse.incident.dto.CreateIncidentRequest;
import com.jonwilla.disasterresponse.incident.dto.IncidentResponse;
import com.jonwilla.disasterresponse.incident.dto.UpdateIncidentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {

    @Mock
    private IncidentRepository incidentRepository;

    private IncidentService incidentService;

    @BeforeEach
    void setUp() {
        incidentService = new IncidentService(incidentRepository);
    }

    @Test
    void createShouldSaveIncidentWithReportedStatus() {
        CreateIncidentRequest request =
                new CreateIncidentRequest(
                        "Major Flood",
                        "Roadway is severely flooded",
                        IncidentSeverity.CRITICAL,
                        "Laurel, MD"
                );

        when(incidentRepository.save(any(Incident.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        IncidentResponse response =
                incidentService.create(request);

        assertEquals("Major Flood", response.title());
        assertEquals(
                "Roadway is severely flooded",
                response.description()
        );
        assertEquals(
                IncidentSeverity.CRITICAL,
                response.severity()
        );
        assertEquals(
                IncidentStatus.REPORTED,
                response.status()
        );
        assertEquals("Laurel, MD", response.location());

        verify(incidentRepository)
                .save(any(Incident.class));
    }

    @Test
    void findByIdShouldReturnIncidentWhenFound() {
        UUID id = UUID.randomUUID();

        Incident incident = new Incident(
                "Wildfire",
                "Wildfire reported near residential area",
                IncidentSeverity.CRITICAL,
                IncidentStatus.REPORTED,
                "Baltimore, MD"
        );

        when(incidentRepository.findById(id))
                .thenReturn(Optional.of(incident));

        IncidentResponse response =
                incidentService.findById(id);

        assertEquals("Wildfire", response.title());
        assertEquals(
                IncidentSeverity.CRITICAL,
                response.severity()
        );
        assertEquals(
                IncidentStatus.REPORTED,
                response.status()
        );
    }

    @Test
    void findByIdShouldThrowWhenIncidentDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(incidentRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                IncidentNotFoundException.class,
                () -> incidentService.findById(id)
        );
    }

    @Test
    void updateShouldModifyIncidentAndFlushChanges() {
        UUID id = UUID.randomUUID();

        Incident incident = new Incident(
                "Flood",
                "Flood initially reported",
                IncidentSeverity.CRITICAL,
                IncidentStatus.REPORTED,
                "Laurel, MD"
        );

        UpdateIncidentRequest request =
                new UpdateIncidentRequest(
                        "Flood Response",
                        "Emergency crews have arrived",
                        IncidentSeverity.HIGH,
                        IncidentStatus.IN_PROGRESS,
                        "Laurel, MD"
                );

        when(incidentRepository.findById(id))
                .thenReturn(Optional.of(incident));

        when(
                incidentRepository.saveAndFlush(
                        any(Incident.class)
                )
        ).thenAnswer(
                invocation -> invocation.getArgument(0)
        );

        IncidentResponse response =
                incidentService.update(id, request);

        assertEquals(
                "Flood Response",
                response.title()
        );

        assertEquals(
                "Emergency crews have arrived",
                response.description()
        );

        assertEquals(
                IncidentSeverity.HIGH,
                response.severity()
        );

        assertEquals(
                IncidentStatus.IN_PROGRESS,
                response.status()
        );

        verify(incidentRepository)
                .saveAndFlush(incident);
    }

    @Test
    void deleteShouldDeleteExistingIncident() {
        UUID id = UUID.randomUUID();

        Incident incident = new Incident(
                "Road Closure",
                "Major road closed",
                IncidentSeverity.HIGH,
                IncidentStatus.REPORTED,
                "Laurel, MD"
        );

        when(incidentRepository.findById(id))
                .thenReturn(Optional.of(incident));

        incidentService.delete(id);

        verify(incidentRepository)
                .delete(incident);
    }

    @Test
    void findAllShouldFilterByStatus() {
        Incident incident = new Incident(
                "Flood",
                "Flood response underway",
                IncidentSeverity.HIGH,
                IncidentStatus.IN_PROGRESS,
                "Laurel, MD"
        );

        when(
                incidentRepository
                        .findByStatusOrderByReportedAtDesc(
                                IncidentStatus.IN_PROGRESS
                        )
        ).thenReturn(List.of(incident));

        List<IncidentResponse> responses =
                incidentService.findAll(
                        IncidentStatus.IN_PROGRESS,
                        null
                );

        assertEquals(1, responses.size());

        assertEquals(
                IncidentStatus.IN_PROGRESS,
                responses.get(0).status()
        );
    }
}
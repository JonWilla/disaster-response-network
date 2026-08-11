package com.jonwilla.disasterresponse.incident.dto;

import java.io.Serializable;

public record IncidentSummaryResponse(
        long totalIncidents,
        long reported,
        long inProgress,
        long resolved,
        long critical
) implements Serializable {
}
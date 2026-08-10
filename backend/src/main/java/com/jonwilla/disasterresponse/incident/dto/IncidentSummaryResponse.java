package com.jonwilla.disasterresponse.incident.dto;

public record IncidentSummaryResponse(
        long totalIncidents,
        long reported,
        long inProgress,
        long resolved,
        long critical
) {
}
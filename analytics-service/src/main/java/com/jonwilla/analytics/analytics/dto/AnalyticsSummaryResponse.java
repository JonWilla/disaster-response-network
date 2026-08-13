package com.jonwilla.analytics.analytics.dto;

public record AnalyticsSummaryResponse(
        long totalIncidents,
        long criticalIncidents,
        long highIncidents,
        long mediumIncidents,
        long lowIncidents
) {
}
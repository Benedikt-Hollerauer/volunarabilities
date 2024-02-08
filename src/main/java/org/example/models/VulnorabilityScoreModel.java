package org.example.models;

public record VulnorabilityScoreModel(
    String cveId,
    Double cvssScore
) { }
package org.example.models;

import java.util.List;

public record CatalogModel(
    String title,
    String catalogVersion,
    String dateReleased,
    int count,
    List<VulnerabilityModel> vulnerabilities
) { }
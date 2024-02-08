package org.example.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.algebra.Result;
import org.example.models.CatalogModel;

import java.net.URL;

public class JsonApiService {
    public static Result<CatalogModel, String> parseJsonToCatalog(String jsonUrl) {
        try {
            CatalogModel catalogModel = (new ObjectMapper()).readValue(
                new URL(jsonUrl),
                CatalogModel.class
            );
            return Result.ok(
                catalogModel
            );
        } catch (Exception e) {
            return Result.err(
                "There was an error while fetching and converting the data. Exception message: " + e.getMessage()
            );
        }
    }
}
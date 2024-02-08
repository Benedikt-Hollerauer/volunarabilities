package org.example;

import com.hubspot.algebra.Result;
import org.example.models.VulnorabilityScoreModel;
import org.example.services.CreateReportFileService;
import org.example.services.JsonApiService;
import org.example.services.ScoreVulnerabilityService;

import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        try {
            Result mayCorrectProcessing = JsonApiService.parseJsonToCatalog(
                "https://www.cisa.gov/sites/default/files/feeds/known_exploited_vulnerabilities.json"
            ).mapOk(catalogModel -> {
                List<Result<VulnorabilityScoreModel, String>> res = catalogModel.vulnerabilities()
                    .stream()
                    .map(vulnerabilityModel ->
                        ScoreVulnerabilityService.getCVSSScore(vulnerabilityModel.cveID())
                    ).toList();
                Optional error = res
                    .stream()
                    .filter(Result::isErr)
                    .findFirst();
                if (error.isPresent()) {
                    return error;
                } else {
                    CreateReportFileService.createReportFile(
                        "./report.html",
                        res.stream()
                            .map(it -> it.unwrapOrElseThrow())
                            .toList()
                    );
                    return true;
                }
            });
            if(mayCorrectProcessing.isOk()) {
                System.out.println("correct processing");
            } else {
                System.out.println("Error: " + mayCorrectProcessing);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
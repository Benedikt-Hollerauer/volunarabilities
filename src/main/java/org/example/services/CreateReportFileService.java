package org.example.services;

import com.hubspot.algebra.Result;
import org.example.models.VulnorabilityScoreModel;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CreateReportFileService {
    public static Result<Boolean, String> createReportFile(String desiredFilePath, List<VulnorabilityScoreModel> vulnorabilityScoreModels) {
        try {
            (new File(desiredFilePath))
                .createNewFile();
            FileWriter writer = new FileWriter(desiredFilePath);
            writer.write(
                getHtmlTable(
                    sortByScore(vulnorabilityScoreModels)
                )
            );
            writer.close();
            return Result.ok(true);
        } catch (Exception e) {
            return Result.err(
                "There was an error while trying to create the report file. Exception message: " + e.getMessage()
            );
        }
    }

    private static List<VulnorabilityScoreModel> sortByScore(List<VulnorabilityScoreModel> vulnorabilityScoreModels) {
        Collections.sort(vulnorabilityScoreModels, Comparator.comparingDouble(VulnorabilityScoreModel::cvssScore));
        return vulnorabilityScoreModels;
    }

    private static String getHtmlTable(List<VulnorabilityScoreModel> vulnorabilityScoreModels) {
        StringBuilder htmlTable = new StringBuilder();
        htmlTable.append("<table border='1'>");
        htmlTable.append("<tr><th>CVE ID</th><th>CVSS Score</th></tr>");
        for (VulnorabilityScoreModel model : vulnorabilityScoreModels) {
            htmlTable.append("<tr>")
                .append("<td>").append(model.cveId()).append("</td>")
                .append("<td>").append(model.cvssScore()).append("</td>")
                .append("</tr>");
        }
        htmlTable.append("</table>");
        return htmlTable.toString();
    }
}
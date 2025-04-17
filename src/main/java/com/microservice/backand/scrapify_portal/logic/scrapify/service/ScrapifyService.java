package com.microservice.backand.scrapify_portal.logic.scrapify.service;

import com.microservice.backand.scrapify_portal.logic.scrapify.entity.ScrapifyData;
import com.microservice.backand.scrapify_portal.logic.scrapify.entity.ScrappingModel;
import com.microservice.backand.scrapify_portal.logic.scrapify.repository.ScrapifyRepository;
import com.microservice.backand.scrapify_portal.modelResponse.scrapify.ScrapifyContentListResponse;
import com.microservice.backand.scrapify_portal.modelResponse.scrapify.ScrapifyDataMongoResponse;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScrapifyService {

    @Autowired
    private ScrapifyRepository scrapifyRepository;

    public ResponseEntity<Object> getContentListOrCSV(Long categoryId, ScrappingModel model, Boolean exportable, Integer page, Integer size) {
        ScrapifyDataMongoResponse mongoResponse = scrapifyRepository.getContentWithPagination(categoryId, model, page, size);
        if (mongoResponse.getData().isEmpty())
            return ResponseEntity.ok(new ScrapifyContentListResponse(
                    false,
                    "Content is Empty"
            ));

        if (exportable) {
            ScrapifyDataMongoResponse response = scrapifyRepository.getContentWithPagination(categoryId, model, null, null);
            return generateCsv(response.getData());
        } else {
            return ResponseEntity.ok(new ScrapifyContentListResponse(
                    true,
                    "Content List Retrieved Successfully",
                    mongoResponse.getTotalCount(),
                    mongoResponse.getData()
            ));
        }

    }

    public ResponseEntity<Object> generateCsv(List<ScrapifyData> scrapifyData) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
             CSVWriter csvWriter = new CSVWriter(writer)) {

            // Dynamically extract headers from the first valid jsonData map
            Set<String> headerSet = extractHeaders(scrapifyData);
            String[] headers = headerSet.toArray(new String[0]);
            csvWriter.writeNext(headers);

            // Write rows
            for (ScrapifyData data : scrapifyData) {
                if (data.getJsonData() != null) {
                    csvWriter.writeNext(extractRow(data.getJsonData(), headers));
                }
            }

            csvWriter.flush();
            byte[] csvBytes = outputStream.toByteArray();

            HttpHeaders httpHeaders = new HttpHeaders();
            String fileName = "scrapify_data_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, "text/csv");
            httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

            return ResponseEntity.ok().headers(httpHeaders).body(csvBytes);

        } catch (Exception e) {
            throw new RuntimeException("Error generating CSV: " + e.getMessage(), e);
        }
    }

    private Set<String> extractHeaders(List<ScrapifyData> scrapifyData) {
        for (ScrapifyData data : scrapifyData) {
            if (data.getJsonData() != null && !data.getJsonData().isEmpty()) {
                return new LinkedHashSet<>(data.getJsonData().keySet());
            }
        }
        return new LinkedHashSet<>();
    }

    private String[] extractRow(HashMap<String, Object> jsonData, String[] headers) {
        List<String> values = new ArrayList<>();
        for (String header : headers) {
            Object value = jsonData.getOrDefault(header, "");
            if (value instanceof List<?> list) {
                values.add(list.stream().map(Object::toString).collect(Collectors.joining(",")));
            } else if (value instanceof Object[] array) {
                values.add(Arrays.stream(array).map(Object::toString).collect(Collectors.joining(",")));
            } else {
                values.add(value != null ? value.toString() : "");
            }
        }
        return values.toArray(new String[0]);
    }
}
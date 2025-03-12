package com.microservice.backand.scrapify_portal.logic.scrapify.service;

import com.microservice.backand.scrapify_portal.logic.scrapify.entity.ScrapifyData;
import com.microservice.backand.scrapify_portal.logic.scrapify.repository.ScrapifyRepository;
import com.microservice.backand.scrapify_portal.modelRequest.ChatGPTResponseDTO;
import com.microservice.backand.scrapify_portal.modelResponse.scrapify.ScrapifyContentListResponse;
import com.microservice.backand.scrapify_portal.modelResponse.scrapify.ScrapifyDataMongoResponse;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScrapifyService {

    @Autowired
    private ScrapifyRepository scrapifyRepository;

    public ResponseEntity<Object> getContentListOrCSV(Long categoryId, Boolean exportable, Integer page, Integer size) {
        ScrapifyDataMongoResponse mongoResponse = scrapifyRepository.getContentWithPagination(categoryId, page, size);
        if (mongoResponse.getData().isEmpty())
            return ResponseEntity.ok(new ScrapifyContentListResponse(
                    false,
                    "Content is Empty"
            ));

        if (exportable) {
            return generateCsv(mongoResponse.getData());
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

            // Get CSV headers dynamically form ChatGPTResponseDTO
            String[] headers = Arrays.stream(ChatGPTResponseDTO.class.getDeclaredFields())
                    .map(Field::getName)
                    .toArray(String[]::new);
            csvWriter.writeNext(headers);

            // Write CSV rows (from jsonData)
            for (ScrapifyData data : scrapifyData) {
                ChatGPTResponseDTO jsonData = data.getJsonData();
                if (jsonData != null) {
                    csvWriter.writeNext(extractFieldValues(jsonData));
                }
            }

            csvWriter.flush();
            byte[] csvBytes = outputStream.toByteArray();

            // Set headers
            HttpHeaders httpHeaders = new HttpHeaders();
            String fileName = "scrapify_data_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, "text/csv");
            httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .body(csvBytes);

        } catch (Exception e) {
            throw new RuntimeException("Error generating CSV: " + e.getMessage(), e);
        }
    }

    private String[] extractFieldValues(ChatGPTResponseDTO jsonData) {
        Field[] fields = jsonData.getClass().getDeclaredFields();
        List<String> values = new ArrayList<>();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(jsonData);

                // Handle null values gracefully
                if (value == null) {
                    values.add("");
                    continue;
                }

                // Handle list/array fields dynamically
                if (value instanceof List<?> list) {
                    values.add(list.stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(",")));
                } else if (value.getClass().isArray()) {
                    values.add(Arrays.stream((Object[]) value)
                            .map(Object::toString)
                            .collect(Collectors.joining(",")));
                } else {
                    values.add(value.toString());
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error extracting field values: " + e.getMessage(), e);
        }
        return values.toArray(new String[0]);
    }
}
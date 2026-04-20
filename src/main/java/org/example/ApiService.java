package org.example;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ApiService {

    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);
    private final RestTemplate restTemplate;
    private WebhookResponse webhookResponse;

    public ApiService() {
        this.restTemplate = new RestTemplate();
    }

    public void executeFlow() {
        try {
            // Step 1: Generate webhook
            logger.info("Step 1: Generating webhook...");
            generateWebhook();

            // Step 2: If webhook received, submit the SQL query
            if (webhookResponse != null && webhookResponse.getWebhook() != null) {
                logger.info("Step 2: Submitting SQL query to webhook...");
                submitQueryToWebhook();
            }

        } catch (Exception e) {
            logger.error("Error executing flow: {}", e.getMessage(), e);
        }
    }

    private void generateWebhook() {
        try {
            // Prepare the request body
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("name", "Shlok Bajaj");
            requestBody.put("regNo", "ADT23SOCB1608");
            requestBody.put("email", "shlokpbajaj000@gmail.com");

            // Make the POST request
            String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
            WebhookResponse response = restTemplate.postForObject(url, requestBody, WebhookResponse.class);

            // Store the response
            this.webhookResponse = response;

            // Log the response
            if (response != null) {
                logger.info("Webhook URL: {}", response.getWebhook());
                logger.info("Access Token received");
            }

        } catch (Exception e) {
            logger.error("Error generating webhook: {}", e.getMessage(), e);
        }
    }

    private void submitQueryToWebhook() {
        try {
            // Generate the SQL query
            String sqlQuery = generateSQLQuery();
            logger.info("Generated SQL Query: {}", sqlQuery);

            // Prepare the request
            FinalQueryRequest queryRequest = new FinalQueryRequest();
            queryRequest.setQuery(sqlQuery);

            // Set up headers with JWT token
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + webhookResponse.getAccessToken());

            HttpEntity<FinalQueryRequest> entity = new HttpEntity<>(queryRequest, headers);

            // Submit to webhook
            String webhookUrl = webhookResponse.getWebhook();
            String response = restTemplate.postForObject(webhookUrl, entity, String.class);

            logger.info("Webhook response: {}", response);

        } catch (Exception e) {
            logger.error("Error submitting query to webhook: {}", e.getMessage(), e);
        }
    }

    private String generateSQLQuery() {
        // SQL query to count younger employees in the same department
        return "SELECT " +
                "e.EMP_ID, " +
                "e.FIRST_NAME, " +
                "e.LAST_NAME, " +
                "d.DEPT_NAME AS DEPARTMENT_NAME, " +
                "COUNT(CASE WHEN e2.AGE < e.AGE THEN 1 END) AS YOUNGER_EMPLOYEES_COUNT " +
                "FROM EMPLOYEE e " +
                "INNER JOIN DEPARTMENT d ON e.DEPT_ID = d.DEPT_ID " +
                "LEFT JOIN EMPLOYEE e2 ON e.DEPT_ID = e2.DEPT_ID " +
                "GROUP BY e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, d.DEPT_NAME, e.AGE " +
                "ORDER BY e.EMP_ID DESC";
    }

    public WebhookResponse getWebhookResponse() {
        return webhookResponse;
    }
}
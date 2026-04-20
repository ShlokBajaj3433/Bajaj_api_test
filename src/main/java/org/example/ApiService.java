package org.example;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    public void executeFlow() {

        RestTemplate restTemplate = new RestTemplate();

        try {String generateUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

            WebhookRequest request = new WebhookRequest();
                request.setName("John Doe");
            request.setRegNo("REG12347");
             request.setEmail("john@example.com");

            WebhookResponse webhookResponse = restTemplate.postForObject(generateUrl, request, WebhookResponse.class);

            if (webhookResponse == null)
            {
                System.out.println("No response received");
                return;
            }

            String sqlQuery =
                    "SELECT p.AMOUNT AS SALARY, " +
                            "CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, " +
                            "TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, " +
                            "d.DEPARTMENT_NAME " +
                            "FROM PAYMENTS p " +
                            "JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
                            "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                            "WHERE DAY(p.PAYMENT_TIME) <> 1 " +
                            "AND p.AMOUNT = ( " +
                            "SELECT MAX(AMOUNT) " +
                            "FROM PAYMENTS " +
                            "WHERE DAY(PAYMENT_TIME) <> 1" +
                            ")";

            FinalQueryRequest finalQueryRequest = new FinalQueryRequest();

            finalQueryRequest.setFinalQuery(sqlQuery);

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            headers.set("Authorization", webhookResponse.getAccessToken());

            HttpEntity<FinalQueryRequest> entity = new HttpEntity<>(finalQueryRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange(webhookResponse.getWebhook(), HttpMethod.POST, entity, String.class);

            System.out.println("Status Code : " + response.getStatusCode());
            System.out.println("Response : " + response.getBody());

        } catch (Exception e) {
            System.out.println("Error while calling API");
            e.printStackTrace();
        }
    }
}
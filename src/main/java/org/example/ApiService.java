package org.example;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    public void executeFlow()
    {
        RestTemplate restTemplate = new RestTemplate();

        try {
            String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

            WebhookRequest request = new WebhookRequest();
            request.setName("Shlok Bajaj");
            request.setRegNo("ADT23SOCB1608");
            request.setEmail("shlokpbajaj000@gmail.com");

            WebhookResponse response = restTemplate.postForObject(url, request, WebhookResponse.class);

            if (response == null) {
                System.out.println("No response received");
                return;
            }

            String sqlQuery = "SELECT " +
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

            FinalQueryRequest finalQueryRequest = new FinalQueryRequest();
            finalQueryRequest.setQuery(sqlQuery);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", response.getAccessToken());

            HttpEntity<FinalQueryRequest> entity = new HttpEntity<>(finalQueryRequest, headers);

            ResponseEntity<String> response2 = restTemplate.exchange(response.getWebhook(), HttpMethod.POST, entity, String.class);

            System.out.println("Status Code : " + response2.getStatusCode());
            System.out.println("Response : " + response2.getBody());

        } catch (Exception e) {
            System.out.println("Error while calling API");
            e.printStackTrace();
        }
    }

}
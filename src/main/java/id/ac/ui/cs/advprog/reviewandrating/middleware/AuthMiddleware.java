package id.ac.ui.cs.advprog.reviewandrating.middleware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthMiddleware {
    @Value("${url.api.gateway}")
    private String urlApiGateaway;

    RestTemplate restTemplate;

    @Autowired
    public AuthMiddleware(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public String getUsernameFromToken(String token) throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
        ResponseEntity<String> response = restTemplate.exchange(String.format("%suser/get-username", urlApiGateaway),
                HttpMethod.GET, httpEntity, String.class);

        return response.getBody();
    }

    public String getRoleFromToken(String token) throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
        ResponseEntity<String> response = restTemplate.exchange(String.format("%suser/get-role", urlApiGateaway),
                HttpMethod.GET, httpEntity, String.class);

        return response.getBody();
    }
}

package id.ac.ui.cs.advprog.reviewandrating.middleware;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class AuthMiddlewareTest {
    @MockBean
    RestTemplate restTemplate;

    @Autowired
    AuthMiddleware authMiddleware;

    HttpEntity<String> httpEntity;
    String token = "Random";


    @BeforeEach
    void setUp() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        httpEntity = new HttpEntity<>("body", headers);
    }

    @Test
    void testGetNameFromToken() {
        String url = "http://35.198.243.155/user/get-username";
        when(restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class)).thenReturn(ResponseEntity.ok("Hanau"));
        String name = authMiddleware.getUsernameFromToken(token);
        assertEquals("Hanau", name);
    }

    @Test
    void testGetRoleFromToken() {
        String url = "http://35.198.243.155/user/get-role";
        when(restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class)).thenReturn(ResponseEntity.ok("STAFF"));
        String role = authMiddleware.getRoleFromToken(token);
        assertEquals("STAFF", role);
    }
}

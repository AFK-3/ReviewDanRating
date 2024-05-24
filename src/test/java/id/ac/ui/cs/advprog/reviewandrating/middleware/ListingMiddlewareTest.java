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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class ListingMiddlewareTest {
    @MockBean
    RestTemplate restTemplate;

    @Autowired
    ListingMIddleware listingMiddleware;

    HttpEntity<String> httpEntity;

    String url = "http://35.198.243.155/listing/get-by-id/123";
    String token = "Random";

    @BeforeEach
    void setUp() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        httpEntity = new HttpEntity<>("body", headers);
    }

    @Test
    void testListingExist() {
        when(restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class)).thenReturn(ResponseEntity.ok().build());
        boolean isExist = listingMiddleware.isListingExist("123", token);
        assertTrue(isExist);
    }

    @Test
    void testListingNotExist() {
        when(restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class)).thenThrow(RestClientException.class);
        boolean isExist = listingMiddleware.isListingExist("123", token);
        assertFalse(isExist);
    }
}



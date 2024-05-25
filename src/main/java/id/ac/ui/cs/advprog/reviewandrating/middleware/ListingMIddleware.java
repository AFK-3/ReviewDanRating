package id.ac.ui.cs.advprog.reviewandrating.middleware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ListingMIddleware {
    @Value("${url.api.gateway}")
    private String urlApiGateaway;
    RestTemplate restTemplate;

    @Autowired
    public ListingMIddleware(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isListingExist(String listingId, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", token);
            HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
            String url = UriComponentsBuilder.fromHttpUrl(urlApiGateaway)
                    .path("/listing/get-by-id/")
                    .path(listingId)
                    .build()
                    .toUriString();
            restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

            return true;
        }
        catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                return false;
            throw new RestClientException("UnAuthorized");
        }
    }
}

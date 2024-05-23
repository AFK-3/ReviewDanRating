package id.ac.ui.cs.advprog.reviewandrating.middleware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ListingMIddleware {
    private String urlApiGateaway = "http://35.198.243.155/";
    @Autowired
    RestTemplate restTemplate;

    public Boolean isListingExist(String listingId, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", token);
            HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
            ResponseEntity<String> response = restTemplate.exchange(String.format("%s/listing/get-by-id/%s",
                    urlApiGateaway, listingId), HttpMethod.GET, httpEntity, String.class);

            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}

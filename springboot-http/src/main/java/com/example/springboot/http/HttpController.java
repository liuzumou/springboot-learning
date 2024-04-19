package com.example.springboot.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

@Slf4j
@RestController
public class HttpController {

    @GetMapping("/1")
    public String useHttpUrlConnection() {
        StringBuilder result = null;
        try {
            // Creating a Request
            URL url = URI.create("https://postman-echo.com/get").toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            // Adding Request Parameters

            // Setting Request Headers
            //con.setRequestProperty("Content-Type", "application/json");
            //read: String contentType = con.getHeaderField("Content-Type");

            // con.connect();

            // Reading the Response
            int status = con.getResponseCode();
            log.info("status:" + status);
            if (status == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                result = new StringBuilder();
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    result.append(inputLine);
                }
                reader.close();
            }
            con.disconnect();
        } catch (Exception ignored) {

        }
        return result != null ? result.toString() : "error";
    }

    @GetMapping("/2")
    public String useHttpClient(){
        String result = "";
        try {
            // Setting GET URI
            HttpRequest getRequest = HttpRequest.newBuilder(new URI("https://postman-echo.com/get"))
                    .GET()
                    .headers("key1", "value1", "key2", "value2")
                    .timeout(Duration.of(10, SECONDS))
                    .build();

            // Setting POST URI
            HttpRequest postRequest = HttpRequest.newBuilder(new URI("https://postman-echo.com/post"))
//                    .POST(HttpRequest.BodyPublishers.noBody())
                    .POST(HttpRequest.BodyPublishers.ofString("Sample request body"))
                    .headers("Content-Type", "text/plain;charset=UTF-8")
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(getRequest, HttpResponse.BodyHandlers.ofString());

//            HttpResponse<String> response = HttpClient
//                    .newBuilder()
//                    .proxy(ProxySelector.getDefault())
//                    .build()
//                    .send(postRequest, HttpResponse.BodyHandlers.ofString());
            result = response.body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @GetMapping("/3")
    public JsonNode useRestTemplate() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("https://postman-echo.com/get", String.class);
//        return response.getBody();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(response.getBody());
    }
}

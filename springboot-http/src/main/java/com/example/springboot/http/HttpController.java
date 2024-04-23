package com.example.springboot.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

@Slf4j
@RestController
public class HttpController {

    @GetMapping("/1")
    public String useHttpUrlConnection(@RequestParam String target) {
        target = StringUtils.hasLength(target) ? target : "https://postman-echo.com/get";
        StringBuilder result = null;
        try {
            // Creating a Request
            URL url = URI.create(target).toURL();
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
    public String useHttpClient() {
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

    @GetMapping("/https")
    public String sendHttps() {
        String result = "";
        try {
            // 创建信任管理器
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};

            // 获取默认的SSL上下文
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            // 创建URL对象
            URL url = new URL("https://api.example.com/data"); // 替换为实际的URL

            // 打开HTTPS连接
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            // 设置请求方法为GET
            connection.setRequestMethod("GET");

            // 获取响应状态码
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // 读取响应内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            result = response.toString();
            // 关闭连接
            connection.disconnect();

        } catch (Exception e) {
            result = "error";
        }
        return result;
    }
}

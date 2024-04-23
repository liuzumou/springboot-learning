package com.example.springboot.http;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;

@Slf4j
@RestController
public class HttpProxyController {

    /**
     * simple proxy by chatGPT3.5
     *
     * @param request
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    @RequestMapping("/proxy")
    public String simpleProxy(HttpServletRequest request) throws URISyntaxException, IOException {

//        String targetUrl = request.getParameter("target");

        // 构建目标URL
        StringBuilder targetUrlBuilder = new StringBuilder("https://postman-echo.com/get");
//        StringBuilder targetUrlBuilder = new StringBuilder("http://www.baidu.com/s");


        // 检查是否有查询参数
        if (request.getQueryString() != null) {
            targetUrlBuilder.append("?").append(request.getQueryString());
        }
        URL url = new URL(targetUrlBuilder.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();


        // 设置代理
//        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod(request.getMethod());

        // 读取请求头，并设置到目标请求
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            connection.setRequestProperty(headerName, request.getHeader(headerName));
//            log.info(headerName + ":" + request.getHeader(headerName));
        }

        // 发起目标请求
        connection.connect();
        String responseBody;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            responseBody = response.toString();
        }

        // 返回目标请求的响应
        return responseBody;
    }




}

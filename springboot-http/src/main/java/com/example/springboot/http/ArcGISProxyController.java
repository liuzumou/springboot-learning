package com.example.springboot.http;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class ArcGISProxyController {

    /**
     * http://localhost:8080/arcigs
     * http://192.168.11.4:6080/arcgis
     *
     * @param request
     * @param response
     */
    @RequestMapping("/**")
    public void proxy(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletOutputStream servletOutputStream = null;
        InputStream is = null;
        OutputStream os = null;
        String reqUrl = null;
        URL newURL = null;

        reqUrl = request.getRequestURL().toString();
        String contextPath = request.getContextPath();
        log.info("reqUrl:" + reqUrl);
        log.info("contextPath:" + contextPath);
        log.info("queryString:" + request.getQueryString());

        String xForURLBase = null;
        if (!"/arcgis".equals(contextPath)) {
            reqUrl = Util.replaceContext(reqUrl, contextPath, "/arcgis");
            xForURLBase = contextPath;
        }

        if (request.getQueryString() != null)
            reqUrl = reqUrl + "?" + request.getQueryString();
        String requestContext = Util.getAppURL(request);
        URL url = new URL(reqUrl);
        String reqProtocol = url.getProtocol();

        HttpURLConnection con = null;
        int statusCode = 0;
        // 处理转发
        try {
            // 创建connection
            newURL = new URL(url.getProtocol() + "://111.75.216.198:6080" + url.getFile());

            con = (HttpURLConnection) newURL.openConnection();
            con.setInstanceFollowRedirects(false);
            con.setRequestMethod(request.getMethod());

            //x-forwarded properties
            if (StringUtils.hasLength(xForURLBase))
                con.setRequestProperty("X-Forwarded-Url-Base", xForURLBase);
            if (StringUtils.hasLength(requestContext))
                con.setRequestProperty("X-Forwarded-Request-Context", requestContext);
            String remoteIpAddress = request.getRemoteAddr();
            con.setRequestProperty("X-Forwarded-For", StringUtils.hasLength(remoteIpAddress) ? "" : remoteIpAddress);

            //复制请求头
            Enumeration<?> reqhs = request.getHeaderNames();
            while (reqhs.hasMoreElements()) {
                String h = (String) reqhs.nextElement();
                //accept-encoding 异常 未解决
                if (h != null) {
                    con.setRequestProperty(h, request.getHeader(h));
//                    log.info(h + ":" + request.getHeader(h));
                }
            }
            log.info(con.getContentEncoding());
            //POST处理
            if ("POST".equals(request.getMethod())) {
                con.setUseCaches(false);
                con.setDoOutput(true);
                ServletInputStream servletInputStream = request.getInputStream();
                os = con.getOutputStream();
                IOUtils.copy(servletInputStream, os);
                IOUtils.closeQuietly(servletInputStream);
                IOUtils.closeQuietly(os);
            }
            statusCode = con.getResponseCode();
            try {
                is = con.getInputStream();
            } catch (Exception ignore) {
                is = con.getErrorStream();
            }
            if (is == null)
                is = con.getErrorStream();


            // header 回写   chunked
            Map<String, List<String>> hmap = con.getHeaderFields();
            log.info("hmap:" + hmap);
            for (Map.Entry<String, List<String>> entry : hmap.entrySet()) {
                String key = entry.getKey();
                List<String> headerValues = entry.getValue();
                if (headerValues != null) {
                    for (String headerValue : headerValues) {
                        if (!"chunked".equalsIgnoreCase(headerValue))//chunked bug
                            response.setHeader(key, headerValue);
                    }
                }
            }

            if (con.getContentType() != null)
                response.setContentType(con.getContentType());
            if (con.getResponseCode() > 0)
                response.setStatus(con.getResponseCode());
            // content-encoding  解决乱码问题
//            response.setHeader("content-encoding", con.getContentEncoding());
            // copy stream
            servletOutputStream = response.getOutputStream();
            IOUtils.copy(is, servletOutputStream);
            log.info("responseheader: "+response.getHeaderNames().toString());
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(servletOutputStream);
            if (con != null)
                con.disconnect();
        }
    }
}

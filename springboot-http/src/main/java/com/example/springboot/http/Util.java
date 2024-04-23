package com.example.springboot.http;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Util {

    public static String getAppURL(HttpServletRequest request) throws MalformedURLException {
        try {
            String urlStr = request.getRequestURL().toString();
            URL url = new URL(urlStr);
            String host = url.getHost();
            String port = (url.getPort() == 443 || url.getPort() == 80 || url.getPort() == -1) ? "" : (":" + url.getPort());
            String cp = request.getContextPath();
            return url.getProtocol() + "://" + host + port + cp;
        } catch (MalformedURLException e) {
            return request.getRequestURI().toString();
        }
    }

    public static String replaceContext(String requestUrl, String sourceContextpath, String destContextPath) throws MalformedURLException {
        try {
            URL url = new URL(requestUrl);
            String host = url.getHost();
            String port = (url.getPort() == 443 || url.getPort() == 80 || url.getPort() == -1) ? "" : (":" + url.getPort());
            String path = url.getPath();
            path = path.replaceFirst(sourceContextpath, destContextPath);
            return url.getProtocol() + "://" + host + port + path;
        } catch (MalformedURLException e) {
            return requestUrl;
        }
    }





    /**
     * 读取InputStream
     * @param is
     * @param enc
     * @return
     */
    public static String readFullStringFromInputStream(InputStream is, String enc) {
        try {
            InputStreamReader isr;
            if (enc == null) {
                isr = new InputStreamReader(is);
            } else {
                isr = new InputStreamReader(is, enc);
            }
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(isr);
            String str = br.readLine();
            while (str != null) {
                sb.append(str);
                str = br.readLine();
            }
            return sb.toString();
        } catch (IOException ioe) {
            return null;
        }
    }
}

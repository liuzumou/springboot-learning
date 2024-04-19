# springboot 发送http请求

通过示例例演示在java中如何发送http请求。 除了java自带的HttpUrlConnection、HttpClient、 RestTemplate外，还可以采用第三方的Apache HTTPClient、Hutool HttpClient等。

## HttpUrlConnection

HttpURLConnection 类是 Java 中用于实现 HTTP 协议的基础类，它提供了一系列方法来建立与 HTTP 服务器的连接、发送请求并读取响应信息

## HttpClient
java11 后，推荐使用HttpClient 来替代HttpUrlConnection

## RestTemplate

> 在Spring Framework 5中，除了WebFlux栈，Spring还引入了一个新的HTTP客户端WebClient。WebClient是一个现代的、可替代RestTemplate的HTTP客户端。它不仅提供了传统的同步API，而且还支持高效的非阻塞和异步方法。也就是说，如果我们正在开发新应用程序或迁移旧应用程序，建议使用WebClient。接下来，RestTemplate将在未来的版本中被弃用。

## 参考
Do a Simple HTTP Request in Java
https://www.baeldung.com/java-http-request

Exploring the New HTTP Client in Java
https://www.baeldung.com/java-9-http-client

The Guide to RestTemplate
https://www.baeldung.com/rest-template

java使用httpclient 、HttpURLConnection 发送第三方请求 使用详解、总结。HttpClient 的使用
https://blog.csdn.net/qq_40036754/article/details/102554755


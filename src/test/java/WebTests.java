import Web.ApplicationScope;
import Web.RequestScope;
import Web.SessionScope;
import org.junit.jupiter.api.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootConfiguration
@SpringBootApplication
@ComponentScan("Web")
public class WebTests {

    @Bean
    @Scope(value = "request",  proxyMode = ScopedProxyMode.TARGET_CLASS)
    public RequestScope requestScope() { return new RequestScope(); }
    @Bean
    @Scope(value = "session",  proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SessionScope sessionScope() { return new SessionScope(); }
    @Bean
    @Scope(value = "application", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ApplicationScope applicationScope() { return new ApplicationScope(); }

    @Test
    @DisplayName("Request scope creates a different instance on every new request")
    public void test25() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8083/request")).build();
        HttpResponse<String> httpResponse1 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> httpResponse2 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertNotEquals(httpResponse1.body(), httpResponse2.body());
    }

    @Test
    @DisplayName("Session scope creates a different instance for every session")
    public void test26() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8083/session")).build();
        HttpResponse<String> httpResponse1 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        HttpClient httpClient2 = HttpClient.newBuilder().build();
        HttpRequest httpRequest2 = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8083/session")).build();
        HttpResponse<String> httpResponse2 = httpClient2.send(httpRequest2, HttpResponse.BodyHandlers.ofString());

        assertNotEquals(httpResponse1.body(), httpResponse2.body());
    }

    @Test
    @DisplayName("Session scope does not create new instance for every request")
    public void test27() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8083/session")).build();
        HttpResponse<String> httpResponse1 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> httpResponse2 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertNotEquals(httpResponse1.body(), httpResponse2.body());
    }

    @Test
    @DisplayName("Application scope creates new instances for every application")
    public void test28() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8083/application")).build();
        HttpResponse<String> httpResponse1 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        shutDownSpringBootApp();
        startSpringBootApp();
        HttpResponse<String> httpResponse2 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertNotEquals(httpResponse1.body(), httpResponse2.body());
    }

    @Test
    @DisplayName("Application scope does not new instances for same application")
    public void test29() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8083/application")).build();
        HttpResponse<String> httpResponse1 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> httpResponse2 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(httpResponse1.body(), httpResponse2.body());
    }

    ConfigurableApplicationContext springApp;
    @BeforeEach
    void startSpringBootApp() {
        SpringApplication app = new SpringApplication(WebTests.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8083"));
        springApp = app.run();
    }

    @AfterEach
    void shutDownSpringBootApp() {
        springApp.close();
    }
}
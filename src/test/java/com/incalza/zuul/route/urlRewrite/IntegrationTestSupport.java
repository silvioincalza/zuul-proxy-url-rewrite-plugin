package com.incalza.zuul.route.urlRewrite;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.boot.SpringApplication.run;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by sincalza on 28/05/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IntegrationTestSupport.TestApplication.class, webEnvironment = RANDOM_PORT)
public abstract class IntegrationTestSupport {

    @SpringBootApplication
    @EnableZuulProxy
    public static class TestApplication {

        public static void main(String[] args) {
            run(TestApplication.class, args);
        }

    }

    @LocalServerPort
    private int serverPort;

    @Value("${mockwebserver.port}")
    private int mockWebServerPort;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    protected MockWebServer mockWebServer;


    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start(mockWebServerPort);
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    protected String localUrl(String path) {
        return "http://localhost:" + this.serverPort + path;
    }

    protected MockResponse aResponse() {
        return new MockResponse().setResponseCode(200).setBody("{\"isWork\": true}").setHeader("Content-Type", APPLICATION_JSON_VALUE);
    }

}

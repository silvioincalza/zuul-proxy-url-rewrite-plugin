package com.incalza.zuul.route.urlRewrite;

import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.TimeUnit;

import static java.net.URI.create;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Created by sincalza on 28/05/2017.
 */
public class UrlRewriteZuulProxyIT extends IntegrationTestSupport {


    @Test
    public void simpleRoute() throws Exception {
        mockWebServer.enqueue(aResponse());
        TimeUnit.SECONDS.sleep(2);
        final ResponseEntity<JsonNode> responseEntity = testRestTemplate.getForEntity(localUrl("/test/an_endpoint?parameter"), JsonNode.class);
        assertOnResponseThat(responseEntity);
        final RecordedRequest takeRequest = takeRecordedRequest();
        assertThat(takeRequest.getPath()).isSubstringOf("/services/an_endpoint?parameter=value");
    }

    @Test
    public void urlRewriteRoute() throws Exception {
        mockWebServer.enqueue(aResponse());
        TimeUnit.SECONDS.sleep(2);
        final String param2 = "58g";
        final RequestEntity<Void> requestEntity = RequestEntity.get(create(localUrl("/v1/a12d/" + param2 + "/services/test/69s"))).build();
        final ResponseEntity<JsonNode> responseEntity = testRestTemplate.exchange(requestEntity, JsonNode.class);
        assertOnResponseThat(responseEntity);
        final RecordedRequest takeRequest = takeRecordedRequest();
        assertThat(takeRequest.getPath()).endsWith("/services/test/" + param2);
    }

    private RecordedRequest takeRecordedRequest() throws InterruptedException {
        return mockWebServer.takeRequest(1, SECONDS);
    }

    private void assertOnResponseThat(ResponseEntity<JsonNode> responseEntity) {
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        assertThat(responseEntity.getHeaders().getContentType().isCompatibleWith(APPLICATION_JSON)).isEqualTo(true);
        assertThat(responseEntity.getBody().get("isWork").asBoolean()).isEqualTo(true);
    }


}

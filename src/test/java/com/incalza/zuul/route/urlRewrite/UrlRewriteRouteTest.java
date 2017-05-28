package com.incalza.zuul.route.urlRewrite;

import org.junit.Test;
import org.springframework.cloud.netflix.zuul.filters.Route;

import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
/**
 * Created by sincalza on 28/05/2017.
 */
public class UrlRewriteRouteTest {

    @Test
    public void newInstance() {
        Route route = new Route("ID", "/PATH", "http://LOCATION", "", true, Collections.<String>emptySet());
        final UrlRewriteRoute urlRewriteRoute = new UrlRewriteRoute(route);
        assertRouteThat(urlRewriteRoute);
        assertThat(urlRewriteRoute.isUrlRewriteRoute(), is(false));
    }

    @Test
    public void newInstanceWhenIsUrlRewrite() {
        Route route = new Route("ID", "/PATH", "urlRewrite:http://LOCATION", "", true, Collections.<String>emptySet());
        final UrlRewriteRoute urlRewriteRoute = new UrlRewriteRoute(route);
        assertRouteThat(urlRewriteRoute);
        assertThat(urlRewriteRoute.isUrlRewriteRoute(), is(true));
    }

    private void assertRouteThat(Route route) {
        UrlRewriteRoute urlRewriteRoute = new UrlRewriteRoute(route);
        assertThat(urlRewriteRoute.getLocation(), is("http://LOCATION"));
        assertThat(urlRewriteRoute.getPath(), is("/PATH"));
        assertThat(urlRewriteRoute.getId(), is("ID"));
        assertThat(urlRewriteRoute.getPrefix(), is(""));
        assertThat(urlRewriteRoute.getRetryable(), is(true));
    }


}
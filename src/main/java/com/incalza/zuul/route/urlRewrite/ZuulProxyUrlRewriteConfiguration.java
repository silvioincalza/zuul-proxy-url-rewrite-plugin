package com.incalza.zuul.route.urlRewrite;

import org.springframework.cloud.netflix.zuul.ZuulProxyConfiguration;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by sincalza on 28/05/2017.
 */
@Configuration
public class ZuulProxyUrlRewriteConfiguration extends ZuulProxyConfiguration {
    @Bean
    public SimpleRouteLocator urlRewriteRouteLocator() {
        return new UrlRewriteRouteLocator(server.getServletPrefix(), zuulProperties);
    }

    @Bean
    public UrlRewriteZuulFilter urlRewriteZuulFilter() {
        return new UrlRewriteZuulFilter(urlRewriteRouteLocator());
    }
}

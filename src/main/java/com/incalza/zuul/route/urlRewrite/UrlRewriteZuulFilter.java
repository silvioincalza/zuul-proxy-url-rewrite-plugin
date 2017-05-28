package com.incalza.zuul.route.urlRewrite;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;

import java.net.MalformedURLException;
import java.net.URL;

import static com.incalza.zuul.route.urlRewrite.UrlRewriteRoute.isUrlRewriteRoute;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;

/**
 * Created by sincalza on 28/05/2017.
 */
public class UrlRewriteZuulFilter extends ZuulFilter {
    private static final Logger log = LoggerFactory.getLogger(UrlRewriteZuulFilter.class);
    private static final String REQUEST_URI = "requestURI";
    private static final int ORDER = PRE_DECORATION_FILTER_ORDER + 1;

    private final RouteLocator routeLocator;

    public UrlRewriteZuulFilter(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @Override
    public int filterOrder() {
        return ORDER; // order: after PreDecorationFilter
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.containsKey(REQUEST_URI); // a filter has requestUri
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        final String requestURI = ctx.getRequest().getRequestURI();
        final Route route = this.routeLocator.getMatchingRoute(requestURI);
        log.info("Zuul UrlRewrite matching path: {} with location: {}", route.getFullPath(), route.getLocation());
        if (isUrlRewriteRoute(route)) {
            // see PreDecorationFilter how to use location, at the moment missing some behaviour
            final String location = route.getLocation();
            ctx.setRouteHost(toUrl(location));
            ctx.addOriginResponseHeader("X-Zuul-Service", location);
            ctx.set(REQUEST_URI, ""); // clean request_uri in order to use directly the route host
        }
        return null;
    }

    private URL toUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed on convert to url " + url, e);
        }
    }
}

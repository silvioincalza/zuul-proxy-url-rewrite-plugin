package com.incalza.zuul.route.urlRewrite;

import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import static com.incalza.zuul.route.urlRewrite.UrlRewriteRoute.isUrlRewriteRoute;

/**
 * Created by sincalza on 28/05/2017.
 */
public class UrlRewriteRouteLocator extends SimpleRouteLocator {

    private final PathMatcher pathMatcher = new AntPathMatcher();


    public UrlRewriteRouteLocator(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
    }


    @Override
    protected Route getRoute(ZuulProperties.ZuulRoute route, String path) {
        final Route _route = super.getRoute(route, path);
        return isUrlRewriteRoute(_route) ? new UrlRewriteRoute(_route) : _route;
    }


    @Override
    protected Route getSimpleMatchingRoute(String path) {
        for (Route route : getRoutes()) {
            if (isUrlRewriteRoute(route)) {
                if (pathMatcher.match(route.getFullPath(), path)) {
                    return ((UrlRewriteRoute) route).rewriteLocation(path);
                }
            }
        }
        return super.getSimpleMatchingRoute(path);
    }


}

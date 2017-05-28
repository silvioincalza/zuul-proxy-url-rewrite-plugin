package com.incalza.zuul.route.urlRewrite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.Map;

import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.removeStart;

/**
 * Created by sincalza on 28/05/2017.
 */
public class UrlRewriteRoute extends Route {
    private static final Logger log = LoggerFactory.getLogger(UrlRewriteRoute.class);
    private static final String PREFIX_URL_REWRITE = "urlRewrite:";
    private final boolean urlRewrite;
    private static final PathMatcher pathMatcher = new AntPathMatcher();
    private String locationRewritten;
    private boolean isLocationRewritten;

    public UrlRewriteRoute(Route route) {
        super(route.getId(), route.getPath(), route.getLocation(), route.getPrefix(), route.getRetryable(), route.getSensitiveHeaders());
        this.urlRewrite = isUrlRewriteRoute(route);
    }

    public boolean isUrlRewriteRoute() {
        return urlRewrite;
    }

    @Override
    public String getLocation() {
        return isLocationRewritten ? locationRewritten : cleanUrlRewritePrefix(super.getLocation());
    }


    public UrlRewriteRoute rewriteLocation(String originalPath) {
        this.isLocationRewritten = false;
        final Map<String, String> uriTemplateVariables = extractUriTemplateVariables(this, originalPath);
        this.locationRewritten = rewriteLocation(this, uriTemplateVariables);
        log.info("Rewritten location from configuration '{}' to '{}', the parameters extracted from requestURI '{}' are {}.",
                cleanUrlRewritePrefix(super.getLocation()),
                locationRewritten,
                originalPath, uriTemplateVariables);
        this.isLocationRewritten = true;
        return this;
    }


    private String rewriteLocation(Route route, Map<String, String> uriTemplateVariables) {
        String location = route.getLocation();
        for (Map.Entry<String, String> entry : uriTemplateVariables.entrySet()) {
            String pattern = format("\\{%s\\}", entry.getKey());
            location = location.replaceAll(pattern, entry.getValue());
        }
        return location;
    }

    private Map<String, String> extractUriTemplateVariables(Route route, String path) {
        return pathMatcher.extractUriTemplateVariables(route.getPath(), path);
    }


    private String cleanUrlRewritePrefix(String location) {
        return removeStart(location, PREFIX_URL_REWRITE);
    }


    public static boolean isUrlRewriteRoute(Route route) {
        return (route instanceof UrlRewriteRoute && ((UrlRewriteRoute) route).isUrlRewriteRoute())
                || route.getLocation().startsWith(PREFIX_URL_REWRITE);
    }


}
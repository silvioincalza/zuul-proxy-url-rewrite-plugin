#### Zuul Proxy Url Rewrite Plugin [![Build Status](https://travis-ci.org/silvioincalza/zuul-proxy-url-rewrite-plugin.svg?branch=master)](https://travis-ci.org/silvioincalza/zuul-proxy-url-rewrite-plugin)

The UrlRewriteZuulFilter is a implementation of a `ZuulFilter` is able to recognize, using Ant pattern matching which is already
used by Zuul and SpringMVC this complex pattern using a prefix on the url parameter called `urlRewrite`.

If you import zuul package you will be able to do:

```
zuul:
  routes:
    aConfig:
      path: /v1/{param1:\w+}/{param2:\w+}/services/test/{param3:\w+}
      url: urlRewrite:http://localhost:${mockwebserver.port}/services/test/{param2}
```

To know about the meaning of `{param1:\w+}` and how to create your pattern matching you can read
AntPathMatcher [api docs](http://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/util/AntPathMatcher.html)

Be aware that on the `url` property you don't have to repeat the path matching pattern, so instead of `{param2:\w+}` just use `{param2}`

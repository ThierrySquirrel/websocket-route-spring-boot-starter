# websocket-route-spring-boot-starter

Websocket Route Spring Book Edition

[中文](./README_zh_CN.md)

Support Function:
- [x] WebsocketRoute

# WebsocketRoute:  
 Make Websocket Cluster More Optional  
 
# Websocket 
 [websocket-spring-boot-starter](https://github.com/ThierrySquirrel/websocket-spring-boot-starter)

## Quick Start

```xml
<!--Adding dependencies to pom. XML-->
        <dependency>
            <artifactId>websocket-route-spring-boot-starter</artifactId>
            <groupId>com.github.thierrysquirrel</groupId>
            <version>1.1.0.3-RELEASE</version>
        </dependency>
``` 

 ### configuration file
 
 ```properties
 ## application.properties
websocket.route.url=127.0.0.1:9090
websocket.route.relays[0].match-path-prefix=/user
websocket.route.relays[0].relay-bean-name=userRelay
 ```

 # Start Websocket
 ```java
 @SpringBootApplication
 public class WebsocketRouteApplication{
     public static void main(String[] args){
         SpringApplication.run(WebsocketRouteApplication.class, args);
     }
 }
 ```

# Getting Started WebsocketRoute
```java
@Component
public class UserRoute implements WebsocketRelayTemplate {
    public static List<String> websocketServerUrl=new ArrayList<> ();
    static {
        /**
         * You Can Use The RPC Tool To Obtain The Cluster URL Address Periodically
         * For Example, Use HTTP To Request URL Storage Server (HttpServer+redis)
         */
        websocketServerUrl.add ("127.0.0.1:8080");
        websocketServerUrl.add ("127.0.0.1:8081");
        websocketServerUrl.add ("127.0.0.1:8082");
    }
    @Override
    public HttpUpgradeMessage relay(HttpRequestMessage requestMessage) throws WebsocketRouteException {
        String uriPath = requestMessage.getUri ();
        Map<String, String> uriParam = requestMessage.getUriParam ();
        HttpHeaders headers = requestMessage.getHeaders ();
//        String hello = uriParam.get ("hello");
//        String localUrl = getLocalUrl (Integer.parseInt (hello));
        String headersUrl = getHeadersUrl (headers);
        headers.set ("ConnectUrl",headersUrl);
        return HttpUpgradeMessageBuilder.builderHttpUpgradeMessage (headersUrl,uriPath,headers);

    }
    public static String getHeadersUrl(HttpHeaders headers){
        /**
         * There Are Many Things That Can Be Done Here
         * Example 1.
         * Team up to join the same server
         * The Team Leader Requests From The URL Server And Gets The URL (The Request Here Can Use A Variety Of Protocols, Such As HTTP + Redis)
         * The Team Leader Assigns The URL To His Teammates.
         * Each Person's Request Header carries The URL And Connects To The Same Server
         * Example 2.
         * Join Friends Server
         * Request Ro Store The URL Of A Friend's Connected Server,(The Request Here Can Use A Variety Of Protocols, Such As HTTP + Redis)
         * The Request Header Carries The URL And Connects To The Same Server
         *
         */
        return headers.get ("world");
    }
    public static String getLocalUrl(int offset){
        return websocketServerUrl.get (offset);
    }
}
```
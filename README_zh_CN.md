# websocket-route-spring-boot-starter

Websocket Route Spring Book Edition

[English](./README.md)

支持功能:
- [x] Websocket路由

# Websocket路由:  
 使websocket群集更具可选性
 
# Websocket 
 [websocket-spring-boot-starter](https://github.com/ThierrySquirrel/websocket-spring-boot-starter)   

## Quick Start

```xml
<!--在pom.xml中添加依赖-->
        <dependency>
            <artifactId>websocket-route-spring-boot-starter</artifactId>
            <groupId>com.github.thierrysquirrel</groupId>
            <version>1.0.6-RELEASE</version>
        </dependency>
``` 

 ### 配置文件
 
 ```properties
 ## application.properties
websocket.route.url=127.0.0.1:9090
websocket.route.relays[0].match-path-prefix=/user
websocket.route.relays[0].relay-bean-name=userRelay
 ```

 # 启动 Websocket
 ```java
 @SpringBootApplication
 public class WebsocketRouteApplication{
     public static void main(String[] args){
         SpringApplication.run(WebsocketRouteApplication.class, args);
     }
 }
 ```

# 开始使用 WebsocketRoute
```java
@Component
public class UserRoute implements WebsocketRelayTemplate {
    public static List<String> websocketServerUrl=new ArrayList<> ();
    static {
        /**
         * 您可以使用RPC工具定期获取集群URL地址,
         * 例如,使用http去请求Url存储服务器 (HttpServer+redis)
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
         * 在这里可以做很多事情
         * 举例 1.
         * 组队加入同一个服务器
         * 队长向url服务器请求,获得url.这里的请求可以使用多种协议例如:Http+Redis
         * 队长将url分配给队友.
         * 每个人的请求头携带URL并连接到同一个服务器
         * 举例 2.
         * 加入好友服务器
         * 请求存放好友已连接服务器的URL,这里的请求可以使用多种协议例如:Http+Redis
         * 请求头携带WebsocketUrl,连接同一服务器
         *
         */
        return headers.get ("world");
    }
    public static String getLocalUrl(int offset){
        return websocketServerUrl.get (offset);
    }
}
```
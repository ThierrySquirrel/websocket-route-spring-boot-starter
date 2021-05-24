/**
 * Copyright 2020 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.thierrysquirrel.websocket.route.netty.server.handler.core.factory;

import com.github.thierrysquirrel.websocket.route.core.container.WebsocketRelayConstant;
import com.github.thierrysquirrel.websocket.route.core.domain.HttpRequestMessage;
import com.github.thierrysquirrel.websocket.route.core.domain.HttpUpgradeMessage;
import com.github.thierrysquirrel.websocket.route.core.domain.builder.HttpRequestMessageBuilder;
import com.github.thierrysquirrel.websocket.route.core.exception.WebsocketRouteException;
import com.github.thierrysquirrel.websocket.route.core.factory.UriFactory;
import com.github.thierrysquirrel.websocket.route.core.template.WebsocketRelayTemplate;
import com.github.thierrysquirrel.websocket.route.netty.client.handler.core.factory.HttpClientHandshakeFactory;
import com.github.thierrysquirrel.websocket.route.netty.client.init.WebsocketClientInit;
import com.github.thierrysquirrel.websocket.route.netty.server.handler.core.factory.constant.HandshakeFactoryConstant;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

import java.util.Map;

/**
 * ClassName: HttpServerHandshakeFactory
 * Description:
 * date: 2020/8/18 4:27
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
public class HttpServerHandshakeFactory {
    private HttpServerHandshakeFactory() {
    }

    public static WebsocketRelayTemplate pathValidation(String requestUri) throws WebsocketRouteException {
        return WebsocketRelayConstant.getWebsocketRelayTemplate (requestUri);
    }

    public static HttpUpgradeMessage relay(WebsocketRelayTemplate relayTemplate,String requestUri, HttpHeaders headers) throws WebsocketRouteException {
        Map<String, String> uriParamMap = UriFactory.getUriParamMap (requestUri);
        HttpRequestMessage requestMessage = HttpRequestMessageBuilder.builderHttpRequestMessage (requestUri, uriParamMap, headers);
        return relayTemplate.relay (requestMessage);
    }

    public static Channel clientChannelUpgrade(HttpUpgradeMessage upgradeMessage, Channel serverChannel, int maxFramePayloadLength, int readTimeoutMilli) throws WebsocketRouteException {
        WebSocketClientHandshaker clientHandshake = HttpClientHandshakeFactory.getWebSocketClientHandshake (upgradeMessage, maxFramePayloadLength);
        return new WebsocketClientInit (upgradeMessage.getUrl (), serverChannel, clientHandshake, readTimeoutMilli).init ();
    }

    public static void handshake(FullHttpRequest request, int maxFramePayloadLength, Channel serverChannel) throws WebsocketRouteException {
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory (
                getWebSocketUrl (request), null, true, maxFramePayloadLength);
        WebSocketServerHandshaker wsHandshake = wsFactory.newHandshaker (request);
        if (null == wsHandshake) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse (serverChannel);
            throw new WebsocketRouteException ("Handshake Fail");
        }
        wsHandshake.handshake (serverChannel, request);
    }


    private static String getWebSocketUrl(FullHttpRequest request) {
        return HandshakeFactoryConstant.WEBSOCKET_PROTOCOL + request.headers ().get (HttpHeaderNames.HOST) + request.uri ();
    }

}

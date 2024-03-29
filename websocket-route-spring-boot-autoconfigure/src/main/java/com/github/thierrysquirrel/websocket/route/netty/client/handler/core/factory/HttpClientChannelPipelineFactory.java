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
package com.github.thierrysquirrel.websocket.route.netty.client.handler.core.factory;

import com.github.thierrysquirrel.websocket.route.netty.client.handler.HttpClientHandler;
import com.github.thierrysquirrel.websocket.route.netty.client.handler.WebsocketClientHandler;
import com.github.thierrysquirrel.websocket.route.netty.core.constant.IdleStateConstant;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * ClassName: HttpClientChannelPipelineFactory
 * Description:
 * date: 2020/8/18 5:15
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
public class HttpClientChannelPipelineFactory {
    private HttpClientChannelPipelineFactory() {
    }

    public static void upgradeWebsocketPipeline(ChannelPipeline pipeline, int readTimeoutMilli, Channel serverChannel) {
        pipeline.remove (IdleStateHandler.class);
        pipeline.remove (HttpClientHandler.class);
        pipeline.addFirst (new IdleStateHandler (readTimeoutMilli, IdleStateConstant.WEBSOCKET_WRITER_IDLE_TIME, IdleStateConstant.WEBSOCKET_ALL_IDLE_TIME))
                .addLast (new WebsocketClientHandler (serverChannel));
    }
}

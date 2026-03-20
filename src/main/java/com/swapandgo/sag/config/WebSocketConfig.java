package com.swapandgo.sag.config;

import com.swapandgo.sag.security.jwt.JwtTokenProvider;
import com.swapandgo.sag.security.user.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService userDetailsService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat")
                .setAllowedOriginPatterns(
                        "http://localhost:3000",
                        "http://localhost:8000",
                        "https://*.ngrok.app",
                        "https://*.ngrok-free.dev",
                        "https://swap-go-git-develop-diwonis-projects.vercel.app"
                );

        registry.addEndpoint("/ws/chat-sockjs")
                .setAllowedOriginPatterns(
                        "http://localhost:3000",
                        "http://localhost:8000",
                        "https://*.ngrok.app",
                        "https://*.ngrok-free.dev",
                        "https://swap-go-git-develop-diwonis-projects.vercel.app"
                )
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new WebSocketStompAuthInterceptor(jwtTokenProvider, userDetailsService));
    }
}

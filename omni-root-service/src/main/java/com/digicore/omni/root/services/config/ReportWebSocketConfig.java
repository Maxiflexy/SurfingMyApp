//package com.digicore.omni.root.services.config;
//
///*
// * @author Oluwatobi Ogunwuyi
// * @createdOn Feb-20(Tue)-2024
// */
//
//import com.auth0.jwt.JWT;
//import com.digicore.omni.root.lib.modules.common.services.ReportGeneratorService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.*;
//
//@Configuration
//@EnableWebSocketMessageBroker
//@RequiredArgsConstructor
////@Slf4j
//public class ReportWebSocketConfig implements WebSocketMessageBrokerConfigurer {
// private final JWT jwtDecoder;
//// private final ReportGeneratorService reportGeneratorService;
//// @Override
//// public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
////  log.info("i am alive");
////  registry.addHandler(reportGeneratorService, "ws").setAllowedOrigins("*")
////          .withSockJS();
////
//// }
//
// @Override
// public void configureMessageBroker(MessageBrokerRegistry config) {
//  config.enableSimpleBroker("/all","/specific");
//  config.setApplicationDestinationPrefixes("/app");
// }
//
// @Override
// public void registerStompEndpoints(StompEndpointRegistry registry) {
//  registry.addEndpoint("/report").setAllowedOriginPatterns("*").setHandshakeHandler(new UserHandshakeHandler(jwtDecoder));
//  registry.addEndpoint("/report").setAllowedOriginPatterns("*").setHandshakeHandler(new UserHandshakeHandler(jwtDecoder)).withSockJS();
// }
//
//}

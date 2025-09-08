//package com.digicore.omni.root.services.config;
//
///*
// * @author Oluwatobi Ogunwuyi
// * @createdOn Feb-21(Wed)-2024
// */
//
//import com.auth0.jwt.JWT;
//import com.digicore.common.util.ClientUtil;
//import com.sun.security.auth.UserPrincipal;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
//import org.springframework.web.util.WebUtils;
//
//import java.security.Principal;
//import java.util.Map;
//import java.util.UUID;
//@RequiredArgsConstructor
//@Configuration
//public class UserHandshakeHandler extends DefaultHandshakeHandler {
// private final Logger LOG = LoggerFactory.getLogger(UserHandshakeHandler.class);
//
// private final JWT jwtDecoder;
//
// @Override
// protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
//  String token = request.getURI().getQuery() !=null?request.getURI().getQuery().replace("accesstoken=",""):null;
//  if (token !=null){
//   String username =  jwtDecoder.decodeJwt(token).getClaim("username").asString();
//   LOG.info("User with ID '{}' opened the page", username);
//   return new UserPrincipal(username);
//  }
//  return new UserPrincipal("invalid-user");
//
//
//
// }
//}
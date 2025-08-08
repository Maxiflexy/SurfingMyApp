/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.workflow.request;

import com.digicore.omnexa.common.lib.approval.workflow.annotation.RequestHandler;
import com.digicore.omnexa.common.lib.approval.workflow.annotation.RequestType;
import com.digicore.omnexa.common.lib.approval.workflow.constant.RequestHandlerType;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jan-26(Sun)-2025
 */

public class RequestHandlerPostProcessor implements BeanPostProcessor {
  private final RequestHandlers handlers;
  private final List<RequestHandlerType> handlerTypeList;

  @Lazy
  public RequestHandlerPostProcessor(List<RequestHandlerType> handlerTypeList) {
    this.handlers = new RequestHandlers();
    this.handlerTypeList = handlerTypeList;
  }

  @Override
  public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
    return o;
  }

  @Override
  public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
    if (isRequestHandler(o)) {
      List<Method> requestTypes = getRequestTypes(o);
      requestTypes.forEach(
          requestType ->
              handlers.addHandler(
                  requestType.getAnnotation(RequestType.class).name(),
                  new RequestHandlers.RequestHandle(requestType, o)));
    }
    return o;
  }

  private boolean isRequestHandler(Object o) {

    if (o.getClass().isAnnotationPresent(RequestHandler.class)) {
      RequestHandler requestHandler = o.getClass().getAnnotation(RequestHandler.class);

      return handlerTypeList.contains(requestHandler.type());
    }
    return false;
  }

  private List<Method> getRequestTypes(Object object) {
    return Arrays.stream(object.getClass().getMethods())
        .filter(method -> method.isAnnotationPresent(RequestType.class))
        .toList();
  }

  public RequestHandlers getHandlers() {
    return handlers;
  }
}

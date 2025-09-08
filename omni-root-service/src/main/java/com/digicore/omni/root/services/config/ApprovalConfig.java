package com.digicore.omni.root.services.config;

import com.digicore.request.processor.enums.RequestHandlerType;
import com.digicore.request.processor.processors.RequestHandlerPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.List;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Oct-30(Sun)-2022
 */

@Configuration
public class ApprovalConfig {

    @Bean
    public RequestHandlerPostProcessor requestHandlerPostProcessor() {
        return new RequestHandlerPostProcessor(List.of(RequestHandlerType.PROCESS_MAKER_REQUESTS,
                RequestHandlerType.MERCHANT_COMPLIANCE_CHECKER, RequestHandlerType.FEE_CONFIGURATION_REQUEST,
                RequestHandlerType.DISPUTE_MANAGEMENT,RequestHandlerType.ROUTING_RULE_MANAGEMENT,
                RequestHandlerType.TERMINAL_MANAGEMENT_REQUEST,
                RequestHandlerType.SETTLEMENT_PROCESS_REQUEST));
    }

//    @Bean
//    public Queue rootQueue() {
//        return new Queue("omniRootNotification", false);
//    }

    @Bean
    @Primary
    @Description("Thymeleaf template engine with Spring integration")
    public SpringTemplateEngine springTemplateEngine() {
        return new SpringTemplateEngine();
    }

    @Bean
    @Primary
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1000); // Adjust the core pool size as needed
        executor.setMaxPoolSize(2000); // Adjust the max pool size as needed
        executor.setQueueCapacity(3000); // Adjust the queue capacity as needed
        executor.setThreadNamePrefix("CustomExecutor-");
        executor.initialize();
        return executor;
    }

}

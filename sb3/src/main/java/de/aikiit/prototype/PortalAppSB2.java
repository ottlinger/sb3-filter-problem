package de.aikiit.prototype;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.stream.Stream;

@SpringBootApplication
@Slf4j
public class PortalAppSB2 {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(PortalAppSB2.class, args);

        log.info("Started application with {} beans", applicationContext.getBeanDefinitionCount());
        final Stream<String> sorted = Arrays.stream(applicationContext.getBeanDefinitionNames()).sorted();
        for (Object bean : sorted.toArray()) {
            log.debug("{}", bean);
        }
    }

}
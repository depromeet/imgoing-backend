package org.imgoing.push;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.thymeleaf.TemplateEngine;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class PushApplication {
    public static void main(String[] args) {
//        BlockHound.install();
        BlockHound.builder()
                .allowBlockingCallsInside(TemplateEngine.class.getCanonicalName(), "process")
                .install();
        SpringApplication.run(PushApplication.class, args);
    }
}

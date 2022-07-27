package com.qadr.bankapi.config;

import com.qadr.bankapi.model.Bank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@EnableScheduling
@Configuration
@Slf4j
public class SchedulerConfig {

    @Scheduled(initialDelay = 10, fixedRate = 300, timeUnit = TimeUnit.SECONDS)
    public void pingSelf(){
        var env = System.getenv("SITE_URL");
        String url = """
                %s/bank/id/{id}
                """.formatted(env);
        RestTemplate restTemplate = new RestTemplate();
        Bank forObject = restTemplate.getForObject(url, Bank.class, 1);
        if(forObject == null){
            log.error("ping self returned null object");
        }
    }

}

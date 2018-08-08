package com.wnc.wynews;

import com.wnc.wynews.spy.WyNewsSpy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableScheduling
// @EnableTransactionManagement
public class App extends SpringBootServletInitializer
{
    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application )
    {
        return application.sources( App.class );
    }

    public static void main( String[] args )
    {
        SpringApplication.run( App.class, args );
        try {
            new WyNewsSpy().spy();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

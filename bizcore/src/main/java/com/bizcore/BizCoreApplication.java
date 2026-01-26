package com.bizcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * BizCore Application
 *
 * This is the main entry point for BizCore platform.
 * It can run standalone or be used as a library by other applications.
 */
@SpringBootApplication
public class BizCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BizCoreApplication.class, args);
    }
}

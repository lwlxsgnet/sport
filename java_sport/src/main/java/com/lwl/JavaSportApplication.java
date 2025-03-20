package com.lwl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("com.lwl.mapper")
public class JavaSportApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaSportApplication.class, args);
    }

}

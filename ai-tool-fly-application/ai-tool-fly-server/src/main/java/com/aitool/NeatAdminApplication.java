package com.aitool;

import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author 10100
 */
@SpringBootApplication
@EnableFileStorage
@EnableCaching
@MapperScan("com.aitool.mapper")
public class NeatAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(NeatAdminApplication.class, args);
        System.out.println("系统启动成功！");
    }
}

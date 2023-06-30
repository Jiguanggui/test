package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
        long l = System.currentTimeMillis();
        System.out.println(l);

        Date date = new Date();
        System.out.println(date); //Fri Jun 30 10:12:38 CST 2023
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(format.format(date)); //2023-06-30 10:12:38
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate); //  2023-06-30
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime); //2023-06-30T10:12:38.140


        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(dateTimeFormatter.format(localDateTime));  //2023-06-30 10:12:38

    }



    @Test
    void test1() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        System.out.println(simpleDateFormat.format(date));
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(dateTimeFormatter.format(localDateTime));
    }


}

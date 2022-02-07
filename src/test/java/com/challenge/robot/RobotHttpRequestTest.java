package com.challenge.robot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RobotHttpRequestTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testRestAPI() {
        List<String> excepted = List.of("Output: 1,2,NORTH", "Output: 1,3,NORTH", "Output: 1,3,WEST", "Output: 0,3,WEST");
        String url = "http://localhost:" + port + "/?command=place 1,2,north report move report left report move report";
        Assertions.assertEquals(excepted, testRestTemplate.getForObject(url, List.class));
    }
}

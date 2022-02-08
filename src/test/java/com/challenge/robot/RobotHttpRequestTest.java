package com.challenge.robot;

import com.challenge.robot.service.RobotService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RobotHttpRequestTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testRestAPI() {
        List<String> excepted = List.of("1,2,NORTH", "1,3,NORTH", "1,3,WEST", "0,3,WEST");
        String url = "http://localhost:" + port + "/?command=place 1,2,north report move report left report move report";
        Assertions.assertEquals(excepted, testRestTemplate.getForObject(url, Map.class).get(RobotService.OUTPUT));
    }
}

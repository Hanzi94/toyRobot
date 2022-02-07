package com.challenge.robot;

import static org.assertj.core.api.Assertions.assertThat;

import com.challenge.robot.controller.RobotController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RobotApplicationTests {
    @Autowired
    private RobotController robotController;

    @Test
    void contextLoads() {
        assertThat(robotController).isNotNull();
    }

}

package com.challenge.robot.controller;

import com.challenge.robot.service.RobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * The rest api for toy robot with command set string for commands split with white space:
 *      PLACE X,Y,F
 *      MOVE
 *      LEFT
 *      RIGHT
 *      REPORT
 */
@RestController
public class RobotController {
    private final RobotService robotService;
    @Autowired
    public RobotController(RobotService robotService){
        this.robotService = robotService;
    }

    @GetMapping("/")
    public Map<String, List<String>> processCommand(@RequestParam(value = "command") String command){
        return robotService.processCommand(command);
    }
}

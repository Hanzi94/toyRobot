package com.challenge.robot.controller;

import com.challenge.robot.service.RobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RobotController {
    private final RobotService robotService;
    @Autowired
    public RobotController(RobotService robotService){
        this.robotService = robotService;
    }

    @GetMapping("/")
    public List<String>processCommand(@RequestParam(value = "command") String command){
        return robotService.processCommand(command);
    }
}

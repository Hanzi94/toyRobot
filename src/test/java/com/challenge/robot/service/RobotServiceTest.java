package com.challenge.robot.service;

import com.challenge.robot.model.Direction;
import com.challenge.robot.model.Robot;
import com.challenge.robot.model.TurnDirection;
import com.challenge.robot.service.RobotService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class RobotServiceTest {
    @Autowired
    RobotService robotService;
    @Test
    void testMovement(){
        final HashMap<String, String> moveCases = new HashMap<>();

        int x1, y1;
        for(int x = Robot.MIN_VALUE; x <= Robot.MAX_VALUE; x++){
            for(int y = Robot.MIN_VALUE; y <= Robot.MAX_VALUE; y++){
                for (Direction dir : Direction.values()) {
                    switch (dir){
                        case NORTH -> {
                           y1 = y < Robot.MAX_VALUE ? y+1 : y;
                           moveCases.put(x + "," + y + "," + dir,  x + "," + y1 + "," + dir);
                        }
                        case SOUTH -> {
                            y1 = y > Robot.MIN_VALUE ? y-1 : y;
                            moveCases.put(x + "," + y + "," + dir,  x + "," + y1 + "," + dir);
                        }
                        case EAST -> {
                            x1 = x < Robot.MAX_VALUE ? x+1 : x;
                            moveCases.put(x + "," + y + "," + dir,  x1 + "," + y + "," + dir);
                        }
                        case WEST -> {
                            x1 = x > Robot.MIN_VALUE ? x-1 : x;
                            moveCases.put(x + "," + y + "," + dir,  x1 + "," + y + "," + dir);
                        }
                        default -> {}
                    }
                }
            }
        }
        for (Map.Entry<String, String> entry : moveCases.entrySet()) {
            Assertions.assertEquals(List.of("Output: " + entry.getValue()),
                    robotService.processCommand("Place " + entry.getKey() + " move report"));
        }
    }
    @Test
    void testTurn(){
        final HashMap<String, String> turnCases = new HashMap<>();
        for (Direction dir : Direction.values()) {
            switch (dir) {
                case NORTH -> {
                    turnCases.put("PLACE 1,1," + dir + " " + TurnDirection.LEFT, "1,1," + Direction.WEST);
                    turnCases.put("PLACE 1,1," + dir + " " + TurnDirection.RIGHT, "1,1," + Direction.EAST);
                }
                case SOUTH -> {
                    turnCases.put("PLACE 1,1," + dir + " " + TurnDirection.LEFT, "1,1," + Direction.EAST);
                    turnCases.put("PLACE 1,1," + dir + " " + TurnDirection.RIGHT, "1,1," + Direction.WEST);
                }
                case EAST -> {
                    turnCases.put("PLACE 1,1," + dir + " " + TurnDirection.LEFT, "1,1," + Direction.NORTH);
                    turnCases.put("PLACE 1,1," + dir + " " + TurnDirection.RIGHT, "1,1," + Direction.SOUTH);
                }
                case WEST -> {
                    turnCases.put("PLACE 1,1," + dir + " " + TurnDirection.LEFT, "1,1," + Direction.SOUTH);
                    turnCases.put("PLACE 1,1," + dir + " " + TurnDirection.RIGHT, "1,1," + Direction.NORTH);
                }
                default -> {
                }
            }
        }
        for (Map.Entry<String, String> entry : turnCases.entrySet()) {
            Assertions.assertEquals(List.of("Output: " + entry.getValue()),
                    robotService.processCommand(entry.getKey() + " report"));
        }
    }

    @Test
    void testMissRobot(){
        Assertions.assertEquals(List.of("Output: ROBOT MISSING"),
                robotService.processCommand("left move report"));
        Assertions.assertEquals(List.of("Output: ROBOT MISSING", "Output: ROBOT MISSING"),
                robotService.processCommand("left move report right report"));
        Assertions.assertEquals(List.of("Output: ROBOT MISSING", "Output: ROBOT MISSING", "Output: 1,1,EAST"),
                robotService.processCommand("left move report right report place 1,1,east report"));
    }

    @Test
    void testNoReport(){
        Assertions.assertEquals(List.of(),
                robotService.processCommand("place 1,1,east left move"));
        Assertions.assertEquals(List.of("Output: ROBOT MISSING"),
                robotService.processCommand("left move right report place 1,1,east left move"));
    }

    @Test
    void testPlaceWrong(){
        Assertions.assertEquals(List.of("Output: Illegal place param (1,1,XREST)", "Output: ROBOT MISSING"),
                robotService.processCommand("place 1,1,xrest report"));
        Assertions.assertEquals(List.of("Output: Illegal place param (1,5,EAST)", "Output: ROBOT MISSING"),
                robotService.processCommand("place 1,5,east report"));
        Assertions.assertEquals(List.of("Output: Illegal place param (1,-1,EAST)", "Output: ROBOT MISSING"),
                robotService.processCommand("place 1,-1,east report"));
        Assertions.assertEquals(List.of("Output: Illegal place param (-1,4,EAST)", "Output: ROBOT MISSING"),
                robotService.processCommand("place -1,4,east report"));
        Assertions.assertEquals(List.of("Output: Illegal place param (5,1,EAST)", "Output: ROBOT MISSING"),
                robotService.processCommand("place 5,1,east report"));
    }
}

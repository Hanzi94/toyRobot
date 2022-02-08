package com.challenge.robot.service;

import com.challenge.robot.model.Direction;
import com.challenge.robot.model.TurnDirection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@SpringBootTest
class RobotServiceTest {
    @Autowired
    RobotService robotService;
    @Test
    void testMovement(){
        final HashMap<String, String> output = new HashMap<>();
        final HashMap<String, String> error = new HashMap<>();
        int x1;
        int y1;
        String key;
        // test all movement cases
        for(int x = RobotService.MIN_VALUE; x <= RobotService.MAX_VALUE; x++){
            for(int y = RobotService.MIN_VALUE; y <= RobotService.MAX_VALUE; y++){
                for (Direction dir : Direction.values()) {
                    x1 = x;
                    y1 = y;
                    key = x + "," + y + "," + dir;
                    switch (dir){
                        case NORTH -> {
                            if(y < RobotService.MAX_VALUE){
                                 y1++;
                            }
                            else{
                                error.put(key, "Command Index: 2, " + RobotService.OUT_OF_BOUNDS_CMD);
                            }
                           output.put(key,  x + "," + y1 + "," + dir);
                        }
                        case SOUTH -> {
                            if(y > RobotService.MIN_VALUE){
                                y1--;
                            }
                            else{
                                error.put(key, "Command Index: 2, " + RobotService.OUT_OF_BOUNDS_CMD);
                            }
                            output.put(key,  x + "," + y1 + "," + dir);
                        }
                        case EAST -> {
                            if(x < RobotService.MAX_VALUE){
                                x1++;
                            }
                            else{
                                error.put(key, "Command Index: 2, " + RobotService.OUT_OF_BOUNDS_CMD);
                            }
                            output.put(key,  x1 + "," + y + "," + dir);
                        }
                        case WEST -> {
                            if(x > RobotService.MIN_VALUE){
                                x1--;
                            }
                            else{
                                error.put(key, "Command Index: 2, " + RobotService.OUT_OF_BOUNDS_CMD);
                            }
                            output.put(key,  x1 + "," + y + "," + dir);
                        }
                    }
                }
            }
        }
        for (Map.Entry<String, String> entry : output.entrySet()) {
            key = entry.getKey();
            Map<String, List<String>> map = robotService.processCommand("Place " + key + " move report");
            Assertions.assertEquals(List.of(entry.getValue()), map.get(RobotService.OUTPUT));
            if(error.containsKey(key)){
                Assertions.assertEquals(List.of(error.get(key)), map.get(RobotService.ERROR));
            }
        }
    }
    @Test
    void testTurn(){
        final HashMap<String, String> turnCases = new HashMap<>();
        // test all turning cases
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
            Map<String, List<String>>result = robotService.processCommand(entry.getKey() + " report");
            Assertions.assertEquals(List.of(entry.getValue()), result.get(RobotService.OUTPUT));
            Assertions.assertEquals(0, result.get(RobotService.ERROR).size());
        }
    }

    @Test
    void testMissRobot(){
        // test no place robot report once
        Map<String, List<String>>result = robotService.processCommand("left move report");
        Assertions.assertEquals(List.of("ROBOT MISSING"), result.get(RobotService.OUTPUT));
        Assertions.assertEquals(List.of("LEFT Command Index: 1, " + RobotService.ROBOT_MISSING,
                        "Move Command Index: 2, " + RobotService.ROBOT_MISSING),
                result.get(RobotService.ERROR));

        // test no place robot report twice
        result = robotService.processCommand("Left move report Right report");
        Assertions.assertEquals(List.of("ROBOT MISSING", "ROBOT MISSING"),
                    result.get(RobotService.OUTPUT));
        Assertions.assertEquals(List.of("LEFT Command Index: 1, " + RobotService.ROBOT_MISSING,
                        "Move Command Index: 2, " + RobotService.ROBOT_MISSING,
                        "RIGHT Command Index: 4, " + RobotService.ROBOT_MISSING),
                result.get(RobotService.ERROR));

        // test no place for first and second report but placed for 3td report
        result = robotService.processCommand("left move report right report place 1,1,east report");
        Assertions.assertEquals(List.of("ROBOT MISSING", "ROBOT MISSING", "1,1,EAST"),
                result.get(RobotService.OUTPUT));
        Assertions.assertEquals(List.of("LEFT Command Index: 1, " +RobotService.ROBOT_MISSING,
                        "Move Command Index: 2, " + RobotService.ROBOT_MISSING,
                        "RIGHT Command Index: 4, " + RobotService.ROBOT_MISSING),
                result.get(RobotService.ERROR));

    }

    @Test
    void testNoReport(){
        // place, turn, and move but no report
        Map<String, List<String>>result = robotService.processCommand("place 1,1,east left move");
        Assertions.assertEquals(List.of(), result.get(RobotService.OUTPUT));
        Assertions.assertEquals(List.of(), result.get(RobotService.ERROR));

        // no place for first report and no report after place, turn and move
        result = robotService.processCommand("left move right report place 1,1,east left move");
        Assertions.assertEquals(List.of("ROBOT MISSING"), result.get(RobotService.OUTPUT));
        Assertions.assertEquals( List.of("LEFT Command Index: 1, " + RobotService.ROBOT_MISSING,
                        "Move Command Index: 2, " + RobotService.ROBOT_MISSING,
                        "RIGHT Command Index: 3, " + RobotService.ROBOT_MISSING),
                result.get(RobotService.ERROR));
    }

    @Test
    void testErrorPlace(){
        String command;
        // test place face error
        command = "place 1,1,xrest report";
        Map<String, List<String>>result = robotService.processCommand(command);
        Assertions.assertEquals(List.of("ROBOT MISSING"), result.get(RobotService.OUTPUT));
        Assertions.assertEquals(List.of("Command Index: 1, Illegal place param (1,1,XREST)"),
                result.get(RobotService.ERROR));

        // test x < minimum
        command = "place -1,4,east report";
        result = robotService.processCommand(command);
        Assertions.assertEquals(List.of("ROBOT MISSING"), result.get(RobotService.OUTPUT));
        Assertions.assertEquals(List.of("Command Index: 1, Illegal place param (-1,4,EAST)"),
                result.get(RobotService.ERROR));

        // test x > maximum
        command = "place 5,1,east report";
        result = robotService.processCommand(command);
        Assertions.assertEquals(List.of("ROBOT MISSING"), result.get(RobotService.OUTPUT));
        Assertions.assertEquals(List.of("Command Index: 1, Illegal place param (5,1,EAST)"),
                result.get(RobotService.ERROR));

        // test y < minimum
        command = "place 1,-1,east report";
        result = robotService.processCommand(command);
        Assertions.assertEquals(List.of("ROBOT MISSING"), result.get(RobotService.OUTPUT));
        Assertions.assertEquals(List.of("Command Index: 1, Illegal place param (1,-1,EAST)"),
                result.get(RobotService.ERROR));
        // test y > maximum
        command = "place 1,5,east report";
        result = robotService.processCommand(command);
        Assertions.assertEquals(List.of("ROBOT MISSING"), result.get(RobotService.OUTPUT));
        Assertions.assertEquals(List.of("Command Index: 1, Illegal place param (1,5,EAST)"),
                result.get(RobotService.ERROR));

    }

    @Test
    void testIllegalCommand(){
        // all illegal commands
        String command = "report1 edit get size";
        Map<String, List<String>>result = robotService.processCommand(command);
        List<String> commands = List.of(command.split("\\s+"));
        Assertions.assertEquals(List.of(), result.get(RobotService.OUTPUT));
        Assertions.assertEquals(IntStream.range(0, commands.size())
                        .mapToObj(i->{
                            int j = i+1;
                            return "Command Index: " + j + ", Illegal command (" + commands.get(i).toUpperCase() + ")";
                        }).toList(),
                result.get(RobotService.ERROR));
        // part of illegal commands
        command = "report1 place 1,1,WEST report";
        result = robotService.processCommand(command);
        Assertions.assertEquals(List.of("1,1,WEST"), result.get(RobotService.OUTPUT));
        Assertions.assertEquals(List.of("Command Index: 1, Illegal command (REPORT1)"),
                result.get(RobotService.ERROR));
    }
}

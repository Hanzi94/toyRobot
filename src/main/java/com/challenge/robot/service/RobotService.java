package com.challenge.robot.service;

import com.challenge.robot.model.Direction;
import com.challenge.robot.model.Robot;
import com.challenge.robot.model.TurnDirection;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RobotService {
    public static final int MAX_VALUE = 4;
    public static final int MIN_VALUE = 0;
    public static final String OUTPUT = "Output";
    public static final String ERROR = "Error";
    public static final String OUT_OF_BOUNDS_CMD = "Move action will cause falling off, ignored";
    public static final String ROBOT_MISSING = "Robot Missing";
    private static final String COMMAND_IDX = "Command Index: ";
    private static final List<Direction>DIRECTIONS
            = List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
    private static final Pattern PLACE_PARAM_PATTERN
            = Pattern.compile("^([0-4])\\s*,\\s*([0-4])\\s*,\\s*(" +
            Direction.NORTH + "|" + Direction.SOUTH + "|" + Direction.WEST + "|" + Direction.EAST
            + ")$");

    /**
     * parse the set of commands split with white and call all legal commands
     * @param commands a string contains a set of commands
     * @return list of the robot's state for report commands
     */
    public synchronized Map<String, List<String>> processCommand(String commands){
        Robot robot = null;
        Map<String, List<String>> report = new HashMap<>();
        report.put(OUTPUT, new ArrayList<>());
        report.put(ERROR, new ArrayList<>());

        boolean lastPlace = false;
        int index = 0;
        for (String command : commands.trim().toUpperCase().split("\\s+")) {
            index ++;
            switch (command) {
                case "PLACE":
                    lastPlace = true;
                    break;
                case "REPORT":
                    lastPlace = false;
                    report.get(OUTPUT).add(report(robot));
                    break;
                case "LEFT":
                    lastPlace = false;
                    turn(robot, TurnDirection.LEFT, report.get(ERROR), index);
                    break;
                case "RIGHT":
                    lastPlace = false;
                    turn(robot, TurnDirection.RIGHT, report.get(ERROR), index);
                    break;
                case "MOVE":
                    lastPlace = false;
                    move(robot, report.get(ERROR), index);
                    break;
                default:
                    if (lastPlace) {
                        index --;
                        lastPlace = false;
                        try {
                            robot = place(command);
                        } catch (IllegalArgumentException exp) {
                            report.get(ERROR).add(COMMAND_IDX + index + ", Illegal place param (" + command + ")");
                        }
                    }
                    else{
                        report.get(ERROR).add(COMMAND_IDX + index + ", Illegal command (" + command + ")");
                    }
                    break;
            }
        }
        return report;
    }

    /**
     * put the toy robot on the table in position X,Y and facing NORTH, SOUTH, EAST or WEST.
     * @param param the param with format "x,y,facing"
     * @return the placed robot on the table
     */
    private Robot place(String param) throws IllegalArgumentException{
        if(param == null || param.isBlank()){
            throw new IllegalArgumentException();
        }
        Matcher matcher = PLACE_PARAM_PATTERN.matcher(param);
        if(!matcher.find()){
            throw new IllegalArgumentException();
        }
        return new Robot(
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)),
                Direction.valueOf(matcher.group(3))
                );
    }

    /**
     * announce the X,Y and F of the robot
     * @param robot The reported robot
     * @return The string contains the X,Y and F of the robot
     */
    private String report(Robot robot){
        if(robot == null){
            return "ROBOT MISSING";
        }
        return robot.toString();
    }

    /**
     * rotate the robot 90 degrees to LEFT or RIGHT in the specified direction without changing the position of the robot
     * @param robot The robot will be rotated
     * @param turnDir The turning direction
     */
    private void turn(Robot robot, TurnDirection turnDir, List<String>error, int commandIndex){
        if(robot != null){
            int index = turnDir == TurnDirection.LEFT
                    ? DIRECTIONS.indexOf(robot.getFace()) - 1
                    : DIRECTIONS.indexOf(robot.getFace()) + 1;
            if(index < 0){
                index = DIRECTIONS.size() - 1;
            }
            else if(index > DIRECTIONS.size() - 1){
                index = 0;
            }
            robot.setFace(DIRECTIONS.get(index));
        }
        else{
            error.add(turnDir + " Command " + "Index: " + commandIndex + ", " + ROBOT_MISSING);
        }
    }

    /**
     * Move the toy robot one unit forward in the direction it is currently facing
     * @param robot the robot will be moved
     * @param error the error message buffer
     */
    private void move(Robot robot, List<String>error, int index){
        if(robot != null){
            int x = robot.getX();
            int y = robot.getY();
            switch(robot.getFace()){
                case NORTH -> y ++;
                case SOUTH -> y --;
                case EAST -> x ++;
                case WEST -> x --;
            }
            if(x >= MIN_VALUE && x <= MAX_VALUE){
                robot.setX(x);
            }
            else{
                error.add(COMMAND_IDX + index + ", " + OUT_OF_BOUNDS_CMD);
            }
            if(y >= MIN_VALUE && y <= MAX_VALUE){
                robot.setY(y);
            }
            else{
                error.add(COMMAND_IDX + index + ", " + OUT_OF_BOUNDS_CMD);
            }
        }
        else{
            error.add("Move Command Index: " + index + ", " + ROBOT_MISSING);
        }
    }
}

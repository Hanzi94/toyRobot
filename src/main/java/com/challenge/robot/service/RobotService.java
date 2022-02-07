package com.challenge.robot.service;

import com.challenge.robot.model.Direction;
import com.challenge.robot.model.Robot;
import com.challenge.robot.model.TurnDirection;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RobotService {
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
    public synchronized List<String> processCommand(String commands){
        Robot robot = null;
        List<String> report = new ArrayList<>();
        boolean lastPlace = false;
        for (String command : commands.trim().toUpperCase().split("\\s+")) {
            switch (command) {
                case "PLACE":
                    lastPlace = true;
                    break;
                case "REPORT":
                    lastPlace = false;
                    report.add(report(robot));
                    break;
                case "LEFT":
                    lastPlace = false;
                    turn(robot, TurnDirection.LEFT);
                    break;
                case "RIGHT":
                    lastPlace = false;
                    turn(robot, TurnDirection.RIGHT);
                    break;
                case "MOVE":
                    lastPlace = false;
                    move(robot);
                    break;
                default:
                    if (lastPlace) {
                        lastPlace = false;
                        try {
                            robot = place(command);
                        } catch (IllegalArgumentException exp) {
                            report.add("Output: Illegal place param (" + command + ")");
                        }
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
            return "Output: ROBOT MISSING";
        }
        return "Output: " + robot;
    }

    /**
     * rotate the robot 90 degrees to LEFT or RIGHT in the specified direction without changing the position of the robot
     * @param robot The robot will be rotated
     * @param turnDir The turning direction
     */
    private void turn(Robot robot, TurnDirection turnDir){
        if(robot != null && turnDir != null){
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
    }

    /**
     * Move the toy robot one unit forward in the direction it is currently facing
     * @param robot the robot will be moved
     */
    private void move(Robot robot){
        if(robot != null){
            switch(robot.getFace()){
                case NORTH -> robot.setY(robot.getY() + 1);
                case SOUTH -> robot.setY(robot.getY() - 1);
                case EAST -> robot.setX(robot.getX() + 1);
                case WEST -> robot.setX(robot.getX() - 1);
            }
        }
    }
}

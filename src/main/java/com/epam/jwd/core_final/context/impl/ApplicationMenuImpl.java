package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.context.ApplicationMenu;
import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.NonUniqueEntityNameException;
import com.epam.jwd.core_final.factory.EntityFactory;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.service.CrewService;
import com.epam.jwd.core_final.service.MissionService;
import com.epam.jwd.core_final.service.SpaceshipService;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import com.epam.jwd.core_final.service.impl.MissionServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceshipServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApplicationMenuImpl implements ApplicationMenu {

    private static ApplicationMenuImpl instance;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationMenuImpl.class);

    private static final String MAIN_MENU = "Main menu:\n" +
            "1. Show all spaceships\n" +
            "2. Show all crew members\n" +
            "3. Show all flight missions\n" +
            "4. Edit spaceships\n" +
            "5. Edit crew members\n" +
            "6. Flight missions menu\n" +
            "7. Help\n" +
            "8. Exit\n";

    private static final String FLIGHT_MISSIONS_MENU = "Flight missions menu:\n" +
            "1. Create flight mission\n" +
            "2. Edit flight missions\n" +
            "3. Export flight missions to file in Json format\n" +
            "4. Back to previous menu\n" +
            "5. Help\n" +
            "6. Exit\n";

    private static final String EDIT_FLIGHT_MISSION_MENU = "Edit spaceship menu:\n" +
            "1. Set new mission status\n" +
            "2. Back to previous menu\n" +
            "3. Help\n" +
            "4. Exit\n";

    private static final String EDIT_SPACESHIP_MENU = "Edit spaceship menu:\n" +
            "1. Set new distance\n" +
            "2. Change status readyForNextMission\n" +
            "3. Back to previous menu\n" +
            "4. Help\n" +
            "5. Exit\n";

    private static final String EDIT_CREW_MEMBER_MENU = "Edit crew member menu:\n" +
            "1. Set new rank\n" +
            "2. Change status readyForNextMission\n" +
            "3. Back to previous menu\n" +
            "4. Help\n" +
            "5. Exit\n";

    private final Scanner scanner = new Scanner(System.in);
    private final CrewService crewService = CrewServiceImpl.createInstance();
    private final SpaceshipService spaceshipService = SpaceshipServiceImpl.createInstance();
    private final MissionService missionService = MissionServiceImpl.createInstance();
    private final EntityFactory<FlightMission> entityFactory = FlightMissionFactory.createInstance();
    private final String fileName = NassaContext.getApplicationProperties().getMissionsFileName();
    private final String fileDirectory = NassaContext.getApplicationProperties().getOutputRootDir();
    private final DateTimeFormatter dateTimeFormatter = NassaContext.getApplicationProperties().getDateTimeFormatter();

    private ApplicationMenuImpl() {

    }

    public static ApplicationMenuImpl createInstance() {
        if (instance == null) {
            instance = new ApplicationMenuImpl();
        }
        return instance;
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return NassaContext.createInstance();
    }

    @Override
    public void printAvailableOptions() {
        System.out.println("Welcome to App!\n");
        boolean logic = true;
        while (logic) {
            System.out.println(MAIN_MENU);
            String command = handleUserInput("Enter command: ");
            switch (command.toLowerCase()) {
                case ("1"):
                case ("show all spaceships"): {
                    spaceshipService.findAllSpaceships().forEach(System.out::println);
                    break;
                }
                case ("2"):
                case ("show all crew members"): {
                    crewService.findAllCrewMembers().forEach(System.out::println);
                    break;
                }
                case ("3"):
                case ("show all flight missions"): {
                    missionService.findAllMissions().forEach(System.out::println);
                    break;
                }
                case ("4"):
                case ("edit spaceships"): {
                    List<Spaceship> allSpaceshipsFromContext = spaceshipService.findAllSpaceships();
                    if (!allSpaceshipsFromContext.isEmpty()) {
                        System.out.println("All spaceships from context:");
                        int i = 1;
                        for (Spaceship spaceship : allSpaceshipsFromContext) {
                            System.out.println(String.format("%d. %s", i++, spaceship));
                        }
                        String number = handleUserInput("Choose the number of spaceship for edit: ");
                        try {
                            int value = Integer.parseInt(number);
                            if (value > 0 && value <= allSpaceshipsFromContext.size()) {
                                editSpaceshipMenu(allSpaceshipsFromContext.get(value - 1));
                            } else {
                                System.out.println("The selected spaceship does not exist");
                            }
                        } catch (NumberFormatException e) {
                            LOGGER.warn("Incorrect number input");
                            System.out.println("The input number of spaceship format is incorrect");
                        }
                    } else {
                        System.out.println("Spaceships context is empty");
                    }
                    break;
                }
                case ("5"):
                case ("edit crew members"): {
                    List<CrewMember> allCrewMembersFromContext = crewService.findAllCrewMembers();
                    if (!allCrewMembersFromContext.isEmpty()) {
                        System.out.println("All crew members from context:");
                        int i = 1;
                        for (CrewMember crewMember : allCrewMembersFromContext) {
                            System.out.println(String.format("%d. %s", i++, crewMember));
                        }
                        String number = handleUserInput("Choose the number of crew member for edit: ");
                        try {
                            int value = Integer.parseInt(number);
                            if (value > 0 && value <= allCrewMembersFromContext.size()) {
                                editCrewMemberMenu(allCrewMembersFromContext.get(value - 1));
                            } else {
                                System.out.println("The selected crew member does not exist");
                            }
                        } catch (NumberFormatException e) {
                            LOGGER.warn("Incorrect number input");
                            System.out.println("The input number of crew member format is incorrect");
                        }
                    } else {
                        System.out.println("Crew members context is empty");
                    }
                    break;
                }
                case ("6"):
                case ("flight missions menu"): {
                    flightMissionsMenu();
                    break;
                }
                case ("7"):
                case ("help"): {
                    System.out.println("Description of all commands:\n" +
                            "1. Show all spaceships - show all spaceship from context.\n" +
                            "2. Show all crew members - show all crew members from context.\n" +
                            "3. Show all flight missions - show all flight missions from context.\n" +
                            "4. Edit spaceships - go to edit spaceships menu.\n" +
                            "5. Edit crew members - go to edit crew members menu.\n" +
                            "6. Flight missions menu - go to flight missions menu.\n" +
                            "7. Help - show description of all commands.\n" +
                            "8. Exit - application shutdown.\n" +
                            "Commands can be entered case insensitive or using numbers.\n");
                    break;
                }
                case ("8"):
                case ("exit"): {
                    logic = false;
                    break;
                }
                default: {
                    System.out.println("Such a console command does not exist. For help, type help in the console.");
                }
            }
        }
    }

    @Override
    public String handleUserInput(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    private void editSpaceshipMenu(Spaceship spaceship) {
        boolean logic = true;
        while (logic) {
            System.out.println(EDIT_SPACESHIP_MENU);
            String command = handleUserInput("Enter command: ");
            switch (command.toLowerCase()) {
                case ("1"):
                case ("set new distance"): {
                    String number = handleUserInput("Enter new distance: ");
                    try {
                        Long newValue = Long.valueOf(number);
                        if (newValue > 0) {
                            spaceship.setFlightDistance(newValue);
                            spaceshipService.updateSpaceshipDetails(spaceship);
                            System.out.println("Spaceship was updated");
                        }
                        else {
                            System.out.println("Distance must be > 0");
                        }
                    } catch (NumberFormatException e) {
                        LOGGER.warn("Incorrect number input");
                        System.out.println("The input number of distance format is incorrect");
                    }
                    break;
                }
                case ("2"):
                case ("change status readyForNextMission"): {
                    FlightMissionCriteria flightMissionCriteria = FlightMissionCriteria.builder()
                            .whereAssignedSpaceship(spaceship)
                            .build();
                    List<FlightMission> flightMissions = missionService.findAllMissionsByCriteria(flightMissionCriteria);
                    if (spaceship.getReadyForNextMission()) {
                        spaceshipService.assignSpaceshipOnMission(spaceship);
                        spaceshipService.updateSpaceshipDetails(spaceship);
                        System.out.println("Spaceship was updated");
                    } else {
                        Optional<FlightMission> foundFailedMission = flightMissions.stream().filter(flightMission ->
                                flightMission.getMissionResult() == MissionResult.FAILED
                                        || flightMission.getMissionResult() == MissionResult.IN_PROGRESS
                                        || flightMission.getMissionResult() == MissionResult.PLANNED).findFirst();
                        if (foundFailedMission.isPresent()) {
                            System.out.println("Unable to change status. This spaceship has already failed," +
                                    " is participating, or is scheduled for a mission.");
                        } else {
                            spaceship.setReadyForNextMission(true);
                            spaceshipService.updateSpaceshipDetails(spaceship);
                            System.out.println("Spaceship was updated");
                        }
                    }
                    break;
                }
                case ("3"):
                case ("back to previous menu"): {
                    logic = false;
                    break;
                }
                case ("4"):
                case ("help"): {
                    System.out.println("Description of all commands:\n" +
                            "1. Set new distance - set new distance to spaceship.\n" +
                            "2. Change status readyForNextMission - change status readyForNextMission to the opposite.\n" +
                            "3. Back to previous menu - go to previous menu.\n" +
                            "4. Help - show description of all commands.\n" +
                            "5. Exit - application shutdown.\n" +
                            "Commands can be entered case insensitive or using numbers.\n");
                    break;
                }
                case ("5"):
                case ("exit"): {
                    System.exit(-1);
                    break;
                }
                default: {
                    System.out.println("Such a console command does not exist. For help, type help in the console.");
                }
            }
        }
    }

    private void editCrewMemberMenu(CrewMember crewMember) {
        boolean logic = true;
        while (logic) {
            System.out.println(EDIT_CREW_MEMBER_MENU);
            String command = handleUserInput("Enter command: ");
            switch (command.toLowerCase()) {
                case ("1"):
                case ("Set new rank"): {
                    Rank[] ranks = Rank.values();
                    System.out.println("All available ranks:");
                    int i = 1;
                    for (Rank rank : ranks) {
                        System.out.println(String.format("%d. %s", i++, rank));
                    }
                    String number = handleUserInput("Choose the number of rank for setting: ");
                    try {
                        int newValue = Integer.parseInt(number);
                        if (newValue > 0 && newValue <= ranks.length) {
                            crewMember.setMemberRank(ranks[newValue - 1]);
                            crewService.updateCrewMemberDetails(crewMember);
                            System.out.println("Crew member was updated");
                        }
                        else {
                            System.out.println("The selected rank does not exist");
                        }
                    } catch (NumberFormatException e) {
                        LOGGER.warn("Incorrect number input");
                        System.out.println("The input number of rank format is incorrect");
                    }
                    break;
                }
                case ("2"):
                case ("change status readyForNextMission"): {
                    if (crewMember.getReadyForNextMissions()) {
                        crewService.assignCrewMemberOnMission(crewMember);
                        crewService.updateCrewMemberDetails(crewMember);
                        System.out.println("Crew member was updated");
                    } else {
                        List<FlightMission> flightMissions = missionService.findAllMissions();
                        Stream<FlightMission> stream = flightMissions.stream()
                                .filter(flightMission -> flightMission.getAssignedCrew().contains(crewMember));
                        Optional<FlightMission> foundFailedMission = stream.filter(flightMission ->
                                flightMission.getMissionResult() == MissionResult.FAILED
                                        || flightMission.getMissionResult() == MissionResult.IN_PROGRESS
                                        || flightMission.getMissionResult() == MissionResult.PLANNED).findFirst();
                        if (foundFailedMission.isPresent()) {
                            System.out.println("Unable to change status. This crew member has already been assigned" +
                                    " to a failed, planned, ongoing mission.");
                        } else {
                            crewMember.setReadyForNextMissions(true);
                            crewService.updateCrewMemberDetails(crewMember);
                            System.out.println("Crew member was updated");
                        }
                    }
                    break;
                }
                case ("3"):
                case ("back to previous menu"): {
                    logic = false;
                    break;
                }
                case ("4"):
                case ("help"): {
                    System.out.println("Description of all commands:\n" +
                            "1. Set new rank - set new rank to crew member.\n" +
                            "2. Change status readyForNextMission - change status readyForNextMission to the opposite.\n" +
                            "3. Back to previous menu - go to previous menu.\n" +
                            "4. Help - show description of all commands.\n" +
                            "5. Exit - application shutdown.\n" +
                            "Commands can be entered case insensitive or using numbers.\n");
                    break;
                }
                case ("5"):
                case ("exit"): {
                    System.exit(-1);
                    break;
                }
                default: {
                    System.out.println("Such a console command does not exist. For help, type help in the console.");
                }
            }
        }
    }

    public void flightMissionsMenu() {
        boolean logic = true;
        while (logic) {
            System.out.println(FLIGHT_MISSIONS_MENU);
            String command = handleUserInput("Enter command: ");
            switch (command.toLowerCase()) {
                case ("1"):
                case ("create flight mission"): {
                    String inputMissionName = handleUserInput("Enter mission name: ");
                    String inputMissionStartDateTime = handleUserInput(
                            "Enter mission start date time in format yyyy-MM-dd HH:mm:ss: ");
                    String inputMissionEndDateTime = handleUserInput(
                            "Enter mission end date time in format yyyy-MM-dd HH:mm:ss: ");
                    String inputMissionDistance = handleUserInput("Enter mission distance: ");
                    try {
                        Long missionDistance = Long.valueOf(inputMissionDistance);
                        if (missionDistance > 0) {
                            LocalDateTime missionStart = LocalDateTime.parse(inputMissionStartDateTime, dateTimeFormatter);
                            LocalDateTime missionEnd = LocalDateTime.parse(inputMissionEndDateTime, dateTimeFormatter);
                            missionService.createMission(entityFactory.create(inputMissionName, missionStart, missionEnd,
                                    missionDistance, MissionResult.PLANNED));
                        } else {
                            System.out.println("Mission distance must be > 0");
                        }
                    } catch (DateTimeParseException | NumberFormatException | NonUniqueEntityNameException e) {
                        LOGGER.warn("Error of input data in mission creation", e);
                        System.out.println("Invalid input in mission creation");
                    }
                }
                case ("2"):
                case ("edit flight missions"): {
                    List<FlightMission> allFlightMissionsFromContext = missionService.findAllMissions();
                    if (!allFlightMissionsFromContext.isEmpty()) {
                        System.out.println("All flight missions from context:");
                        int i = 1;
                        for (FlightMission flightMission : allFlightMissionsFromContext) {
                            System.out.println(String.format("%d. %s", i++, flightMission));
                        }
                        String number = handleUserInput("Choose the number of flight mission for edit: ");
                        try {
                            int value = Integer.parseInt(number);
                            if (value > 0 && value <= allFlightMissionsFromContext.size()) {
                                editFlightMissionMenu(allFlightMissionsFromContext.get(value - 1));
                            } else {
                                System.out.println("The selected flight mission does not exist");
                            }
                        } catch (NumberFormatException e) {
                            LOGGER.warn("Incorrect number input");
                            System.out.println("The input number of flight mission format is incorrect");
                        }
                    } else {
                        System.out.println("Flight mission context is empty");
                    }
                }
                case ("3"):
                case ("export flight missions to file in Json format"): {
                    List<FlightMission> flightMissions = missionService.findAllMissions();
                    File filePath = new File(String.format("src/main/resources/%s", fileDirectory));
                    filePath.mkdirs();
                    File file = new File(filePath + "/" + fileName + ".json");
                    try (FileWriter fileWriter = new FileWriter(file)) {
                        if (!flightMissions.isEmpty()) {
                            fileWriter.append("[");
                            List<String> strings = flightMissions.stream().map(flightMission -> {
                                try {
                                    return flightMission.convertToJson();
                                } catch (JsonProcessingException e) {
                                    LOGGER.error("Json serialization flight mission object error");
                                }
                                return null;
                            }).collect(Collectors.toList());
                            String result = String.join(",\r\n", strings);
                            fileWriter.append(result);
                            fileWriter.append("]\r\n");
                        }
                        fileWriter.flush();
                    } catch (IOException e) {
                        LOGGER.error("File not found or json serialization error", e);
                    }
                    break;
                }
                case ("4"):
                case ("back to previous menu"): {
                    logic = false;
                    break;
                }
                case ("5"):
                case ("help"): {
                    System.out.println("Description of all commands:\n" +
                            "1. Create flight mission - create flight mission.\n" +
                            "2. Edit flight missions - edit flight mission.\n" +
                            "3. Export flight missions to file in Json format - export to Json.\n" +
                            "4. Back to previous menu - go to previous menu.\n" +
                            "5. Help - show description of all commands.\n" +
                            "6. Exit - application shutdown.\n" +
                            "Commands can be entered case insensitive or using numbers.\n");
                    break;
                }
                case ("6"):
                case ("exit"): {
                    System.exit(-1);
                    break;
                }
                default: {
                    System.out.println("Such a console command does not exist. For help, type help in the console.");
                }
            }
        }
    }

    private void editFlightMissionMenu(FlightMission flightMission) {
        boolean logic = true;
        while (logic) {
            System.out.println(EDIT_FLIGHT_MISSION_MENU);
            String command = handleUserInput("Enter command: ");
            switch (command.toLowerCase()) {
                case ("1"):
                case ("Set new mission status"): {
                    MissionResult[] missionStatuses = MissionResult.values();
                    System.out.println("All available mission statuses:");
                    int i = 1;
                    for (MissionResult missionStatus : missionStatuses) {
                        System.out.println(String.format("%d. %s", i++, missionStatus));
                    }
                    String number = handleUserInput("Choose the number of mission status for setting: ");
                    try {
                        int newValue = Integer.parseInt(number);
                        if (newValue > 0 && newValue <= missionStatuses.length) {
                            flightMission.setMissionResult(missionStatuses[newValue - 1]);
                            missionService.updateSpaceshipDetails(flightMission);
                            System.out.println("Flight mission was updated");
                        }
                        else {
                            System.out.println("The selected mission status does not exist");
                        }
                    } catch (NumberFormatException e) {
                        LOGGER.warn("Incorrect number input");
                        System.out.println("The input number of mission status format is incorrect");
                    }
                    break;
                }
                case ("2"):
                case ("back to previous menu"): {
                    logic = false;
                    break;
                }
                case ("3"):
                case ("help"): {
                    System.out.println("Description of all commands:\n" +
                            "1. Set new mission status - set new mission status to flight mission.\n" +
                            "2. Back to previous menu - go to previous menu.\n" +
                            "3. Help - show description of all commands.\n" +
                            "4. Exit - application shutdown.\n" +
                            "Commands can be entered case insensitive or using numbers.\n");
                    break;
                }
                case ("4"):
                case ("exit"): {
                    System.exit(-1);
                    break;
                }
                default: {
                    System.out.println("Such a console command does not exist. For help, type help in the console.");
                }
            }
        }
    }
}

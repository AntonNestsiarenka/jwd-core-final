package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.domain.BaseEntity;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.InvalidStateException;
import com.epam.jwd.core_final.exception.UnknownEntityException;
import com.epam.jwd.core_final.factory.impl.CrewMemberFactory;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.factory.impl.SpaceshipFactory;
import com.epam.jwd.core_final.util.PropertyReaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// todo
public class NassaContext implements ApplicationContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(NassaContext.class);
    private static final ApplicationProperties APPLICATION_PROPERTIES = (ApplicationProperties) PropertyReaderUtil.
            getProperties().get("appProperties");
    private static NassaContext instance;

    // no getters/setters for them
    private final Collection<CrewMember> crewMembers = new ArrayList<>();
    private final Collection<Spaceship> spaceships = new ArrayList<>();
    private final Collection<FlightMission> flightMissions = new ArrayList<>();


    private NassaContext() {

    }

    public static NassaContext createInstance() {
        if (instance == null) {
            instance = new NassaContext();
        }
        return instance;
    }

    @Override
    public <T extends BaseEntity> Collection<T> retrieveBaseEntityList(Class<T> tClass) {
        if (tClass == CrewMember.class) {
            return (Collection<T>) crewMembers;
        }
        else if (tClass == Spaceship.class) {
            return (Collection<T>) spaceships;
        }
        else if (tClass == FlightMission.class) {
            return (Collection<T>) flightMissions;
        }
        throw new UnknownEntityException(tClass.getSimpleName());
    }

    /**
     * You have to read input files, populate collections
     * @throws InvalidStateException
     */
    @Override
    public void init() throws InvalidStateException {
        initCrewMembersFromFile();
        initSpaceshipsFromFile();
        generateFlightMissions();
    }

    private void initCrewMembersFromFile() throws InvalidStateException {
        final String fileName = APPLICATION_PROPERTIES.getCrewFilename();
        final String fileDirectory = APPLICATION_PROPERTIES.getInputRootDir();
        String text = readTextFromFile(fileDirectory, fileName);
        Pattern pattern = Pattern.compile("(?<roleId>\\d+),(?<name>[^,]+),(?<rankId>\\d+);");
        Matcher matcher = pattern.matcher(text);
        CrewMemberFactory crewMemberFactory = CrewMemberFactory.createInstance();
        while (matcher.find()) {
            crewMembers.add(crewMemberFactory.create(matcher.group("name"),
                    Role.resolveRoleById(Integer.parseInt(matcher.group("roleId"))),
                    Rank.resolveRankById(Integer.parseInt(matcher.group("rankId")))));
        }
    }

    private void initSpaceshipsFromFile() throws InvalidStateException {
        final String fileName = APPLICATION_PROPERTIES.getSpaceshipsFileName();
        final String fileDirectory = APPLICATION_PROPERTIES.getInputRootDir();
        String text = readTextFromFile(fileDirectory, fileName);
        Pattern pattern = Pattern.compile("(?<name>[^\n\r.#][a-zA-Z\\s-]+);(?<distance>\\d+);" +
                "\\{(?<key1>\\d+):(?<value1>\\d+),(?<key2>\\d+):(?<value2>\\d+),(?<key3>\\d+):(?<value3>\\d+)," +
                "(?<key4>\\d+):(?<value4>\\d+)}\r?\n?");
        Matcher matcher = pattern.matcher(text);
        SpaceshipFactory spaceshipFactory = SpaceshipFactory.createInstance();
        while (matcher.find()) {
            Map<Role, Short> map = new HashMap<>();
            map.put(Role.resolveRoleById(Integer.parseInt(matcher.group("key1"))),
                    Short.valueOf(matcher.group("value1")));
            map.put(Role.resolveRoleById(Integer.parseInt(matcher.group("key2"))),
                    Short.valueOf(matcher.group("value2")));
            map.put(Role.resolveRoleById(Integer.parseInt(matcher.group("key3"))),
                    Short.valueOf(matcher.group("value3")));
            map.put(Role.resolveRoleById(Integer.parseInt(matcher.group("key4"))),
                    Short.valueOf(matcher.group("value4")));
            spaceships.add(spaceshipFactory.create(matcher.group("name"), map,
                    Long.valueOf(matcher.group("distance"))));
        }
    }

    private void generateFlightMissions() throws InvalidStateException {
        Random random = new Random(System.currentTimeMillis());
        final String fileName = APPLICATION_PROPERTIES.getMissionsFileName();
        final String fileDirectory = APPLICATION_PROPERTIES.getInputRootDir();
        String text = readTextFromFile(fileDirectory, fileName);
        FlightMissionFactory flightMissionFactory = FlightMissionFactory.createInstance();
        String[] missionNames = text.split("\r?\n");
        List<String> listOfMissionNames = Arrays.stream(missionNames).collect(Collectors.toList());
        List<Spaceship> allSpaceships = spaceships.stream()
                .filter(Spaceship::getReadyForNextMission).collect(Collectors.toList());
        List<CrewMember> allCrewMembers = crewMembers.stream()
                .filter(CrewMember::getReadyForNextMissions).collect(Collectors.toList());
        if (listOfMissionNames.isEmpty() || allSpaceships.isEmpty() || allCrewMembers.isEmpty()) {
            throw new InvalidStateException("Flight mission generation is not possible." +
                    " Mission names or spaceships or crewMembers is unavailable");
        }
        final int numberOfMissions = 5;
        for (int i = 0; i < numberOfMissions; i++) {
            LocalDateTime missionStart = generateStartDateTimeForFlightMission(random);
            LocalDateTime missionEnd = generateEndDateTimeForFlightMission(random, missionStart);
            Long distance = generateFlightMissionDistance(random);
            try {
                Spaceship spaceship = allSpaceships.get(randInt(random, 0, allSpaceships.size() - 1));
                String missionName = listOfMissionNames.get(randInt(random, 0,
                        listOfMissionNames.size() - 1));
                FlightMission flightMission = flightMissionFactory.create(missionName,
                        missionStart, missionEnd, distance, MissionResult.PLANNED);

                List<CrewMember> assignedCrews = new ArrayList<>();
                Set<Role> setOfRole = spaceship.getCapacityByRole().keySet();
                for (Role role : setOfRole) {
                    List<CrewMember> filteredCrewMembers = allCrewMembers.stream()
                            .filter(crewMember -> crewMember.getMemberRole() == role)
                            .collect(Collectors.toList());
                    for (int j = 0; j < spaceship.getCapacityByRole().get(role); j++) {
                        CrewMember assignedCrewMember = filteredCrewMembers.get(randInt(random, 0,
                                filteredCrewMembers.size() - 1));
                        assignedCrews.add(assignedCrewMember);
                    }
                }
                flightMission.setAssignedSpaceship(spaceship);
                spaceship.setReadyForNextMission(false);
                assignedCrews.forEach(crewMember -> crewMember.setReadyForNextMissions(false));
                flightMission.setAssignedCrew(assignedCrews);
                flightMissions.add(flightMission);
                allCrewMembers.removeAll(assignedCrews);
                allSpaceships.remove(spaceship);
                listOfMissionNames.remove(missionName);
            }
            catch (IndexOutOfBoundsException e) {
                LOGGER.warn("Failed to generate flight mission with current random parameters. Generation will try to" +
                        " generate other mission with next random parameters.");
            }
        }
    }

    private String readTextFromFile(String fileDirectory, String fileName) throws InvalidStateException {
        try (InputStreamReader fileReader = new FileReader(String.format("src/main/resources/%s/%s",
                fileDirectory, fileName))) {
            StringBuilder text = new StringBuilder();
            while (fileReader.ready()) {
                text.append((char) fileReader.read());
            }
            return text.toString();
        } catch (IOException e) {
            throw new InvalidStateException("Reading from file failed", e);
        }
    }

    private long generateFlightMissionDistance(Random random) {
        final long lowOffset = 85462;
        final long highOffset = 998512;
        return randLong(random, lowOffset, highOffset);
    }

    private LocalDateTime generateStartDateTimeForFlightMission(Random random) {
       //  30 min in seconds
       final long lowOffset = 1800;
       // 2 weeks in seconds
       final long highOffset = 1209600;
       return LocalDateTime.now().plusSeconds(randLong(random, lowOffset, highOffset));
    }

    private LocalDateTime generateEndDateTimeForFlightMission(Random random, LocalDateTime missionStart) {
        //  2 hours in seconds
        final long lowOffset = 7200;
        // 2 weeks in seconds
        final long highOffset = 1209600;
        return missionStart.plusSeconds(randLong(random, lowOffset, highOffset));
    }

    private int randInt(Random random, int lowLimit, int highLimit) {
        if (lowLimit > highLimit) {
            int temp = lowLimit;
            lowLimit = highLimit;
            highLimit = temp;
        }
        return lowLimit + Math.abs(random.nextInt()) % (highLimit - lowLimit + 1);
    }

    private long randLong(Random random, long lowLimit, long highLimit) {
        if (lowLimit > highLimit) {
            long temp = lowLimit;
            lowLimit = highLimit;
            highLimit = temp;
        }
        return lowLimit + Math.abs(random.nextLong()) % (highLimit - lowLimit + 1);
    }
}

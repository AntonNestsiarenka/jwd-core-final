package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.domain.BaseEntity;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.InvalidStateException;
import com.epam.jwd.core_final.exception.UnknownEntityException;
import com.epam.jwd.core_final.factory.impl.CrewMemberFactory;
import com.epam.jwd.core_final.factory.impl.SpaceshipFactory;
import com.epam.jwd.core_final.util.PropertyReaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        try (InputStreamReader fileReader = new FileReader(String.format("src/main/resources/%s/%s",
                fileDirectory, fileName))) {
            StringBuilder text = new StringBuilder();
            while (fileReader.ready()) {
                text.append((char) fileReader.read());
            }
            Pattern pattern = Pattern.compile("(?<roleId>\\d+),(?<name>[^,]+),(?<rankId>\\d+);");
            Matcher matcher = pattern.matcher(text);
            CrewMemberFactory crewMemberFactory = CrewMemberFactory.createInstance();
            while (matcher.find()) {
                crewMembers.add(crewMemberFactory.create(matcher.group("name"),
                        Role.resolveRoleById(Integer.parseInt(matcher.group("roleId"))),
                        Rank.resolveRankById(Integer.parseInt(matcher.group("rankId")))));
            }
        } catch (IOException e) {
            throw new InvalidStateException("Initialization of crew members failed", e);
        }
    }

    private void initSpaceshipsFromFile() throws InvalidStateException {
        final String fileName = APPLICATION_PROPERTIES.getSpaceshipsFileName();
        final String fileDirectory = APPLICATION_PROPERTIES.getInputRootDir();
        try (InputStreamReader fileReader = new FileReader(String.format("src/main/resources/%s/%s",
                fileDirectory, fileName))) {
            StringBuilder text = new StringBuilder();
            while (fileReader.ready()) {
                text.append((char) fileReader.read());
            }
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
        } catch (IOException e) {
            throw new InvalidStateException("Initialization of spaceships failed", e);
        }
    }

    private void generateFlightMissions() {
        //will be implemented later
    }
}

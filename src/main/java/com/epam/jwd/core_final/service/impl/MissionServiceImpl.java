package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.exception.NonUniqueEntityNameException;
import com.epam.jwd.core_final.service.MissionService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MissionServiceImpl implements MissionService {

    private static MissionServiceImpl instance;

    private final ApplicationContext applicationContext;

    private MissionServiceImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static MissionServiceImpl createInstance() {
        if (instance == null) {
            instance = new MissionServiceImpl(NassaContext.createInstance());
        }
        return instance;
    }

    @Override
    public List<FlightMission> findAllMissions() {
        return new ArrayList<>(applicationContext.retrieveBaseEntityList(FlightMission.class));
    }

    @Override
    public List<FlightMission> findAllMissionsByCriteria(Criteria<? extends FlightMission> criteria) {
        FlightMissionCriteria flightMissionCriteria = (FlightMissionCriteria) criteria;
        List<FlightMission> allFlightMissions = findAllMissions();
        Stream<FlightMission> filteredStream = filterByCriteria(allFlightMissions, flightMissionCriteria);
        return filteredStream.collect(Collectors.toList());
    }

    @Override
    public Optional<FlightMission> findMissionByCriteria(Criteria<? extends FlightMission> criteria) {
        FlightMissionCriteria flightMissionCriteria = (FlightMissionCriteria) criteria;
        List<FlightMission> allFlightMissions = findAllMissions();
        Stream<FlightMission> filteredStream = filterByCriteria(allFlightMissions, flightMissionCriteria);
        return filteredStream.findFirst();
    }

    @Override
    public FlightMission updateSpaceshipDetails(FlightMission flightMission) {
        return flightMission;
    }

    @Override
    public FlightMission createMission(FlightMission flightMission) {
        List<FlightMission> allFlightMissions = findAllMissions();
        allFlightMissions.forEach(mission -> {
            if (mission.getName().equalsIgnoreCase(flightMission.getName())) {
                throw new NonUniqueEntityNameException("Flight mission with the same name is already in the context");
            }
        });
        return saveFlightMission(flightMission);
    }

    private FlightMission saveFlightMission(FlightMission flightMission) {
        applicationContext.retrieveBaseEntityList(FlightMission.class).add(flightMission);
        return flightMission;
    }

    private Stream<FlightMission> filterByCriteria(List<FlightMission> allFlightMissions,
                                                   FlightMissionCriteria flightMissionCriteria) {
        Stream<FlightMission> stream = allFlightMissions.stream();
        if (flightMissionCriteria.getWhereId() != null) {
            stream = stream.filter(flightMission -> flightMission.getId().equals(flightMissionCriteria.getWhereId()));
        }
        if (flightMissionCriteria.getWhereName() != null) {
            stream = stream.filter(flightMission -> flightMission.getName()
                    .equalsIgnoreCase(flightMissionCriteria.getWhereName()));
        }
        if (flightMissionCriteria.getWhereBeforeStartDate() != null) {
            stream = stream.filter(flightMission -> flightMission.getStartDate()
                    .isBefore(flightMissionCriteria.getWhereBeforeStartDate()));
        }
        if (flightMissionCriteria.getWhereAfterStartDate() != null) {
            stream = stream.filter(flightMission -> flightMission.getStartDate()
                    .isAfter(flightMissionCriteria.getWhereAfterStartDate()));
        }
        if (flightMissionCriteria.getWhereBeforeEndDate() != null) {
            stream = stream.filter(flightMission -> flightMission.getEndDate()
                    .isBefore(flightMissionCriteria.getWhereBeforeEndDate()));
        }
        if (flightMissionCriteria.getWhereAfterEndDate() != null) {
            stream = stream.filter(flightMission -> flightMission.getEndDate()
                    .isAfter(flightMissionCriteria.getWhereAfterEndDate()));
        }
        if (flightMissionCriteria.getWhereLesserDistance() != null) {
            stream = stream.filter(flightMission -> flightMission.getDistance()
                    < flightMissionCriteria.getWhereLesserDistance());
        }
        if (flightMissionCriteria.getWhereGreaterDistance() != null) {
            stream = stream.filter(flightMission -> flightMission.getDistance()
                    > flightMissionCriteria.getWhereGreaterDistance());
        }
        stream = (flightMissionCriteria.getWhereAssignedSpaceship() == null)
                ? stream.filter(flightMission -> flightMission.getAssignedSpaceship() == null)
                : stream.filter(flightMission -> flightMission.getAssignedSpaceship()
                .equals(flightMissionCriteria.getWhereAssignedSpaceship()));
        stream = (flightMissionCriteria.getWhereAssignedCrews() == null)
                ? stream.filter(flightMission -> flightMission.getAssignedCrew() == null)
                : stream.filter(flightMission -> flightMission.getAssignedCrew()
                .equals(flightMissionCriteria.getWhereAssignedCrews()));
        if (flightMissionCriteria.getWhereMissionResult() != null) {
            stream = stream.filter(flightMission -> flightMission.getMissionResult()
                    == flightMissionCriteria.getWhereMissionResult());
        }
        return stream;
    }
}

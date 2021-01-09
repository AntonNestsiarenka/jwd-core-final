package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.exception.NonUniqueEntityNameException;
import com.epam.jwd.core_final.service.MissionService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        // will be implemented later
        return null;
    }

    @Override
    public Optional<FlightMission> findMissionByCriteria(Criteria<? extends FlightMission> criteria) {
        // will be implemented later
        return Optional.empty();
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
}

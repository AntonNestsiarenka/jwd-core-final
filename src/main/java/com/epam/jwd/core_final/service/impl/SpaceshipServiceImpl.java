package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.AssigningEntityOnMissionException;
import com.epam.jwd.core_final.exception.NonUniqueEntityNameException;
import com.epam.jwd.core_final.service.SpaceshipService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class SpaceshipServiceImpl implements SpaceshipService {

    private static SpaceshipServiceImpl instance;

    private final ApplicationContext applicationContext;

    private SpaceshipServiceImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static SpaceshipServiceImpl createInstance() {
        if (instance == null) {
            instance = new SpaceshipServiceImpl(NassaContext.createInstance());
        }
        return instance;
    }

    @Override
    public List<Spaceship> findAllSpaceships() {
        return new ArrayList<>(applicationContext.retrieveBaseEntityList(Spaceship.class));
    }

    @Override
    public List<Spaceship> findAllSpaceshipsByCriteria(Criteria<? extends Spaceship> criteria) {
        // will be implemented later
        return null;
    }

    @Override
    public Optional<Spaceship> findSpaceshipByCriteria(Criteria<? extends Spaceship> criteria) {
        // will be implemented later
        return Optional.empty();
    }

    @Override
    public Spaceship updateSpaceshipDetails(Spaceship spaceship) {
        // ???
        return null;
    }

    @Override
    public void assignSpaceshipOnMission(Spaceship spaceship) throws RuntimeException {
        Collection<FlightMission> allFlightMissions = applicationContext.retrieveBaseEntityList(FlightMission.class);
        allFlightMissions.forEach(flightMission -> {
            if (flightMission.getAssignedSpaceship().equals(spaceship)) {
                throw new AssigningEntityOnMissionException(Spaceship.class.getSimpleName());
            }
        });
        spaceship.setReadyForNextMission(false);
    }

    @Override
    public Spaceship createSpaceship(Spaceship spaceship) throws RuntimeException {
        List<Spaceship> allSpaceships = findAllSpaceships();
        allSpaceships.forEach(spaceshipFromContext -> {
            if (spaceshipFromContext.getName().equalsIgnoreCase(spaceship.getName())) {
                throw new NonUniqueEntityNameException("Spaceship with the same name is already in the context");
            }
        });
        return saveSpaceship(spaceship);
    }

    private Spaceship saveSpaceship(Spaceship spaceship) {
        applicationContext.retrieveBaseEntityList(Spaceship.class).add(spaceship);
        return spaceship;
    }
}

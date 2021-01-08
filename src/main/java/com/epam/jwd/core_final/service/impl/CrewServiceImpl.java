package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.exception.AssigningEntityOnMissionException;
import com.epam.jwd.core_final.exception.NonUniqueEntityNameException;
import com.epam.jwd.core_final.service.CrewService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CrewServiceImpl implements CrewService {

    private static CrewServiceImpl instance;

    private final ApplicationContext applicationContext;

    private CrewServiceImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static CrewServiceImpl createInstance() {
        if (instance == null) {
            instance = new CrewServiceImpl(NassaContext.createInstance());
        }
        return instance;
    }

    @Override
    public List<CrewMember> findAllCrewMembers() {
        return new ArrayList<>(applicationContext.retrieveBaseEntityList(CrewMember.class));
    }

    @Override
    public List<CrewMember> findAllCrewMembersByCriteria(Criteria<? extends CrewMember> criteria) {
        // will be implemented later
        return null;
    }

    @Override
    public Optional<CrewMember> findCrewMemberByCriteria(Criteria<? extends CrewMember> criteria) {
        // will be implemented later
        return Optional.empty();
    }

    @Override
    public CrewMember updateCrewMemberDetails(CrewMember crewMember) {
        // ???
        return null;
    }

    @Override
    public void assignCrewMemberOnMission(CrewMember crewMember) throws RuntimeException {
        Collection<FlightMission> allFlightMissions = applicationContext.retrieveBaseEntityList(FlightMission.class);
        allFlightMissions.forEach(flightMission -> {
            if (flightMission.getAssignedCrew().contains(crewMember)) {
                throw new AssigningEntityOnMissionException(CrewMember.class.getSimpleName());
            }
        });
        crewMember.setReadyForNextMissions(false);
    }

    @Override
    public CrewMember createCrewMember(CrewMember crewMember) throws RuntimeException {
        List<CrewMember> allCrewMembers = findAllCrewMembers();
        allCrewMembers.forEach(crewMemberFromContext -> {
            if (crewMemberFromContext.getName().equalsIgnoreCase(crewMember.getName())) {
                throw new NonUniqueEntityNameException("Crew member with the same name is already in the context");
            }
        });
        return saveCrewMember(crewMember);
    }

    private CrewMember saveCrewMember(CrewMember crewMember) {
        applicationContext.retrieveBaseEntityList(CrewMember.class).add(crewMember);
        return crewMember;
    }
}

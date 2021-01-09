package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        CrewMemberCriteria crewMemberCriteria = (CrewMemberCriteria) criteria;
        List<CrewMember> allCrewMembers = findAllCrewMembers();
        Stream<CrewMember> filteredStream = filterByCriteria(allCrewMembers, crewMemberCriteria);
        return filteredStream.collect(Collectors.toList());
    }

    @Override
    public Optional<CrewMember> findCrewMemberByCriteria(Criteria<? extends CrewMember> criteria) {
        CrewMemberCriteria crewMemberCriteria = (CrewMemberCriteria) criteria;
        List<CrewMember> allCrewMembers = findAllCrewMembers();
        Stream<CrewMember> filteredStream = filterByCriteria(allCrewMembers, crewMemberCriteria);
        return filteredStream.findFirst();
    }

    @Override
    public CrewMember updateCrewMemberDetails(CrewMember crewMember) {
        return crewMember;
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

    private Stream<CrewMember> filterByCriteria(List<CrewMember> allCrewMembers,
                                                CrewMemberCriteria crewMemberCriteria) {
        Stream<CrewMember> stream = allCrewMembers.stream();
        if (crewMemberCriteria.getWhereId() != null) {
            stream = stream.filter(crewMember -> crewMember.getId().equals(crewMemberCriteria.getWhereId()));
        }
        if (crewMemberCriteria.getWhereName() != null) {
            stream = stream.filter(crewMember -> crewMember.getName()
                    .equalsIgnoreCase(crewMemberCriteria.getWhereName()));
        }
        if (crewMemberCriteria.getWhereMemberRole() != null) {
            stream = stream.filter(crewMember -> crewMember.getMemberRole() == crewMemberCriteria.getWhereMemberRole());
        }
        if (crewMemberCriteria.getWhereMemberRank() != null) {
            stream = stream.filter(crewMember -> crewMember.getMemberRank() == crewMemberCriteria.getWhereMemberRank());
        }
        if (crewMemberCriteria.getWhichReadyForNextMission() != null) {
            stream = stream.filter(crewMember -> crewMember.getReadyForNextMissions()
                    .equals(crewMemberCriteria.getWhichReadyForNextMission()));
        }
        return stream;
    }
}

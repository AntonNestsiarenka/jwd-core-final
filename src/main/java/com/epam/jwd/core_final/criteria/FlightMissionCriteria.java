package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Spaceship;
import java.time.LocalDateTime;

/**
 * Should be a builder for {@link com.epam.jwd.core_final.domain.FlightMission} fields
 */
public class FlightMissionCriteria extends Criteria<FlightMission> {

    private final LocalDateTime whereBeforeStartDate;
    private final LocalDateTime whereAfterStartDate;
    private final LocalDateTime whereBeforeEndDate;
    private final LocalDateTime whereAfterEndDate;
    private final Long whereLesserDistance;
    private final Long whereGreaterDistance;
    private final Spaceship whereAssignedSpaceship;
    private final CrewMember whereAssignedCrew;
    private final MissionResult whereMissionResult;

    private FlightMissionCriteria(Long whereId, String whereName, LocalDateTime whereBeforeStartDate,
                                  LocalDateTime whereAfterStartDate, LocalDateTime whereBeforeEndDate,
                                  LocalDateTime whereAfterEndDate, Long whereLesserDistance, Long whereGreaterDistance,
                                  Spaceship whereAssignedSpaceship, CrewMember whereAssignedCrew,
                                  MissionResult whereMissionResult) {
        super(whereId, whereName);
        this.whereBeforeStartDate = whereBeforeStartDate;
        this.whereAfterStartDate = whereAfterStartDate;
        this.whereBeforeEndDate = whereBeforeEndDate;
        this.whereAfterEndDate = whereAfterEndDate;
        this.whereLesserDistance = whereLesserDistance;
        this.whereGreaterDistance = whereGreaterDistance;
        this.whereAssignedSpaceship = whereAssignedSpaceship;
        this.whereAssignedCrew = whereAssignedCrew;
        this.whereMissionResult = whereMissionResult;
    }

    public LocalDateTime getWhereBeforeStartDate() {
        return whereBeforeStartDate;
    }

    public LocalDateTime getWhereAfterStartDate() {
        return whereAfterStartDate;
    }

    public LocalDateTime getWhereBeforeEndDate() {
        return whereBeforeEndDate;
    }

    public LocalDateTime getWhereAfterEndDate() {
        return whereAfterEndDate;
    }

    public Long getWhereLesserDistance() {
        return whereLesserDistance;
    }

    public Long getWhereGreaterDistance() {
        return whereGreaterDistance;
    }

    public Spaceship getWhereAssignedSpaceship() {
        return whereAssignedSpaceship;
    }

    public CrewMember getWhereAssignedCrew() {
        return whereAssignedCrew;
    }

    public MissionResult getWhereMissionResult() {
        return whereMissionResult;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long whereId;
        private String whereName;
        private LocalDateTime whereBeforeStartDate;
        private LocalDateTime whereAfterStartDate;
        private LocalDateTime whereBeforeEndDate;
        private LocalDateTime whereAfterEndDate;
        private Long whereLesserDistance;
        private Long whereGreaterDistance;
        private Spaceship whereAssignedSpaceship;
        private CrewMember whereAssignedCrew;
        private MissionResult whereMissionResult;

        public Builder whereId(Long id) {
            whereId = id;
            return this;
        }

        public Builder whereName(String name) {
            whereName = name;
            return this;
        }

        public Builder whereBeforeStartDate(LocalDateTime dateTime) {
            whereBeforeStartDate = dateTime;
            return this;
        }

        public Builder whereAfterStartDate(LocalDateTime dateTime) {
            whereAfterStartDate = dateTime;
            return this;
        }

        public Builder whereBeforeEndDate(LocalDateTime dateTime) {
            whereBeforeEndDate = dateTime;
            return this;
        }

        public Builder whereAfterEndDate(LocalDateTime dateTime) {
            whereAfterEndDate = dateTime;
            return this;
        }

        public Builder whereLesserDistance(Long distance) {
            whereLesserDistance = distance;
            return this;
        }

        public Builder whereGreaterDistance(Long distance) {
            whereGreaterDistance = distance;
            return this;
        }

        public Builder whereAssignedSpaceship(Spaceship spaceship) {
            whereAssignedSpaceship = spaceship;
            return this;
        }

        public Builder whereAssignedCrew(CrewMember crewMember) {
            whereAssignedCrew = crewMember;
            return this;
        }

        public Builder whereMissionResult(MissionResult missionResult) {
            whereMissionResult = missionResult;
            return this;
        }

        public FlightMissionCriteria build() {
            return new FlightMissionCriteria(whereId, whereName, whereBeforeStartDate, whereAfterStartDate,
                    whereBeforeEndDate, whereAfterEndDate, whereLesserDistance, whereGreaterDistance,
                    whereAssignedSpaceship, whereAssignedCrew, whereMissionResult);
        }
    }
}

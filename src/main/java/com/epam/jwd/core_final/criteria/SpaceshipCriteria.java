package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;

/**
 * Should be a builder for {@link Spaceship} fields
 */
public class SpaceshipCriteria extends Criteria<Spaceship> {

    private final Role whereRoleExistInCapacity;
    private final Short whereLesserCountOfRole;
    private final Short whereGreaterCountOfRole;
    private final Long whereLesserFlightDistance;
    private final Long whereGreaterFlightDistance;
    private final Boolean whichReadyForNextMission;

    private SpaceshipCriteria(Long whereId, String whereName, Role whereRoleExistInCapacity,
                              Short whereLesserCountOfRole, Short whereGreaterCountOfRole, Long whereGreaterFlightDistance,
                              Long whereLesserFlightDistance, Boolean whichReadyForNextMission) {
        super(whereId, whereName);
        this.whereRoleExistInCapacity = whereRoleExistInCapacity;
        this.whereLesserCountOfRole = whereLesserCountOfRole;
        this.whereGreaterCountOfRole = whereGreaterCountOfRole;
        this.whereLesserFlightDistance = whereLesserFlightDistance;
        this.whereGreaterFlightDistance = whereGreaterFlightDistance;
        this.whichReadyForNextMission = whichReadyForNextMission;
    }

    public Role getWhereRoleExistInCapacity() {
        return whereRoleExistInCapacity;
    }

    public Short getWhereLesserCountOfRole() {
        return whereLesserCountOfRole;
    }

    public Short getWhereGreaterCountOfRole() {
        return whereGreaterCountOfRole;
    }

    public Long getWhereLesserFlightDistance() {
        return whereLesserFlightDistance;
    }

    public Long getWhereGreaterFlightDistance() {
        return whereGreaterFlightDistance;
    }

    public Boolean getWhichReadyForNextMission() {
        return whichReadyForNextMission;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long whereId;
        private String whereName;
        private Role whereRoleExistInCapacity;
        private Short whereLesserCountOfRole;
        private Short whereGreaterCountOfRole;
        private Long whereLesserFlightDistance;
        private Long whereGreaterFlightDistance;
        private Boolean whichReadyForNextMission;

        public Builder whereId(Long id) {
            whereId = id;
            return this;
        }

        public Builder whereName(String name) {
            whereName = name;
            return this;
        }

        public Builder whereRoleExistInCapacity(Role role) {
            whereRoleExistInCapacity = role;
            return this;
        }

        public Builder whereLesserCountOfRole(Short countOfRole) {
            whereLesserCountOfRole = countOfRole;
            return this;
        }

        public Builder whereGreaterCountOfRole(Short countOfRole) {
            whereGreaterCountOfRole = countOfRole;
            return this;
        }

        public Builder whereLesserFlightDistance(Long flightDistance) {
            whereLesserFlightDistance = flightDistance;
            return this;
        }

        public Builder whereGreaterFlightDistance(Long flightDistance) {
            whereGreaterFlightDistance = flightDistance;
            return this;
        }

        public Builder whichReadyForNextMission(Boolean isReadyForNextMission) {
            whichReadyForNextMission = isReadyForNextMission;
            return this;
        }

        public SpaceshipCriteria build() {
            return new SpaceshipCriteria(whereId, whereName, whereRoleExistInCapacity, whereLesserCountOfRole,
                    whereGreaterCountOfRole, whereLesserFlightDistance, whereGreaterFlightDistance,
                    whichReadyForNextMission);
        }
    }
}

package com.epam.jwd.core_final.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * crew {@link java.util.Map<Role, Short>}
 * flightDistance {@link Long} - total available flight distance
 * isReadyForNextMissions {@link Boolean} - true by default. Set to false, after first failed mission
 */
public class Spaceship extends AbstractBaseEntity {
    //todo
    private static Long idUnique = 0L;

    private final Map<Role, Short> capacityByRole;
    private Long flightDistance;
    private Boolean isReadyForNextMission;

    public Spaceship(String name, Map<Role, Short> capacityByRole, Long flightDistance) {
        super(idUnique++, name);
        this.capacityByRole = capacityByRole;
        this.flightDistance = flightDistance;
        this.isReadyForNextMission = true;
    }

    public Map<Role, Short> getCapacityByRole() {
        return new HashMap<>(capacityByRole);
    }

    public Long getFlightDistance() {
        return flightDistance;
    }

    public void setFlightDistance(Long flightDistance) {
        this.flightDistance = flightDistance;
    }

    public Boolean getReadyForNextMission() {
        return isReadyForNextMission;
    }

    public void setReadyForNextMission(Boolean readyForNextMission) {
        isReadyForNextMission = readyForNextMission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spaceship spaceship = (Spaceship) o;
        return getId().equals(spaceship.getId()) &&
                getName().equals(spaceship.getName()) &&
                Objects.equals(capacityByRole, spaceship.capacityByRole) &&
                Objects.equals(flightDistance, spaceship.flightDistance) &&
                Objects.equals(isReadyForNextMission, spaceship.isReadyForNextMission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), capacityByRole, flightDistance, isReadyForNextMission);
    }

    @Override
    public String toString() {
        return "Spaceship{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", capacityByRole=" + capacityByRole +
                ", flightDistance=" + flightDistance +
                ", isReadyForNextMission=" + isReadyForNextMission +
                '}';
    }
}

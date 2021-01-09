package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.factory.EntityFactory;
import java.time.LocalDateTime;

public class FlightMissionFactory implements EntityFactory<FlightMission> {

    private static FlightMissionFactory instance;

    private FlightMissionFactory() {

    }

    public static FlightMissionFactory createInstance() {
        if (instance == null) {
            instance = new FlightMissionFactory();
        }
        return instance;
    }

    @Override
    public FlightMission create(Object... args) {
        return new FlightMission((String) args[0], (LocalDateTime) args[1], (LocalDateTime) args[2], (Long) args[3],
                (MissionResult) args[4]);
    }
}

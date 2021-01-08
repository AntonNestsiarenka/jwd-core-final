package com.epam.jwd.core_final.exception;

public class AssigningEntityOnMissionException extends RuntimeException {

    private final String entityName;

    public AssigningEntityOnMissionException(String entityName) {
        super();
        this.entityName = entityName;
    }

    @Override
    public String getMessage() {
        return String.format("Assigning entity %s on mission failed. Entity is already assigned in other mission.",
                entityName);
    }
}

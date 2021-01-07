package com.epam.jwd.core_final.domain;

import java.time.format.DateTimeFormatter;

/**
 * This class should be IMMUTABLE!
 * <p>
 * Expected fields:
 * <p>
 * inputRootDir {@link String} - base dir for all input files
 * outputRootDir {@link String} - base dir for all output files
 * crewFileName {@link String}
 * missionsFileName {@link String}
 * spaceshipsFileName {@link String}
 * <p>
 * fileRefreshRate {@link Integer}
 * dateTimeFormat {@link String} - date/time format for {@link java.time.format.DateTimeFormatter} pattern
 */
public final class ApplicationProperties {
    //todo
    private final String inputRootDir;
    private final String outputRootDir;
    private final String crewFilename;
    private final String missionsFileName;
    private final String spaceshipsFileName;
    private final Integer fileRefreshRate;
    private final DateTimeFormatter dateTimeFormatter;

    public ApplicationProperties(String inputRootDir, String outputRootDir, String crewFilename,
                                 String missionsFileName, String spaceshipsFileName, Integer fileRefreshRate,
                                 DateTimeFormatter patternForDateTimeFormatter) {
        this.inputRootDir = inputRootDir;
        this.outputRootDir = outputRootDir;
        this.crewFilename = crewFilename;
        this.missionsFileName = missionsFileName;
        this.spaceshipsFileName = spaceshipsFileName;
        this.fileRefreshRate = fileRefreshRate;
        this.dateTimeFormatter = patternForDateTimeFormatter;
    }

    public String getInputRootDir() {
        return inputRootDir;
    }

    public String getOutputRootDir() {
        return outputRootDir;
    }

    public String getCrewFilename() {
        return crewFilename;
    }

    public String getMissionsFileName() {
        return missionsFileName;
    }

    public String getSpaceshipsFileName() {
        return spaceshipsFileName;
    }

    public Integer getFileRefreshRate() {
        return fileRefreshRate;
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }
}

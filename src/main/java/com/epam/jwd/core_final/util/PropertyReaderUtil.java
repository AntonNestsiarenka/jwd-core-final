package com.epam.jwd.core_final.util;

import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.exception.ApplicationPropertiesException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PropertyReaderUtil {

    private static final Properties properties = new Properties();

    private PropertyReaderUtil() {
    }

    public static Properties getProperties() {
        return properties;
    }

    /**
     * try-with-resource using FileInputStream
     *
     * @see {https://www.netjstech.com/2017/09/how-to-read-properties-file-in-java.html for an example}
     * <p>
     * as a result - you should populate {@link ApplicationProperties} with corresponding
     * values from property file
     */
    public static void loadProperties() {
        final String propertiesFileName = "src/main/resources/application.properties";
        try (InputStream inputStream = new FileInputStream(propertiesFileName)) {
            StringBuilder text = new StringBuilder();
            int i = -1;
            while ((i = inputStream.read()) != -1) {
                text.append((char) i);
            }
            Pattern pattern = Pattern.compile("^(?=(.|\n|\r)*inputRootDir=([\\w\\d-]+))" +
                    "(?=(.|\n|\r)*outputRootDir=([\\w\\d-]+))" +
                    "(?=(.|\n|\r)*crewFileName=([\\w\\d-]+))" +
                    "(?=(.|\n|\r)*missionsFileName=([\\w\\d-]+))" +
                    "(?=(.|\n|\r)*spaceshipsFileName=([\\w\\d-]+))" +
                    "(?=(.|\n|\r)*fileRefreshRate=(\\d+))" +
                    "(?=(.|\n|\r)*dateTimeFormat=([a-zA-Z:\\-\\s]+))" +
                    "[a-zA-Z\\d]+=.+(\r?\n(\r?\n)*[a-zA-Z\\d]+=.+)*$");
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                ApplicationProperties applicationProperties = new ApplicationProperties(matcher.group(2),
                        matcher.group(4), matcher.group(6), matcher.group(8), matcher.group(10),
                        Integer.valueOf(matcher.group(12)), DateTimeFormatter.ofPattern(matcher.group(14)));
                properties.put("appProperties", applicationProperties);
            }
            else {
                throw new ApplicationPropertiesException("Cannot parse application.properties file." +
                        " Some variables are missing or incorrect format for setting variables.");
            }
        }
        catch (IOException | IllegalArgumentException e) {
            throw new ApplicationPropertiesException("Cannot parse application.properties file", e);
        }
    }
}

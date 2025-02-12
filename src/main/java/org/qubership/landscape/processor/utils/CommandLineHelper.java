package org.qubership.landscape.processor.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class CommandLineHelper {
    private String propsFileName;

    private String baseConfigFileName;
    private String extraConfigDirName;
    private String resultConfigFileName;
    private String srcLogosDirName;
    private String dstLogosDirName;

    public CommandLineHelper(String[] args) {
        if (args.length == 1) {
            propsFileName = args[0];
        }
    }

    public boolean areArgumentsSufficient() {
        if (propsFileName == null) return false;

        // load properties from property file
        Path path = Paths.get(propsFileName);
        try (InputStream is = Files.newInputStream(path)) {
            Properties properties = new Properties();
            properties.load(is);

            this.baseConfigFileName   = getPropertyOrFail(properties,"base-configuration-file-name");
            this.extraConfigDirName   = getPropertyOrFail(properties,"extra-configurations-directory-name");
            this.resultConfigFileName = getPropertyOrFail(properties,"result-configuration-file-name");
            this.srcLogosDirName      = getPropertyOrFail(properties,"source-logos-folder");
            this.dstLogosDirName      = getPropertyOrFail(properties,"dest-logos-folder");

        } catch (IOException | IllegalStateException ex) {
            TheLogger.error("Error while reading file " + propsFileName, ex);
            return false;
        }

        // all expected properties are loaded
        return true;
    }

    public void printHelp() {
        TheLogger.warn("Usage: java -jar processor-app.jar $PATH_TO_PROPERTIES_FILE$");
    }

    private String getPropertyOrFail(Properties properties, String propertyName) {
        String value = properties.getProperty(propertyName);
        if (StringUtils.isEmpty(value)) {
            throw new IllegalStateException("Error while reading '" + propertyName + "' property value.");
        }

        return value;
    }

    public String getPropsFileName() {
        return propsFileName;
    }

    public String getBaseConfigFileName() {
        return baseConfigFileName;
    }

    public String getExtraConfigDirName() {
        return extraConfigDirName;
    }

    public String getResultConfigFileName() {
        return resultConfigFileName;
    }

    public String getSrcLogosDirName() {
        return srcLogosDirName;
    }

    public String getDstLogosDirName() {
        return dstLogosDirName;
    }
}

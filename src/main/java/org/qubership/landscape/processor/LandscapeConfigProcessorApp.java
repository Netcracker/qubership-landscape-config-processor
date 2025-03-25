package org.qubership.landscape.processor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.qubership.landscape.processor.model.landscapefile.TheCategory;
import org.qubership.landscape.processor.model.landscapefile.TheItem;
import org.qubership.landscape.processor.model.landscapefile.TheLandscape;
import org.qubership.landscape.processor.model.landscapefile.TheSubCategory;
import org.qubership.landscape.processor.utils.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Prepares effective configuration for landscape2 application
 * using base landscape.yml file and number of *.yml extensions
 * by merging them into base configuration.
 *
 * It also supports additional states for the items providing post-rendering SVG icons.
 */
public class LandscapeConfigProcessorApp {
    public static void main(String[] args) throws Exception {
        // Generic algorithm:
        // Step1: read all *.yml files and try them as landscape2-formatted file
        // Step2: build common merged model
        // Step3: save common merged model into result yaml file
        // Step4: generate updates SVG icons where required by the state of corresponding item

        // Check all required parameters are set via command-line interface
        CommandLineHelper cmdHelper = new CommandLineHelper(args);
        if (!cmdHelper.areArgumentsSufficient()) {
            cmdHelper.printHelp();
            System.exit(-1);
        }

        // Instantiate JSON/YAML reader
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Step1: read original landscape.yml file and fetch Categories & Subcategories
        String primaryFileName = cmdHelper.getBaseConfigFileName();
        File primaryFile = new File(primaryFileName);

        TheLandscape primaryModel = null;
        try {
             primaryModel = mapper.readValue(primaryFile, TheLandscape.class);
        } catch (Exception ex) {
            TheLogger.error("Primary model of landscape2 format can't be found/read. Terminating.", ex);
            System.exit(-1);
        }

        // let's mark all OOB items as archived
        AuxUtils.markAllAsArchived(primaryModel);

        String rootDir = cmdHelper.getExtraConfigDirName();
        List<String> collectedYamls = FileUtils.findAllFilesRecursively(rootDir, ".yml");

        if (collectedYamls.isEmpty()) {
            TheLogger.warn("No additional *.yml files were found at " + rootDir);
        }

        // We should skip primary file which may be collected in PATH_TO_SCAN dir


        List<TheLandscape> allModels = new ArrayList<>(collectedYamls.size());
        for (String nextFileName : collectedYamls) {
            try {
                File secondaryFile = new File(nextFileName);
                if (primaryFile.equals(secondaryFile)) {
                    TheLogger.debug("Skip primary file from list of secondaries");
                    continue;
                }

                TheLogger.debug("Reading additional configuration at " + nextFileName);
                TheLandscape nextModel = mapper.readValue(secondaryFile, TheLandscape.class);

                TheLogger.debug("Merging additional configuration...");
                primaryModel.mergeValuesFrom(nextModel);
                TheLogger.debug("Merged");
            } catch (Exception ex) {
                TheLogger.warn("File can't be read in landscape2 format. Terminating.", ex);
                System.exit(-1);
            }
        }

        // remove items which must be dropped
        TheModelsUtils.removeDroppedItems(primaryModel);

        String outputFileName = cmdHelper.getResultConfigFileName();
        TheLogger.debug("Saving result configuration into " + outputFileName);

        // Provide some statistics about new result file for debug aims
        printReport(primaryModel);

        mapper.writeValue(new File(outputFileName), primaryModel);

        // now let's go through all items - and generate logos with red-cross for deprecated
        String srcLogosFolder = cmdHelper.getSrcLogosDirName();
        String dstLogosFolder = cmdHelper.getDstLogosDirName();
        TheModelsUtils.generateRedCrossLogos(primaryModel, srcLogosFolder, dstLogosFolder);
    }

    private static void printReport(TheLandscape model) {
        int countCategories = 0;
        int countSubCategories = 0;
        int countItems = 0;

        for (TheCategory cat : model.getCategories()) {
            countCategories++;

            for (TheSubCategory subCat : cat.getSubcategories()) {
                countSubCategories++;

                for (TheItem item : subCat.getItemList()) {
                    countItems++; // todo: yeah... better without iteration, just to use getItemsList().size()... but - no matter now
                }
            }
        }

        TheLogger.debug("Summary:");
        TheLogger.debug("    Categories count: " + countCategories);
        TheLogger.debug("    SubCategories count: " + countSubCategories);
        TheLogger.debug("    Items count: " + countItems);
    }


}

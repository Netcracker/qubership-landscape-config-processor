package org.qubership.landscape.processor.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.qubership.landscape.processor.model.landscapefile.TheCategory;
import org.qubership.landscape.processor.model.landscapefile.TheItem;
import org.qubership.landscape.processor.model.landscapefile.TheLandscape;
import org.qubership.landscape.processor.model.landscapefile.TheSubCategory;
import org.qubership.landscape.processor.utils.svg.IGraphNodeRenderer;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TheModelsUtils {
    private final static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static TheLandscape loadModelFrom (File yamlFile) throws Exception {
        return mapper.readValue(yamlFile, TheLandscape.class);
    }

    public static TheLandscape loadModelFromResource (String resourceName) throws Exception {
        ClassLoader classloader = mapper.getClass().getClassLoader();
        InputStream is = classloader.getResourceAsStream(resourceName);

        return mapper.readValue(is, TheLandscape.class);
    }

    public static void removeDroppedItems(TheLandscape primaryModel) {
        for (TheCategory cat : primaryModel.getCategories()) {
            for (TheSubCategory subCat : cat.getSubcategories()) {

                List<TheItem> dropList = null;
                for (TheItem item : subCat.getItemList()) {
                    if ("DROP".equalsIgnoreCase(item.getProject())) {
                        if (dropList == null) dropList = new ArrayList<>();
                        dropList.add(item);
                    }
                }
                if (dropList != null) {
                    subCat.getItemList().removeAll(dropList);
                }
            }
        }
    }

    public static void generateRedCrossLogos(TheLandscape primaryModel, String folderWithOriginalLogos, String folderToSaveNewLogos) {

        for (TheCategory cat : primaryModel.getCategories()) {
            for (TheSubCategory subCat : cat.getSubcategories()) {
                for (TheItem item : subCat.getItemList()) {
                    String status = item.getProject();

                    if ("REJECT".equalsIgnoreCase(status) || "REJECTED".equalsIgnoreCase(status)) {
                        postRenderSvg(item, SVGUtils.RED_CROSS_RENDERER, folderWithOriginalLogos, folderToSaveNewLogos);
                        continue;
                    }

                    if ("RESEARCHING".equalsIgnoreCase(status)) {
                        postRenderSvg(item, SVGUtils.UNDER_RESEARCH_RENDERER, folderWithOriginalLogos, folderToSaveNewLogos);
                    }
                }
            }
        }
    }

    private static void postRenderSvg(TheItem item, IGraphNodeRenderer renderer, String folderWithOriginalLogos, String folderToSaveNewLogos) {
        String logoShortName = item.getLogo();

        String inFileName = folderWithOriginalLogos + File.separator + logoShortName;
        String outFileName = folderToSaveNewLogos + File.separator + logoShortName;

        try {
            String svgContent = SVGUtils.addExtraGraphLayer(inFileName, renderer);
            FileUtils.saveToFile(svgContent, outFileName);
            TheLogger.debug("New SVG file generated " + outFileName);
        } catch (Exception ex) {
            TheLogger.error("Error while processing SVG files: in file = " + logoShortName + ", out file = " + outFileName, ex);
            // do nothing, continue next item
        }
    }
}

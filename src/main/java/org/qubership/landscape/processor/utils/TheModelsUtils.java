package org.qubership.landscape.processor.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.qubership.landscape.processor.model.landscapefile.CustomState.*;

public class TheModelsUtils {
    private final static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static TheLandscape loadModelFrom(File yamlFile) throws Exception {
        return mapper.readValue(yamlFile, TheLandscape.class);
    }

    public static TheLandscape loadModelFromResource(String resourceName) throws Exception {
        ClassLoader classloader = mapper.getClass().getClassLoader();
        InputStream is = classloader.getResourceAsStream(resourceName);

        return mapper.readValue(is, TheLandscape.class);
    }

    public static void removeDroppedItems(TheLandscape primaryModel) {
        for (TheCategory cat : primaryModel.getCategories()) {
            for (TheSubCategory subCat : cat.getSubcategories()) {

                List<TheItem> dropList = null;
                for (TheItem item : subCat.getItemList()) {
                    String status = item.getProject();
                    if (DROP.correspondsTo(status)) {
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

                    if (REJECT.correspondsTo(status)) {
                        postRenderSvg(item, SVGUtils.RED_CROSS_RENDERER, folderWithOriginalLogos, folderToSaveNewLogos);
                        continue;
                    }

                    if (RESEARCH.correspondsTo(status))
                        postRenderSvg(item, SVGUtils.UNDER_RESEARCH_RENDERER, folderWithOriginalLogos, folderToSaveNewLogos);
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

    public static final String HOMEPAGE_URL_DEFAULT_VALUE = "http://google.com";
    public static final String LOGO_DEFAULT_VALUE = "nc_logo.svg";

    /**
     * Goes over all items and add mandatory fields in case they are absent
     * @param primaryModel
     */
    public static void fulfillMandatoryFieldIfAbsent(TheLandscape primaryModel) {
        for (TheCategory cat : primaryModel.getCategories()) {
            for (TheSubCategory subCat : cat.getSubcategories()) {
                for (TheItem item : subCat.getItemList()) {
                    if (StringUtils.isEmpty(item.getHomepageUrl())) {
                        String value = findAttributeValue(item.getName(),"homepage_url", primaryModel,HOMEPAGE_URL_DEFAULT_VALUE);
                        item.setHomepageUrl(value);
                    }

                    if (StringUtils.isEmpty(item.getLogo())) {
                        String value = findAttributeValue(item.getName(),"logo", primaryModel, LOGO_DEFAULT_VALUE);
                        item.setLogo(value); // todo: generate personal logos
                    }
                }
            }
        }
    }

    /**
     * Scans all items in the primary model which have same name as specified in the itemName parameter.
     * In case such item is found - try to get attribute value from the filed which is marked by corresponding JsonProperty annotation
     * @param itemName String item name to search
     * @param jsonPropertyName String json property annotation name, see TheItem fields
     * @param primaryModel TheLandscape instance to iteratee over
     * @param defaultValue String value to be returned in case nothing is found
     * @return String
     */
    public static String findAttributeValue(String itemName, String jsonPropertyName, TheLandscape primaryModel, String defaultValue) {
        for (TheCategory cat : primaryModel.getCategories()) {
            for (TheSubCategory subCat : cat.getSubcategories()) {
                for (TheItem item : subCat.getItemList()) {
                    if (itemName.equals(item.getName())) {
                        String fieldValue = getValueByReflection(item, jsonPropertyName);
                        if (!defaultValue.equals(fieldValue) && fieldValue != null) return fieldValue;
                    }
                }
            }
        }

        return defaultValue;
    }


    private static String getValueByReflection(TheItem item, String jsonPropertyName) {
        try {
            Field[] fields = item.getClass().getDeclaredFields();
            for (Field field : fields) {
                JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
                if (jsonProperty == null) continue;

                if (jsonPropertyName.equals(jsonProperty.value())) {
                    field.setAccessible(true);
                    return (String) field.get(item);
                }
            }
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }

        return null;
    }
}

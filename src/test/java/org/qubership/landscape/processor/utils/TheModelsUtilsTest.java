package org.qubership.landscape.processor.utils;

import org.junit.Test;
import org.qubership.landscape.processor.model.landscapefile.TheLandscape;
import org.qubership.landscape.processor.model.yaml_based.ConfigA_Factory;

import static org.junit.Assert.assertEquals;

public class TheModelsUtilsTest {
    @Test
    public void testFindAttributeValueByReflection_logoField_existedItem() throws Exception {
        TheLandscape primaryModel = ConfigA_Factory.getConfigA();
        String actLogoValue = TheModelsUtils.findAttributeValue("Item - 002", "logo", primaryModel, "DEF_VALUE");
        String expLogoValue = "test002.svg";

        assertEquals(expLogoValue, actLogoValue);
    }

    @Test
    public void testFindAttributeValueByReflection_HomeUrlField_existedItem() throws Exception {
        TheLandscape primaryModel = ConfigA_Factory.getConfigA();
        String actUrlValue = TheModelsUtils.findAttributeValue("Item - 003", "homepage_url", primaryModel, "DEF_VALUE");
        String expUrlValue = "https://example.org/test003";

        assertEquals(expUrlValue, actUrlValue);
    }

    @Test
    public void testFindAttributeValueByReflection_HomeUrlField_NonExistedItem() throws Exception {
        TheLandscape primaryModel = ConfigA_Factory.getConfigA();
        String expValue = TheModelsUtils.HOMEPAGE_URL_DEFAULT_VALUE;
        String actValue = TheModelsUtils.findAttributeValue("Non existed item name", "homepage_url", primaryModel, expValue);

        assertEquals(expValue, actValue);
    }
}

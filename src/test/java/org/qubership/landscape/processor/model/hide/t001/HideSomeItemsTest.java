package org.qubership.landscape.processor.model.hide.t001;

import org.qubership.landscape.processor.model.yaml_based.ConfigA_Factory;
import org.junit.Test;
import org.qubership.landscape.processor.model.landscapefile.TheCategory;
import org.qubership.landscape.processor.model.landscapefile.TheItem;
import org.qubership.landscape.processor.model.landscapefile.TheLandscape;
import org.qubership.landscape.processor.model.landscapefile.TheSubCategory;
import org.qubership.landscape.processor.utils.TheModelsUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HideSomeItemsTest {

    private void doTestLogic(String extraResourceName, List<String> expItemNamesAfterMerge) {
        try {
            TheLandscape baseModel = ConfigA_Factory.getConfigA();
            TheLandscape extraModel = TheModelsUtils.loadModelFromResource(extraResourceName);

            // merge configs
            baseModel.mergeValuesFrom(extraModel);
            TheModelsUtils.removeDroppedItems(baseModel);

            // check results
            TheCategory theCat = baseModel.getCategories().get(0);
            TheSubCategory theSubCat = theCat.getSubcategories().get(0);

            List<TheItem> items = theSubCat.getItemList();
            List<String> expNames = new ArrayList<>(expItemNamesAfterMerge);

            assertEquals(expNames.size(), items.size());
            for (TheItem nextItem : items) {
                expNames.remove(nextItem.getName());
            }

            assertTrue("Unexpected item names: " + expNames, expNames.isEmpty());


        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void hideItem001() {
        doTestLogic("com/github/exadmin/hide/t001/drop-item-001.yml",
                Arrays.asList("Item - 002", "Item - 003"));
    }

    @Test
    public void hideItem002() {
        doTestLogic("com/github/exadmin/hide/t001/drop-item-002.yml",
                Arrays.asList("Item - 001", "Item - 003"));
    }

    @Test
    public void hideItem003() {
        doTestLogic("com/github/exadmin/hide/t001/drop-item-003.yml",
                Arrays.asList("Item - 002", "Item - 001"));
    }

    @Test
    public void hideNonExistedItem() {
        doTestLogic("com/github/exadmin/hide/t001/drop-item-004.yml",
                Arrays.asList("Item - 001", "Item - 002", "Item - 003"));
    }

    @Test (expected = AssertionError.class)
    public void rainyTest() {
        doTestLogic("com/github/exadmin/hide/t001/drop-item-001.yml",
                Arrays.asList("Item - 001", "Item - 002", "Item - 003"));
    }
}

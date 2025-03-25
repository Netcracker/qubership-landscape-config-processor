package org.qubership.landscape.processor.utils;

import org.qubership.landscape.processor.model.landscapefile.TheCategory;
import org.qubership.landscape.processor.model.landscapefile.TheItem;
import org.qubership.landscape.processor.model.landscapefile.TheLandscape;
import org.qubership.landscape.processor.model.landscapefile.TheSubCategory;

import java.util.ArrayList;
import java.util.List;

public class AuxUtils {
    public static List<String> getSubcategories(TheLandscape lsModel) {
        final List<String> uniqueSet = new ArrayList<>();

        for (TheCategory category : lsModel.getCategories()) {
            String categoryName = category.getName();

            if (categoryName == null || categoryName.isEmpty()) {
                throw new IllegalStateException("Empty category is found in landscape-model. Value == '" + categoryName + "'");
            }

            categoryName = categoryName + ":"; // creating a kind of namespace for subcategory

            for (TheSubCategory subcategory : category.getSubcategories()) {
                String subCategoryName = subcategory.getName();

                if (subCategoryName == null || subCategoryName.isEmpty()) {
                    throw new IllegalStateException("Empty subcategory is found in category = '" + categoryName.substring(0, categoryName.length() - 1) + "'");
                }

                String subCategoryNameWithPrefix = categoryName + subCategoryName;
                if (uniqueSet.contains(subCategoryNameWithPrefix)) {
                    throw new IllegalStateException("Duplicate subcategory definition is found: " + subCategoryNameWithPrefix);
                }

                uniqueSet.add(subCategoryNameWithPrefix);
            }
        }

        return uniqueSet;
    }

    public static void markAllAsArchived(TheLandscape model) {
        for (TheCategory cat : model.getCategories()) {
            for (TheSubCategory subCat : cat.getSubcategories()) {
                for (TheItem item : subCat.getItemList()) {
                    item.setProject("archived");
                }
            }
        }
    }
}

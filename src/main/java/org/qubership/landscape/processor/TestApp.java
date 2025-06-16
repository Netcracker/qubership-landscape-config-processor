package org.qubership.landscape.processor;

import org.qubership.landscape.processor.model.landscapefile.TheCategory;
import org.qubership.landscape.processor.model.landscapefile.TheItem;
import org.qubership.landscape.processor.model.landscapefile.TheLandscape;
import org.qubership.landscape.processor.model.landscapefile.TheSubCategory;
import org.qubership.landscape.processor.utils.TheModelsUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestApp {
    public static void main(String[] args) throws Exception {
        TheLandscape primaryModel = TheModelsUtils.loadModelFrom(new File("c:\\SVN\\qubership-landscape-config\\landscape.yml"));
        TheLandscape secondaryModel = TheModelsUtils.loadModelFrom(new File("c:\\SVN\\qubership-landscape-config\\added-items\\in-use\\in-use-by-Sergey-Ivanov.yml"));

        {
            System.out.println("Status before merge");
            List<String> pmList = getFieldValue(primaryModel, "PostgreSQL", "logo");
            System.out.println("PM Logo = " + pmList);

            List<String> smList = getFieldValue(secondaryModel, "PostgreSQL", "logo");
            System.out.println("SM Logo = " + smList);
        }

        primaryModel.mergeValuesFrom(secondaryModel);

        for (TheCategory cat : primaryModel.getCategories()) {
            for (TheSubCategory subCat : cat.getSubcategories()) {
                for (TheItem item : subCat.getItemList()) {
                    if (item.getName().equalsIgnoreCase("PostgreSQL")) {
                        System.out.println("Found " + item);
                    }
                }
            }
        }

        {
            System.out.println("Status after merge");
            List<String> pmList = getFieldValue(primaryModel, "PostgreSQL", "logo");
            System.out.println("PM Logo = " + pmList);

            List<String> smList = getFieldValue(secondaryModel, "PostgreSQL", "logo");
            System.out.println("SM Logo = " + smList);
        }
    }

    private static List<String> getFieldValue(TheLandscape model, String itemName, String fieldName) {
        List<String> result = new ArrayList<>();

        for (TheCategory cat : model.getCategories()) {
            for (TheSubCategory subCat : cat.getSubcategories()) {
                for (TheItem item : subCat.getItemList()) {
                    if (item.getName().equalsIgnoreCase(itemName)) {
                        if ("logo".equalsIgnoreCase(fieldName)) {
                            result.add(item.getLogo());
                        } else if ("homepage_url".equalsIgnoreCase(fieldName)) {
                            result.add(item.getHomepageUrl());
                        } else {
                            throw new IllegalArgumentException("Unknown field name " + fieldName);
                        }
                    }
                }
            }
        }

        return result;
    }


}

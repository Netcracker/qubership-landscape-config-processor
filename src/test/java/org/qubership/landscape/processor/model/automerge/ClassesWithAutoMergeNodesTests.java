package org.qubership.landscape.processor.model.automerge;

import org.junit.Test;
import org.qubership.landscape.processor.utils.ListsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class ClassesWithAutoMergeNodesTests {
    private static class TheSubClass extends AutoMergeable {
        private String name;
        private String strField;
        private Integer intField;

        public TheSubClass(String name, String strField, Integer intField) {
            this.name = name;
            this.strField = strField;
            this.intField = intField;
        }

        public String getName() {
            return name;
        }

        public String getStrField() {
            return strField;
        }

        public Integer getIntField() {
            return intField;
        }

        @Override
        protected boolean allowMergeFrom(AutoMergeable other) {
            if (other == null || getClass() != other.getClass()) return false;
            TheSubClass that = (TheSubClass) other;
            return Objects.equals(name, that.name);
        }
    }


    private static class TheParentClass extends AutoMergeable {
        private String name;
        private TheSubClass vipChild;
        private List<TheSubClass> children;

        public TheParentClass(String name) {
            this.name = name;
            this.children = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public TheSubClass getVipChild() {
            return vipChild;
        }

        public List<TheSubClass> getChildren() {
            return children;
        }

        public void setVipChild(TheSubClass vipChild) {
            this.vipChild = vipChild;
        }

        @Override
        protected boolean allowMergeFrom(AutoMergeable other) {
            return false;
        }
    }

    @Test
    public void mergeOneAutomergeableField() {
        // prepare test data
        TheParentClass objMergeInto = new TheParentClass("Main Parent Instance");
        {
            TheSubClass vipChild = new TheSubClass("Vip Child", "aaaaa", 1000);
            objMergeInto.setVipChild(vipChild);
        }

        TheParentClass objMergeFrom = new TheParentClass("Object to merge from");
        {
            TheSubClass vipChild = new TheSubClass("Vip Child", "bbbbb", 55555);
            objMergeFrom.setVipChild(vipChild);
        }

        // merge objects
        objMergeInto.mergeValuesFrom(objMergeFrom);

        // check result
        TheSubClass actSubClassObj = objMergeInto.getVipChild();
        assertEquals("Vip Child", actSubClassObj.getName());
        assertEquals("bbbbb", actSubClassObj.getStrField());
        assertEquals((Integer) 55555, actSubClassObj.getIntField());
    }

    @Test
    public void mergeAutomergeableFieldOfListType() {
        // prepare test data
        TheParentClass objMergeInto = new TheParentClass("Main Parent Instance");
        {
            TheSubClass vipChild = new TheSubClass("Vip Child", null, 1000);
            objMergeInto.setVipChild(vipChild);

            // create objects with name "Child #X" where X -> 0 to 9
            for (int i=0; i<10; i++) {
                TheSubClass subObj = new TheSubClass("Child #" + i, "text" + i, i * 1000);
                objMergeInto.getChildren().add(subObj);
            }
        }

        TheParentClass objMergeFrom = new TheParentClass("Object to merge from");
        {
            TheSubClass vipChild = new TheSubClass("Vip Child", "bbbbb", 55555);
            objMergeFrom.setVipChild(vipChild);

            // create objects with name "Child #X" where X -> 8 to 11 (partially intersects with previously created objects)
            for (int i=8; i<12; i++) {
                TheSubClass subObj = new TheSubClass("Child #" + i, "text" + i, i * 1000);
                objMergeFrom.getChildren().add(subObj);
            }
        }

        // merge objects
        objMergeInto.mergeValuesFrom(objMergeFrom);

        // check result
        TheSubClass actSubClassObj = objMergeInto.getVipChild();
        assertEquals("Vip Child", actSubClassObj.getName());
        assertEquals("bbbbb", actSubClassObj.getStrField());
        assertEquals((Integer) 55555, actSubClassObj.getIntField());

        // prepare expected list of names for children objects
        List<String> expNames = new ArrayList<>();
        for (int i=0; i<12; i++) {
            expNames.add("Child #" + i);
        }

        // collect actual names from child objects
        List<String> actNames = new ArrayList<>();
        for (TheSubClass childObj : objMergeInto.getChildren()) {
            actNames.add(childObj.getName());
        }

        ListsUtils.assertListsSimilar(expNames, actNames);
    }
}

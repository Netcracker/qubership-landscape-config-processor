package org.qubership.landscape.processor.model.automerge;

import org.junit.Test;
import org.qubership.landscape.processor.utils.ListsUtils;

import java.util.List;

public class ArrayFieldsTests {
    private static class TheTestClass extends AutoMergeable {
        private List<String> field1;
        private List<String> field2;

        public TheTestClass(List<String> field1, List<String> field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        public List<String> getField1() {
            return field1;
        }

        public List<String> getField2() {
            return field2;
        }

        @Override
        protected boolean allowMergeFrom(AutoMergeable other) {
            return false;
        }
    }

    @Test
    public void mergeStringArraysDifferentValuesEverywhere() {
        List<String> obj1Field1 = ListsUtils.asList("10000", "20000", "30000");
        List<String> obj1Field2 = ListsUtils.asList("aaa", "bbb", "ccc");
        TheTestClass obj1 = new TheTestClass(obj1Field1, obj1Field2);

        List<String> obj2Field1 = ListsUtils.asList("11111", "22222", "33333");
        List<String> obj2Field2 = ListsUtils.asList("aaaaa", "bbbbb", "ccccc");
        TheTestClass obj2 = new TheTestClass(obj2Field1, obj2Field2);

        List<String> expObj1Field1 = ListsUtils.asList("10000", "20000", "30000", "11111", "22222", "33333");
        List<String> expObj1Field2 = ListsUtils.asList("aaa", "bbb", "ccc", "aaaaa", "bbbbb", "ccccc");

        obj1.mergeValuesFrom(obj2);

        ListsUtils.assertListsSimilar(expObj1Field1, obj1.getField1());
        ListsUtils.assertListsSimilar(expObj1Field2, obj1.getField2());
    }

    @Test
    public void mergeStringArraysSameValuesExist() {
        List<String> obj1Field1 = ListsUtils.asList("10000", "20000", "30000");
        List<String> obj1Field2 = ListsUtils.asList("aaa", "bbb", "ccc");
        TheTestClass obj1 = new TheTestClass(obj1Field1, obj1Field2);

        List<String> obj2Field1 = ListsUtils.asList("11111", "20000", "33333");
        List<String> obj2Field2 = ListsUtils.asList("aaaaa", "bbbbb", "ccc");
        TheTestClass obj2 = new TheTestClass(obj2Field1, obj2Field2);

        List<String> expObj1Field1 = ListsUtils.asList("10000", "20000", "30000", "11111", "33333");
        List<String> expObj1Field2 = ListsUtils.asList("aaa", "bbb", "ccc", "aaaaa", "bbbbb");

        obj1.mergeValuesFrom(obj2);

        ListsUtils.assertListsSimilar(expObj1Field1, obj1.getField1());
        ListsUtils.assertListsSimilar(expObj1Field2, obj1.getField2());
    }
}

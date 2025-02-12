package org.qubership.landscape.processor.model.automerge;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SimpleStringFieldsTests {

    private static class TheTestClass extends AutoMergeable {
        private String field1;
        private String field2;

        @IgnoreWhenAutoMerging
        private String field3;

        public TheTestClass(String field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        public TheTestClass(String field1, String field2, String field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }

        public String getField1() {
            return field1;
        }

        public String getField2() {
            return field2;
        }

        public String getField3() {
            return field3;
        }

        @Override
        protected boolean allowMergeFrom(AutoMergeable other) {
            return false;
        }
    }

    @Test
    public void mergeStringFieldsButIgnoreSpecifiedOne() {
        TheTestClass obj1 = new TheTestClass("ttt", null, "Ignore this value");
        TheTestClass obj2 = new TheTestClass("Hello", "World!", "Overridden");

        assertEquals("ttt", obj1.getField1());
        assertNull(obj1.getField2());
        assertEquals("Ignore this value", obj1.getField3());

        obj1.mergeValuesFrom(obj2);
        assertEquals("Hello", obj1.getField1());
        assertEquals("World!", obj1.getField2());
        assertEquals("Ignore this value", obj1.getField3());
    }
}

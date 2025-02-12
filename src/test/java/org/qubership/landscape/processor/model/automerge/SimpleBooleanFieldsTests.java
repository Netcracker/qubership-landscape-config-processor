package org.qubership.landscape.processor.model.automerge;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleBooleanFieldsTests {

    private static class TheTestClass extends AutoMergeable {
        private Boolean field1;
        private Boolean field2;

        @IgnoreWhenAutoMerging
        private Boolean field3;

        public TheTestClass(Boolean field1, Boolean field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        public TheTestClass(Boolean field1, Boolean field2, Boolean field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }

        public Boolean getField1() {
            return field1;
        }

        public Boolean getField2() {
            return field2;
        }

        public Boolean getField3() {
            return field3;
        }

        @Override
        protected boolean allowMergeFrom(AutoMergeable other) {
            return false;
        }
    }

    @Test
    public void mergeStringFieldsButIgnoreSpecifiedOne() {
        TheTestClass obj1 = new TheTestClass(false, null, true);
        TheTestClass obj2 = new TheTestClass(true, true, false);

        assertFalse(obj1.getField1());
        assertNull(obj1.getField2());
        assertTrue(obj1.getField3());

        obj1.mergeValuesFrom(obj2);

        assertTrue(obj1.getField1());
        assertTrue(obj1.getField2());
        assertTrue(obj1.getField3()); // field is marked as ignorable for merge
    }
}

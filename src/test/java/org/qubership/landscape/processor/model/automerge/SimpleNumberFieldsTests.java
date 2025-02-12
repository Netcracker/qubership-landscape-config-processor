package org.qubership.landscape.processor.model.automerge;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class SimpleNumberFieldsTests {
    private static class TheTestClass extends AutoMergeable {
        private Integer field1;
        private Long field2;
        private Float field3;
        private Number field4;

        public TheTestClass(Integer field1, Long field2, Float field3, Number field4) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
            this.field4 = field4;
        }

        public Integer getField1() {
            return field1;
        }

        public Long getField2() {
            return field2;
        }

        public Float getField3() {
            return field3;
        }

        public Number getField4() {
            return field4;
        }

        @Override
        protected boolean allowMergeFrom(AutoMergeable other) {
            return false;
        }
    }

    @Test
    public void mergeStringFieldsButIgnoreSpecifiedOne() {
        TheTestClass obj1 = new TheTestClass(100, 200L, 300.0f, new BigInteger("500000"));
        TheTestClass obj2 = new TheTestClass(111, 222L, 333.3f, new BigInteger("555555"));

        assertEquals((Integer) 100, obj1.getField1());
        assertEquals((Long) 200L, obj1.getField2());
        assertEquals((Float) 300.0f, obj1.getField3());
        assertEquals(new BigInteger("500000"), obj1.getField4());

        obj1.mergeValuesFrom(obj2);

        assertEquals((Integer) 111, obj1.getField1());
        assertEquals((Long) 222L, obj1.getField2());
        assertEquals((Float) 333.3f, obj1.getField3());
        assertEquals(new BigInteger("555555"), obj1.getField4());
    }
}

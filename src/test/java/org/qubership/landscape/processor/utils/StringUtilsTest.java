package org.qubership.landscape.processor.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringUtilsTest {
    @Test // both parameters are non-empty - first argument must be returned
    public void testGetFirstNonEmpty1() {
        String actValue = StringUtils.getFirstNonEmpty("aaa", "bbb");
        assertEquals("aaa", actValue);
    }

    @Test // first parameter is an empty string - second argument must be returned
    public void testGetFirstNonEmpty2() {
        String actValue = StringUtils.getFirstNonEmpty("", "bbb");
        assertEquals("bbb", actValue);
    }

    @Test // first parameter is null - second argument must be returned
    public void testGetFirstNonEmpty3() {
        String actValue = StringUtils.getFirstNonEmpty(null, "bbb");
        assertEquals("bbb", actValue);
    }

    @Test
    public void testIsEmpty1() {
        boolean actValue = StringUtils.isEmpty("aaa");
        assertFalse(actValue);
    }

    @Test
    public void testIsEmpty2() {
        boolean actValue = StringUtils.isEmpty("");
        assertTrue(actValue);
    }

    @Test
    public void testIsEmpty3() {
        boolean actValue = StringUtils.isEmpty("   ");
        assertTrue(actValue);
    }

    @Test
    public void testIsEmpty4() {
        boolean actValue = StringUtils.isEmpty(null);
        assertTrue(actValue);
    }
}

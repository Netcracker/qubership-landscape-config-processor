package org.qubership.landscape.processor.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListsUtils {

    /**
     * Compares two arrays that they contain same-value elements.
     * The difference to JUnit assertArraysEquals - is that order of elements may vary.
     * In case first list contains duplicates - then second list must contain same number of elements.
     * Elements comparing is based on equals() method usage.
     * In case both lists are null - means lists are equal
     * Null and empty - are treated as different states
     * @param expList first list to compare (expected semantic)
     * @param actList second list to compare (actual semantic)
     */
    public static <T> void assertListsSimilar(List<T> expList, List<T> actList) {
        if (expList == null && actList == null) return;
        if (expList == null || actList == null) throw new AssertionError("One of lists is null, another is not.");

        if (expList.size() != actList.size()) throw new AssertionError("Different lists size. Exp length = " + expList.size() + ", act size = " + actList.size());

        List<T> actListCopy = new ArrayList<>(actList);
        for (T expObj : expList) {
            boolean isRemoved = actListCopy.remove(expObj);
            if (!isRemoved) throw new AssertionError("Can't find exp element " + expObj + " in the actual list");
        }

        if (!actListCopy.isEmpty()) throw new AssertionError("There are rest elements in the actual list. Number of such elements is " + actListCopy);
    }

    /**
     * Returns mutable List of elements passed as array
     * @param array
     * @return
     * @param <T>
     */
    @SafeVarargs
    public static <T> List<T> asList(T ... array) {
        return new ArrayList<>(Arrays.asList(array));
    }
}

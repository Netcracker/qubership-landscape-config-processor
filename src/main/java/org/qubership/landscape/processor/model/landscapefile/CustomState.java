package org.qubership.landscape.processor.model.landscapefile;

import java.util.ArrayList;
import java.util.List;

public enum CustomState {
    REJECT("reject", "rejected"),
    RESEARCH("research", "researching"),
    USED("used"),
    DROP("drop", "dropped");

    private final List<String> stateList;

    CustomState(String ... states) {
        if (states == null) throw new IllegalArgumentException("At least one state string must be provided");

        this.stateList = new ArrayList<>();
        for (String next : states) {
            this.stateList.add(next.toUpperCase());
        }
    }

    /**
     * Returns true in case provided string corresponds/fits to this CustomState enum.
     * Case insensitie check
     * @param someStateToCheck
     * @return
     */
    public boolean correspondsTo(String someStateToCheck) {
        if (someStateToCheck == null) return false;

        someStateToCheck = someStateToCheck.toUpperCase().trim();
        for (String myState : stateList) {
            if (myState.equals(someStateToCheck)) return true;
        }

        return false;
    }
}

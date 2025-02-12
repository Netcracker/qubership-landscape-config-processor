package org.qubership.landscape.processor.model.landscapefile;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.qubership.landscape.processor.model.automerge.AutoMergeable;

import java.util.List;

public class TheLandscape extends AutoMergeable {
    @JsonProperty("landscape")
    private List<TheCategory> categories;

    public List<TheCategory> getCategories() {
        return categories;
    }

    @Override
    protected boolean allowMergeFrom(AutoMergeable other) {
        return false;
    }
}

package org.qubership.landscape.processor.model.landscapefile;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.qubership.landscape.processor.model.automerge.AutoMergeable;
import org.qubership.landscape.processor.model.automerge.IgnoreWhenAutoMerging;

import java.util.Objects;

public class TheOrganization extends AutoMergeable {
    @JsonProperty("organization")
    @IgnoreWhenAutoMerging
    private Object organization;

    @JsonProperty("name")
    private String name;

    public TheOrganization(String name) {
        this.name = name;
    }

    public TheOrganization() {
    }

    public String getName() {
        return name;
    }

    @Override
    protected boolean allowMergeFrom(AutoMergeable other) {
        if (other == null || getClass() != other.getClass()) return false;
        TheOrganization that = (TheOrganization) other;
        return Objects.equals(name, that.name);
    }
}

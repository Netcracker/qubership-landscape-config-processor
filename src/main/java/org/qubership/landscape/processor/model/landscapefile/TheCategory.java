package org.qubership.landscape.processor.model.landscapefile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.qubership.landscape.processor.model.automerge.AutoMergeable;
import org.qubership.landscape.processor.model.automerge.IgnoreWhenAutoMerging;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = false)
public class TheCategory extends AutoMergeable {
    @JsonProperty("category")
    @IgnoreWhenAutoMerging
    private TheCategory theCategory;

    @JsonProperty("name")
    private String name;

    @JsonProperty("subcategories")
    private List<TheSubCategory> subcategories;

    public String getName() {
        return name;
    }

    public TheCategory getCategory() {
        return theCategory;
    }

    public List<TheSubCategory> getSubcategories() {
        return subcategories;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    protected boolean allowMergeFrom(AutoMergeable other) {
        if (other == null || getClass() != other.getClass()) return false;
        TheCategory that = (TheCategory) other;
        return Objects.equals(name, that.name);
    }

}

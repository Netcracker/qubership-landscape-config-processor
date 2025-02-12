package org.qubership.landscape.processor.model.landscapefile;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.qubership.landscape.processor.model.automerge.AutoMergeable;
import org.qubership.landscape.processor.model.automerge.IgnoreWhenAutoMerging;

public class InAdditionalRepos extends AutoMergeable {
    @JsonProperty("additional_repos")
    @IgnoreWhenAutoMerging
    Object repo;

    @JsonProperty("repo_url")
    private String repos;

    @JsonProperty("branch")
    private String branch;

    public String getRepos() {
        return repos;
    }

    public String getBranch() {
        return branch;
    }

    @Override
    protected boolean allowMergeFrom(AutoMergeable other) {
        return false; // information about repo & bracnh must be always added - not merged
    }
}

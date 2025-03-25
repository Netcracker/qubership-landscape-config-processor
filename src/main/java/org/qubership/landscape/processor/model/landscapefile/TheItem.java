package org.qubership.landscape.processor.model.landscapefile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.qubership.landscape.processor.model.automerge.AutoMergeable;
import org.qubership.landscape.processor.model.automerge.ConcatenateWhenMerge;
import org.qubership.landscape.processor.model.automerge.IgnoreWhenAutoMerging;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = false)
public class TheItem extends AutoMergeable {
    @JsonProperty("item")
    @IgnoreWhenAutoMerging
    private Object item;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    @ConcatenateWhenMerge
    private String description;

    @JsonProperty("homepage_url")
    private String homepageUrl;

    @JsonProperty("logo")
    private String logo;

    @JsonProperty("crunchbase")
    private String crunchbase;

    @JsonProperty("repo_url")
    private String repoUrl;

    @JsonProperty("twitter")
    private String twitterUrl;

    @JsonProperty("project")
    private String project;

    @JsonProperty("extra")
    private InExtra extra;

    @JsonProperty("allow_duplicate_repo")
    private String allowDuplicateRepo;

    @JsonProperty("joined")
    private String joined;

    @JsonProperty("url_for_bestpractices")
    private String urlForBestPractices;

    @JsonProperty("additional_repos")
    private List<InAdditionalRepos> repoUrls;

    @JsonProperty("branch")
    private String branch;

    @JsonProperty("organization")
    private TheOrganization organization;

    @JsonProperty("unnamed_organization")
    private Boolean unnamedOrganization;

    @JsonProperty("second_path")
    private List<String> secondPaths;

    @JsonProperty("project_org")
    private String projectOrg;

    @JsonProperty("open_source")
    private String openSource;

    @JsonProperty("enduser")
    private Boolean endUser;

    @JsonProperty("stock_ticker")
    private String stockTicker;

    public String getStockTicker() {
        return stockTicker;
    }

    public Boolean getEndUser() {
        return endUser;
    }

    public String getOpenSource() {
        return openSource;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getHomepageUrl() {
        return homepageUrl;
    }

    public String getLogo() {
        return logo;
    }

    public String getCrunchbase() {
        return crunchbase;
    }

    public Object getItem() {
        return item;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public String getProject() {
        return project;
    }

    public InExtra getExtra() {
        return extra;
    }

    public String getAllowDuplicateRepo() {
        return allowDuplicateRepo;
    }

    public String getJoined() {
        return joined;
    }

    public String getUrlForBestPractices() {
        return urlForBestPractices;
    }

    public List<InAdditionalRepos> getRepoUrls() {
        return repoUrls;
    }

    public String getBranch() {
        return branch;
    }

    public Boolean getUnnamedOrganization() {
        return unnamedOrganization;
    }

    public TheOrganization getOrganization() {
        return organization;
    }

    public List<String> getSecondPaths() {
        return secondPaths;
    }

    public String getProjectOrg() {
        return projectOrg;
    }

    @Override
    protected boolean allowMergeFrom(AutoMergeable other) {
        if (other == null || getClass() != other.getClass()) return false;
        TheItem theItem = (TheItem) other;
        return Objects.equals(name, theItem.name);
    }

    @Override
    public String toString() {
        return name;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
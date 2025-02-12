package org.qubership.landscape.processor.model.landscapefile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.qubership.landscape.processor.model.automerge.AutoMergeable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InExtra extends AutoMergeable {
    @JsonProperty("accepted")
    private String accepted;

    @JsonProperty("dev_stats_url")
    private String devStatsUrl;

    @JsonProperty("artwork_url")
    private String artWorkUrl;

    @JsonProperty("clomonitor_name")
    private String clomonitorName;

    @JsonProperty("annual_review_url")
    private String annualReviewUrl;

    @JsonProperty("annual_review_date")
    private String annualReviewDate;

    @JsonProperty("slack_url")
    private String slackUrl;

    @JsonProperty("blog_url")
    private String blogUrl;

    @JsonProperty("chat_channel")
    private String chatChannel;

    public String getAccepted() {
        return accepted;
    }

    public String getDevStatsUrl() {
        return devStatsUrl;
    }

    public String getArtWorkUrl() {
        return artWorkUrl;
    }

    public String getClomonitorName() {
        return clomonitorName;
    }

    public String getAnnualReviewUrl() {
        return annualReviewUrl;
    }

    public String getAnnualReviewDate() {
        return annualReviewDate;
    }

    public String getSlackUrl() {
        return slackUrl;
    }

    public String getBlogUrl() {
        return blogUrl;
    }

    public String getChatChannel() {
        return chatChannel;
    }

    @Override
    protected boolean allowMergeFrom(AutoMergeable other) {
        return true; // InExtra instance can be only one for the parent
    }
}

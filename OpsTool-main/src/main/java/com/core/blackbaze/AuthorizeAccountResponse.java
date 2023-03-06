
package com.core.blackbaze;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthorizeAccountResponse {

    @SerializedName("absoluteMinimumPartSize")
    @Expose
    private Integer absoluteMinimumPartSize;
    @SerializedName("accountId")
    @Expose
    private String accountId;
    @SerializedName("allowed")
    @Expose
    private Allowed allowed;
    @SerializedName("apiUrl")
    @Expose
    private String apiUrl;
    @SerializedName("authorizationToken")
    @Expose
    private String authorizationToken;
    @SerializedName("downloadUrl")
    @Expose
    private String downloadUrl;
    @SerializedName("recommendedPartSize")
    @Expose
    private Integer recommendedPartSize;
    
    @SerializedName("uploadUrl")
    @Expose
    private String uploadUrl;

    public Integer getAbsoluteMinimumPartSize() {
        return absoluteMinimumPartSize;
    }

    public void setAbsoluteMinimumPartSize(Integer absoluteMinimumPartSize) {
        this.absoluteMinimumPartSize = absoluteMinimumPartSize;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Allowed getAllowed() {
        return allowed;
    }

    public void setAllowed(Allowed allowed) {
        this.allowed = allowed;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Integer getRecommendedPartSize() {
        return recommendedPartSize;
    }

    public void setRecommendedPartSize(Integer recommendedPartSize) {
        this.recommendedPartSize = recommendedPartSize;
    }

	public String getUploadUrl() {
		return uploadUrl;
	}

	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}
}


package com.core.blackbaze;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Allowed {

    @SerializedName("bucketId")
    @Expose
    private Object bucketId;
    @SerializedName("bucketName")
    @Expose
    private Object bucketName;
    @SerializedName("capabilities")
    @Expose
    private List<String> capabilities = null;
    @SerializedName("namePrefix")
    @Expose
    private Object namePrefix;

    public Object getBucketId() {
        return bucketId;
    }

    public void setBucketId(Object bucketId) {
        this.bucketId = bucketId;
    }

    public Object getBucketName() {
        return bucketName;
    }

    public void setBucketName(Object bucketName) {
        this.bucketName = bucketName;
    }

    public List<String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<String> capabilities) {
        this.capabilities = capabilities;
    }

    public Object getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(Object namePrefix) {
        this.namePrefix = namePrefix;
    }

}


package com.core.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoricalPoint {

    @SerializedName("PointInTime")
    @Expose
    private Long pointInTime;
    @SerializedName("InterbankRate")
    @Expose
    private Double interbankRate;
    @SerializedName("InverseInterbankRate")
    @Expose
    private Double inverseInterbankRate;

    public Long getPointInTime() {
        return pointInTime;
    }

    public void setPointInTime(Long pointInTime) {
        this.pointInTime = pointInTime;
    }

    public Double getInterbankRate() {
        return interbankRate;
    }

    public void setInterbankRate(Double interbankRate) {
        this.interbankRate = interbankRate;
    }

    public Double getInverseInterbankRate() {
        return inverseInterbankRate;
    }

    public void setInverseInterbankRate(Double inverseInterbankRate) {
        this.inverseInterbankRate = inverseInterbankRate;
    }

}

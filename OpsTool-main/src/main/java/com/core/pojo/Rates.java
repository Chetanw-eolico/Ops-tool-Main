
package com.core.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rates {

    @SerializedName("CurrentInterbankRate")
    @Expose
    private Double currentInterbankRate;
    @SerializedName("CurrentInverseInterbankRate")
    @Expose
    private Double currentInverseInterbankRate;
    @SerializedName("Average")
    @Expose
    private Double average;
    @SerializedName("HistoricalPoints")
    @Expose
    private List<HistoricalPoint> historicalPoints = null;

    public Double getCurrentInterbankRate() {
        return currentInterbankRate;
    }

    public void setCurrentInterbankRate(Double currentInterbankRate) {
        this.currentInterbankRate = currentInterbankRate;
    }

    public Double getCurrentInverseInterbankRate() {
        return currentInverseInterbankRate;
    }

    public void setCurrentInverseInterbankRate(Double currentInverseInterbankRate) {
        this.currentInverseInterbankRate = currentInverseInterbankRate;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public List<HistoricalPoint> getHistoricalPoints() {
        return historicalPoints;
    }

    public void setHistoricalPoints(List<HistoricalPoint> historicalPoints) {
        this.historicalPoints = historicalPoints;
    }

}

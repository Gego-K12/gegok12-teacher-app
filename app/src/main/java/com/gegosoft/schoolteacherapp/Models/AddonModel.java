package com.gegosoft.schoolteacherapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddonModel {
    @SerializedName("data")
    @Expose
    private List<AddonDatum> data = null;

    public List<AddonDatum> getData() {
        return data;
    }

    public void setData(List<AddonDatum> data) {
        this.data = data;
    }

    public class AddonDatum {
        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("purchase_status")
        @Expose
        private boolean purchaseStatus;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isPurchaseStatus() {
            return purchaseStatus;
        }

        public void setPurchaseStatus(boolean purchaseStatus) {
            this.purchaseStatus = purchaseStatus;
        }
    }
}

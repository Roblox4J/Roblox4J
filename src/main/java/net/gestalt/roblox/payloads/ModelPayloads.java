package net.gestalt.roblox.payloads;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

public interface ModelPayloads {
    @Getter
    class CreatorPayload {
        @Expose
        @SerializedName("Id")
        private long id;
        @Expose
        @SerializedName("Name")
        private String name;
        @Expose
        @SerializedName("CreatorType")
        private String creatorType;
        @Expose
        @SerializedName("CreatorTargetId")
        private long creatorTargetId;
    }
    // https://economy.roblox.com/v2/assets/%s/details
    @Getter
    class GetInfoPayload {
        @Expose
        @SerializedName("TargetId")
        private long targetId;
        @Expose
        @SerializedName("ProductType")
        private String productType;
        @Expose
        @SerializedName("AssetId")
        private long assetId;
        @Expose
        @SerializedName("ProductId")
        private long productId;
        @Expose
        @SerializedName("Name")
        private String name;
        @Expose
        @SerializedName("Description")
        private String description;
        @Expose
        @SerializedName("AssetTypeId")
        private short assetTypeId;
        @Expose
        @SerializedName("Creator")
        private CreatorPayload creator;
        @Expose
        @SerializedName("IconImageAssetId")
        private long iconImageAssetId;
        @Expose
        @SerializedName("Created")
        private String created;
        @Expose
        @SerializedName("Updated")
        private String updated;
        @Expose
        @SerializedName("PriceInRobux")
        private int priceInRobux;
        @Expose
        @SerializedName("PriceInTickets")
        private int priceInTickets;
        @Expose
        @SerializedName("Sales")
        private long sales;
        @Expose
        @SerializedName("IsNew")
        private boolean isNew;
        @Expose
        @SerializedName("IsForSale")
        private boolean isForSale;
        @Expose
        @SerializedName("IsPublicDomain")
        private boolean isPublicDomain;
        @Expose
        @SerializedName("IsLimited")
        private boolean isLimited;
        @Expose
        @SerializedName("IsLimitedUnique")
        private boolean isLimitedUnique;
        @Expose
        @SerializedName("Remaining")
        private int remaining;
        @Expose
        @SerializedName("MinimumMembershipLevel")
        private int minimumMembershipLevel;
        @Expose
        @SerializedName("ContentRatingTypeId")
        private int contentRatingTypeId;
    }
}
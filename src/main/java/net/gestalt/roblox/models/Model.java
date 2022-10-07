package net.gestalt.roblox.models;

import net.gestalt.http.OkRobloxClient;
import net.gestalt.roblox.payloads.ModelPayloads;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public record Model(long targetId, String productType, long assetId, long productId, String name, String description,
                    short assetTypeId, ModelPayloads.CreatorPayload creator, long iconImageAssetId, String created,
                    String updated, int priceInRobux, int priceInTickets, long sales, boolean isNew, boolean isForSale,
                    boolean isPublicDomain, boolean isLimited, boolean isLimitedUnique, int remaining,
                    int minimumMembershipLevel, int contentRatingTypeId, OkRobloxClient okRobloxClient) {
    /**
     * This method will toggle a favorite on the model.
     * @return The favorite mono object.
     */
    public Mono<Void> toggleFavorite() {
        // Create the request.
        Request request = new Request.Builder()
                .url("https://www.roblox.com/v2/favorite/toggle")
                .post(RequestBody.create("itemTargetId=%s&favoriteType=Asset".formatted(this.assetId),
                        MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8")))
                .build();

        return this.okRobloxClient.execute(request, null, true);
    }

    /**
     * This method will cast a vote unto the model
     * @param vote The vote.
     * @return The vote mono object.
     */
    public Mono<Void> vote(boolean vote) {
        // Create the request.
        Request request = new Request.Builder()
                .url("https://www.roblox.com/voting/vote?assetId=%s&vote=%s".formatted(this.assetId, vote))
                .post(RequestBody.create(new byte[0], null))
                .build();

        return this.okRobloxClient.execute(request, null, true);
    }

    /**
     * This method will buy the provided asset.
     * @return The buy mono object.
     */
    public Mono<Void> buy() {
        // Create the request.
        Request request = new Request.Builder()
                .url("https://economy.roblox.com/v1/purchases/products/%s".formatted(this.productId))
                .post(RequestBody.create(new byte[0], null))
                .build();

        return this.okRobloxClient.execute(request, null, true);
    }

    /**
     * This method will delete the model from the authenticated users' inventory.
     * @return The delete mono object.
     */
    public Mono<Void> delete() {
        // Create the request.
        Request request = new Request.Builder()
                .url("https://www.roblox.com/asset/delete-from-inventory")
                .post(RequestBody.create("assetId=%s".formatted(this.assetId),
                        MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8")))
                .build();

        return this.okRobloxClient.execute(request, null, true);
    }

    /**
     * This will create a model from its corresponding payload.
     * @param infoPayload The payload containing model info.
     * @param okRobloxClient The client used to send the request.
     * @return The model object.
     */
    @Contract("_, _ -> new")
    public static @NotNull Model fromData(ModelPayloads.@NotNull GetInfoPayload infoPayload,
                                          OkRobloxClient okRobloxClient) {
        return new Model(infoPayload.getTargetId(), infoPayload.getProductType(), infoPayload.getAssetId(),
                infoPayload.getProductId(), infoPayload.getName(), infoPayload.getDescription(),
                infoPayload.getAssetTypeId(), infoPayload.getCreator(), infoPayload.getIconImageAssetId(),
                infoPayload.getCreated(), infoPayload.getUpdated(), infoPayload.getPriceInRobux(),
                infoPayload.getPriceInTickets(), infoPayload.getSales(), infoPayload.isNew(), infoPayload.isForSale(),
                infoPayload.isPublicDomain(), infoPayload.isLimited(), infoPayload.isLimitedUnique(),
                infoPayload.getRemaining(), infoPayload.getMinimumMembershipLevel(),
                infoPayload.getContentRatingTypeId(), okRobloxClient);
    }
}
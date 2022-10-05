package net.gestalt.roblox.games;

import net.gestalt.http.OkRobloxClient;
import net.gestalt.roblox.payloads.GamePayloads;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Game(long id, long rootPlaceId, String name, String description, String sourceName,
                   String sourceDescription, String created, String updated, String universeAvatarType, String genre,
                   int price, int playing, long visits, int maxPlayers, int favoriteCount,
                   String[] allowedGearGenres, String[] allowedGearCategories, boolean isGenreEnforced,
                   boolean copyingAllowed, boolean studioAccessToApisAllowed, boolean createVipServersAllowed,
                   boolean isAllGenre, boolean isFavoritedByUser, OkRobloxClient okRobloxClient) {
    @Contract("_, _ -> new")
    public static @NotNull Game fromData(GamePayloads.@NotNull UniversePayload universePayload, OkRobloxClient okRobloxClient) {
        return new Game(universePayload.getId(), universePayload.getRootPlaceId(), universePayload.getName(),
                universePayload.getDescription(), universePayload.getSourceName(), universePayload.getSourceDescription(),
                universePayload.getCreated(), universePayload.getUpdated(), universePayload.getUniverseAvatarType(),
                universePayload.getGenre(), universePayload.getPrice(), universePayload.getPlaying(), universePayload.getVisits(),
                universePayload.getMaxPlayers(), universePayload.getFavoriteCount(), universePayload.getAllowedGearGenres(),
                universePayload.getAllowedGearCategories(), universePayload.isGenreEnforced(), universePayload.isCopyingAllowed(),
                universePayload.isStudioAccessToApisAllowed(), universePayload.isCreateVipServersAllowed(),
                universePayload.isAllGenre(), universePayload.isFavoritedByUser(), okRobloxClient);
    }
}
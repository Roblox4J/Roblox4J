package net.gestalt.roblox.payloads;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

public interface GamePayloads {
    @Getter
    class UniverseContainingPlacePayload {
        @Expose
        @SerializedName("UniverseId")
        long universeId;
    }
    @Getter
    class UniversePayload {
        @Expose
        private long id, rootPlaceId, visits;
        @Expose
        private String name, description, sourceName, sourceDescription, created, updated, universeAvatarType, genre;
        @Expose
        private int price, playing, maxPlayers, favoriteCount;
        @Expose
        private String[] allowedGearGenres, allowedGearCategories;
        @Expose
        private boolean isGenreEnforced, copyingAllowed, studioAccessToApisAllowed, createVipServersAllowed, isAllGenre,
                isFavoritedByUser;
    }
    @Getter
    class GetUniversesPayload {
        @Expose
        private UniversePayload[] data;
    }
}
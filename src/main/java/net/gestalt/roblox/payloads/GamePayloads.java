package net.gestalt.roblox.payloads;

import com.google.gson.annotations.Expose;
import lombok.Getter;

public interface GamePayloads {
    @Getter
    class UniverseContainingPlacePayload {
        @Expose
        long universeId;
    }
    @Getter
    class UniversePayload {
        @Expose
        private long id, rootPlaceId;
        @Expose
        private String name, description, sourceName, sourceDescription, created, updated, universeAvatarType, genre;
        @Expose
        private int price, playing, visits, maxPlayers, favoriteCount;
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
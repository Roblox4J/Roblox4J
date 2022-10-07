package roblox4j.roblox.payloads;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

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
    @Getter
    @Setter
    class SetVotePayload {
        @Expose
        private boolean vote;
    }
    @Getter
    @Setter
    class SetFavoritedPayload {
        @Expose
        private boolean isFavorited;
    }
}
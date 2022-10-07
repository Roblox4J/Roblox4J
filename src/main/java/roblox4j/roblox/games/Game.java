package roblox4j.roblox.games;

import com.google.gson.Gson;
import roblox4j.exceptions.InvalidRequestException;
import roblox4j.exceptions.NoPermissionException;
import roblox4j.exceptions.RateLimitException;
import roblox4j.http.OkRobloxClient;
import roblox4j.roblox.payloads.GamePayloads;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public record Game(long id, long rootPlaceId, String name, String description, String sourceName,
                   String sourceDescription, String created, String updated, String universeAvatarType, String genre,
                   int price, int playing, long visits, int maxPlayers, int favoriteCount,
                   String[] allowedGearGenres, String[] allowedGearCategories, boolean isGenreEnforced,
                   boolean copyingAllowed, boolean studioAccessToApisAllowed, boolean createVipServersAllowed,
                   boolean isAllGenre, boolean isFavoritedByUser, long universeId, OkRobloxClient okRobloxClient) {
    private static final Gson GSON = new Gson();

    /**
     * This method will place a vote on the provided game.
     * @param vote The users' vote on the game.
     * @return The vote mono object.
     */
    public @NotNull Mono<Void> vote(boolean vote) {
        // Make the payload from the parameters.
        GamePayloads.SetVotePayload setVotePayload = new GamePayloads.SetVotePayload();
        setVotePayload.setVote(vote);

        // Create the request.
        Request request = new Request.Builder()
                .url("https://games.roblox.com/v1/games/%s/user-votes".formatted(this.universeId))
                .patch(RequestBody.create(GSON.toJson(setVotePayload), MediaType.parse("application/json")))
                .build();

        return this.okRobloxClient.<Void>execute(request, null, true)
                .onErrorResume(InvalidRequestException.class, e -> switch (e.getCode()) {
                    case 5 -> Mono.error(RateLimitException::new);
                    case 6 -> Mono.error(NoPermissionException::new);
                    default -> Mono.error(e);
                });
    }

    /**
     * This method will favorite the provided game.
     * @param favorite Whether the user is favouring the game or not.
     * @return The favorite mono object.
     */
    public Mono<Void> favorite(boolean favorite) {
        // Create the payload.
        GamePayloads.SetFavoritedPayload setFavoritedPayload = new GamePayloads.SetFavoritedPayload();
        setFavoritedPayload.setFavorited(favorite);

        // Make the request.
        Request request = new Request.Builder()
                .url("https://games.roblox.com/v1/games/%s/favorites".formatted(this.universeId))
                .post(RequestBody.create(GSON.toJson(setFavoritedPayload),
                        MediaType.parse("application/json")))
                .build();

        return this.okRobloxClient.execute(request, null, true);
    }

    /**
     * This method will load a game from its corresponding data.
     * @param universePayload The payload containing the game information.
     * @param universeId The id of the universe.
     * @param okRobloxClient The Roblox client.
     * @return The game object.
     */
    @Contract("_, _, _ -> new")
    public static @NotNull Game fromData(GamePayloads.@NotNull UniversePayload universePayload, long universeId, OkRobloxClient okRobloxClient) {
        return new Game(universePayload.getId(), universePayload.getRootPlaceId(), universePayload.getName(),
                universePayload.getDescription(), universePayload.getSourceName(), universePayload.getSourceDescription(),
                universePayload.getCreated(), universePayload.getUpdated(), universePayload.getUniverseAvatarType(),
                universePayload.getGenre(), universePayload.getPrice(), universePayload.getPlaying(), universePayload.getVisits(),
                universePayload.getMaxPlayers(), universePayload.getFavoriteCount(), universePayload.getAllowedGearGenres(),
                universePayload.getAllowedGearCategories(), universePayload.isGenreEnforced(), universePayload.isCopyingAllowed(),
                universePayload.isStudioAccessToApisAllowed(), universePayload.isCreateVipServersAllowed(),
                universePayload.isAllGenre(), universePayload.isFavoritedByUser(), universeId, okRobloxClient);
    }
}
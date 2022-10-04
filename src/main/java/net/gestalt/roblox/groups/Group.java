package net.gestalt.roblox.groups;

import com.google.gson.Gson;
import net.gestalt.exceptions.*;
import net.gestalt.http.OkRobloxClient;
import net.gestalt.roblox.accounts.Account;
import net.gestalt.roblox.payloads.GroupPayloads;
import net.gestalt.utils.ExcludeFromJacocoGeneratedReport;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import javax.naming.NoPermissionException;

public record Group(long id, String name, String description, int memberCount, boolean isBuildersClubOnly,
                    boolean publicEntryAllowed, boolean isLocked, boolean hasVerifiedBadge, Owner owner, Shout shout,
                    OkRobloxClient okRobloxClient) {
    private static final Gson GSON = new Gson();

    /**
     * This method will set the name of the group. The authenticated user must own the group.
     * @param name The new name of the group.
     * @return A mono object that'll set the new name.
     */
    public @NotNull Mono<Void> setName(String name) {
        // Set up the payload.
        GroupPayloads.SetNamePayload setNamePayload = new GroupPayloads.SetNamePayload();
        setNamePayload.setName(name);

        // Create the request.
        Request request = new Request.Builder()
                .url("https://groups.roblox.com/v1/groups/%s/name".formatted(this.id))
                .patch(RequestBody.create(GSON.toJson(setNamePayload), MediaType.parse("application/json")))
                .build();

        return this.okRobloxClient.<Void>execute(request, null, true)
                .onErrorResume(InvalidRequestException.class, e -> switch (e.getCode()) {
                    case 12 -> Mono.error(InsufficientRobuxException::new);
                    case 14 -> Mono.error(ModeratedException::new);
                    case 20 -> Mono.error(FieldTakenException::new);
                    case 23 -> Mono.error(NoPermissionException::new);
                    case 36 -> Mono.error(ChangedTooRecentlyException::new);
                    default -> Mono.error(e);
                });
    }

    /**
     * This method will set the group description. The authenticated account must have access.
     * @param description The new description.
     * @return Mono object to set the description.
     */
    public @NotNull Mono<Void> setDescription(String description) {
        // Set up the payload.
        GroupPayloads.SetDescriptionPayload setDescriptionPayload = new GroupPayloads.SetDescriptionPayload();
        setDescriptionPayload.setDescription(description);

        // Make the request.
        Request request = new Request.Builder()
                .url("https://groups.roblox.com/v1/groups/%s/description".formatted(this.id))
                .patch(RequestBody.create(GSON.toJson(setDescriptionPayload),
                        MediaType.parse("application/json")))
                .build();

        return this.okRobloxClient.<Void>execute(request, null, true)
                .onErrorResume(InvalidRequestException.class, e -> switch (e.getCode()) {
                    case 29 -> Mono.error(EmptyException::new);
                    case 18 -> Mono.error(FieldTooLongException::new);
                    case 23 -> Mono.error(NoPermissionException::new);
                    case 32 -> Mono.error(ModeratedException::new);
                    default -> Mono.error(e);
        });
    }

    /**
     * This method will send a shout. The authenticated user must have permission to do this.
     * @param shout The group shout.
     * @return The shout mono object.
     */
    public @NotNull Mono<Void> sendShout(String shout) {
        // Set up the payload.
        GroupPayloads.SendShoutPayload sendShoutPayload = new GroupPayloads.SendShoutPayload();
        sendShoutPayload.setMessage(shout);

        // Make the request.
        Request request = new Request.Builder()
                .url("https://groups.roblox.com/v1/groups/%s/status".formatted(this.id))
                .patch(RequestBody.create(GSON.toJson(sendShoutPayload), MediaType.parse("application/json")))
                .build();

        return this.okRobloxClient.<Void>execute(request, null, true)
                .onErrorResume(InvalidRequestException.class, e -> switch (e.getCode()) {
                    case 6 -> Mono.error(NoPermissionException::new);
                    case 7 -> Mono.error(EmptyException::new);
                    default -> Mono.error(e);
                });
    }

    /**
     * This method will accept the provided id into the group.
     * @param id The id of the account.
     * @return The mono object to accept the user.
     */
    public @NotNull Mono<Void> acceptAccountRequest(String id) {
        Request request = new Request.Builder()
                .url("https://groups.roblox.com/v1/groups/%s/join-requests/users/%s".formatted(this.id, id))
                .post(RequestBody.create(new byte[0], null))
                .build();

        return this.okRobloxClient.<Void>execute(request, null, true)
                .onErrorResume(InvalidRequestException.class, e -> switch (e.getCode()) {
                    case 3 -> Mono.error(InvalidIdException::new);
                    case 19 -> Mono.error(NoPermissionException::new);
                    case 20 -> Mono.error(NotPendingException::new);
                    default -> Mono.error(e);
                });
    }

    /**
     * This method will accept the provided account into the group.
     * @param id The id of the account.
     * @return Accept mono object.
     */
    @SuppressWarnings("unused")
    @ExcludeFromJacocoGeneratedReport
    public @NotNull Mono<Void> acceptAccountRequest(long id) {
        return this.acceptAccountRequest(String.valueOf(id));
    }

    /**
     * This method will accept the account into the group.
     * @param account The account being accepted.
     * @return Accept mono object.
     */
    @SuppressWarnings("unused")
    @ExcludeFromJacocoGeneratedReport
    public @NotNull Mono<Void> acceptAccountRequest(@NotNull Account account) {
        return this.acceptAccountRequest(String.valueOf(account.id()));
    }

    /**
     * This method will kick an account from the group.
     * @param id The id of the account.
     * @return Mono object that'll kick the account.
     */
    public @NotNull Mono<Void> kickAccount(String id) {
        Request request = new Request.Builder()
                .url("https://groups.roblox.com/v1/groups/%s/users/%s".formatted(this.id, id))
                .delete()
                .build();

        return this.okRobloxClient.<Void>execute(request, null, true)
                .onErrorResume(InvalidRequestException.class, e -> switch (e.getCode()) {
                    case 3 -> Mono.error(InvalidIdException::new);
                    case 4 -> Mono.error(NoPermissionException::new);
                    default -> Mono.error(e);
                });
    }

    /**
     * This method will kick an account from the group.
     * @param id The id of the account.
     * @return Kick mono object.
     */
    @SuppressWarnings("unused")
    @ExcludeFromJacocoGeneratedReport
    public @NotNull Mono<Void> kickAccount(long id) {
        return this.kickAccount(String.valueOf(id));
    }

    /**
     * This method will kick the provided account from the group.
     * @param account The account being kicked from the group.
     * @return Kick mono object.
     */
    @SuppressWarnings("unused")
    @ExcludeFromJacocoGeneratedReport
    public @NotNull Mono<Void> kickAccount(@NotNull Account account) {
        return this.kickAccount(String.valueOf(account.id()));
    }

    /**
     * This method will set the new owner of the group. The authenticated user must own the group.
     * @param id The new owner.
     * @return The set owner mono object.
     */
    public @NotNull Mono<Void> changeOwner(String id) {
        // Create the payload.
        GroupPayloads.SetOwnerPayload setOwnerPayload = new GroupPayloads.SetOwnerPayload();
        setOwnerPayload.setUserId(Long.parseLong(id));

        Request request = new Request.Builder()
                .url("https://groups.roblox.com/v1/groups/%s/change-owner".formatted(this.id))
                .post(RequestBody.create(GSON.toJson(setOwnerPayload), MediaType.parse("application/json")))
                .build();

        return this.okRobloxClient().<Void>execute(request, null, true)
                .onErrorResume(InvalidRequestException.class, e -> switch (e.getCode()) {
                    case 15 -> Mono.error(NotMemberException::new);
                    case 17 -> Mono.error(NoPermissionException::new);
                    default -> Mono.error(e);
                });
    }

    /**
     * This method will set the new owner of a group. The authenticated user must own the group.
     * @param id The id of the account.
     * @return The kick set owner mono object.
     */
    @SuppressWarnings("unused")
    @ExcludeFromJacocoGeneratedReport
    public @NotNull Mono<Void> changeOwner(long id) {
        return this.changeOwner(String.valueOf(id));
    }

    /**
     * This method will set the provided account to the new owner of the group.
     * @param account The account being set to the owner.
     * @return The kick set owner mono object.
     */
    @SuppressWarnings("unused")
    @ExcludeFromJacocoGeneratedReport
    public @NotNull Mono<Void> changeOwner(@NotNull Account account) {
        return this.changeOwner(String.valueOf(account.id()));
    }

    /**
     * This method will claim the group if there is no owner.
     * @return The join group mono object.
     */
    public @NotNull Mono<Void> claimOwnership() {
        Request request = new Request.Builder()
                .url("https://groups.roblox.com/v1/groups/%s/claim-ownership".formatted(this.id))
                .post(RequestBody.create(new byte[0], null))
                .build();

        return this.okRobloxClient.<Void>execute(request, null, true)
                .onErrorResume(InvalidRequestException.class, e -> switch (e.getCode()) {
                    case 11 -> Mono.error(NoPermissionException::new);
                    case 12 -> Mono.error(AlreadyOwnedException::new);
                    case 13 -> Mono.error(RateLimitException::new);
                    default -> Mono.error(e);
                });
    }

    /**
     * This method will load a group object from a payload.
     * @param groupPayload The payload.
     * @param okRobloxClient The Roblox HTTP client used to receive the payload.
     * @return The corresponding group object.
     */
    @Contract("_, _ -> new")
    public static @NotNull Group fromData(GroupPayloads.@NotNull GetGroupPayload groupPayload,
                                          OkRobloxClient okRobloxClient) {
        return new Group(groupPayload.getId(), groupPayload.getName(), groupPayload.getDescription(),
                groupPayload.getMemberCount(), groupPayload.isBuildersClubOnly(), groupPayload.isPublicEntryAllowed(),
                groupPayload.isLocked(), groupPayload.isHasVerifiedBadge(), groupPayload.getOwner(),
                groupPayload.getShout(), okRobloxClient);
    }
}
package roblox4j.roblox.payloads;

import com.google.gson.annotations.Expose;
import lombok.Getter;

public interface FriendPayloads {
   @Getter
   class GetFriendsPayload {
       @Getter
       public static class Data {
           @Expose
           private boolean isOnline, isDeleted, hasVerifiedBadge, isBanned;
           @Expose
           private int presenceType, friendFrequentScore, friendFrequentRank;
           @Expose
           private String description, created, externalAppDisplayName, name, displayName;
           @Expose
           private long id;
       }
       @Expose
       Data[] data;
   }
}
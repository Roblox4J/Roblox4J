package net.gestalt.roblox.payloads;

public interface CursorInterface<T> {
    String getNextPageCursor();
    T[] getData();
}
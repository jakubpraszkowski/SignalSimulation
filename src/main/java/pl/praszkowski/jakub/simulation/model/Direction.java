package pl.praszkowski.jakub.simulation.model;

import org.jetbrains.annotations.NotNull;

public enum Direction {
    NORTH, SOUTH, EAST, WEST;

    public static Direction fromString(@NotNull String dir) {
        return switch (dir.trim().toUpperCase()) {
            case "NORTH" -> NORTH;
            case "SOUTH" -> SOUTH;
            case "EAST" -> EAST;
            case "WEST" -> WEST;
            default -> throw new IllegalArgumentException("Unknown direction: " + dir);
        };
    }
}

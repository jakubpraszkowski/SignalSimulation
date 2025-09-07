package pl.praszkowski.jakub.simulation.model;

import java.util.Map;
import java.util.Set;

public class ConflictMatrix {
    public static final Map<Direction, Set<Direction>> CONFLICTS = Map.of(
            Direction.NORTH, Set.of(Direction.EAST, Direction.WEST),
            Direction.SOUTH, Set.of(Direction.EAST, Direction.WEST),
            Direction.EAST,  Set.of(Direction.NORTH, Direction.SOUTH),
            Direction.WEST,  Set.of(Direction.NORTH, Direction.SOUTH)
    );
}

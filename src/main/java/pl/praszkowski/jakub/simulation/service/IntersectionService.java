package pl.praszkowski.jakub.simulation.service;

import org.springframework.stereotype.Service;
import pl.praszkowski.jakub.simulation.model.*;

import java.util.*;

import static pl.praszkowski.jakub.simulation.model.ConflictMatrix.CONFLICTS;

@Service
public class IntersectionService {

    private final Map<Direction, Road> roads = new EnumMap<>(Direction.class);
    private final Set<Direction> greenDirections = EnumSet.noneOf(Direction.class);
    private int greenTimeCounter = 0;
    private static final int MAX_GREEN_TIME = 5;

    public IntersectionService() {
        for (Direction dir : Direction.values()) {
            roads.put(dir, new Road());
        }
        greenDirections.add(Direction.NORTH);
        greenDirections.add(Direction.SOUTH);
        roads.get(Direction.NORTH).setLightState(TrafficLightState.GREEN);
        roads.get(Direction.SOUTH).setLightState(TrafficLightState.GREEN);
        for (Direction dir : Direction.values()) {
            if (!greenDirections.contains(dir)) {
                roads.get(dir).setLightState(TrafficLightState.RED);
            }
        }
    }

    public void addVehicle(Vehicle vehicle) {
        roads.get(vehicle.start()).addVehicle(vehicle);
    }

    public StepStatus step() {
        List<String> leftVehicles = new ArrayList<>();
        boolean lightsChanged = false;

        // Sprawdzenie, czy trzeba zmienić światła
        Set<Direction> directionsToChange = new HashSet<>();
        for (Direction dir : greenDirections) {
            Road road = roads.get(dir);
            if (road.isEmpty() || greenTimeCounter >= MAX_GREEN_TIME) {
                directionsToChange.add(dir);
            }
        }

        if (!directionsToChange.isEmpty()) {
            Set<Direction> nextGreen = chooseGreenDirections();
            for (Direction dir : greenDirections) {
                roads.get(dir).setLightState(TrafficLightState.RED);
            }
            greenDirections.clear();
            greenDirections.addAll(nextGreen);
            for (Direction dir : greenDirections) {
                roads.get(dir).setLightState(TrafficLightState.GREEN);
            }
            greenTimeCounter = 0;
            lightsChanged = true;
        }

        for (Direction dir : greenDirections) {
            Road road = roads.get(dir);
            if (!road.isEmpty() && road.checkIfCanPass()) {
                QueueEntry entry = road.pollVehicle();
                leftVehicles.add(entry.vehicle().id());
            }
        }

        incrementWaitingForOthers();
        if (!lightsChanged) {
            greenTimeCounter++;
        }

        return new StepStatus(leftVehicles);
    }

    private void incrementWaitingForOthers() {
        for (Direction dir : Direction.values()) {
            if (!greenDirections.contains(dir)) {
                roads.get(dir).incrementWaitingTimeForAll();
            }
        }
    }

    private Set<Direction> chooseGreenDirections() {
        Set<Direction> nextGreen = new HashSet<>();
        List<Map.Entry<Direction, Road>> sortedRoads = roads.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> -e.getValue().getWaitingVehiclesCount()))
                .toList();
        for (Map.Entry<Direction, Road> entry : sortedRoads) {
            Direction candidate = entry.getKey();
            if (canSetGreen(candidate, nextGreen)) {
                nextGreen.add(candidate);
            }
        }
        if (nextGreen.isEmpty()) {
            return Collections.singleton(Direction.NORTH);
        }
        return nextGreen;
    }

    private boolean canSetGreen(Direction candidate, Set<Direction> currentlyGreen) {
        Set<Direction> conflicts = CONFLICTS.getOrDefault(candidate, Collections.emptySet());
        for (Direction greenDir : currentlyGreen) {
            if (conflicts.contains(greenDir)) {
                return false;
            }
        }
        return true;
    }
}
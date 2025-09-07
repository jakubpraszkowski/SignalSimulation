package pl.praszkowski.jakub.simulation.model;

import lombok.Getter;
import lombok.Setter;
import java.util.LinkedList;
import java.util.Queue;

@Getter
public class Road {
    private final Queue<QueueEntry> queue = new LinkedList<>();

    @Setter
    private TrafficLightState lightState = TrafficLightState.RED;

    public void addVehicle(Vehicle vehicle) {
        queue.add(new QueueEntry(vehicle, 0));
    }

    public QueueEntry pollVehicle() {
        return queue.poll();
    }

    public void incrementWaitingTimeForAll() {
        Queue<QueueEntry> temp = new LinkedList<>();
        while (!queue.isEmpty()) {
            QueueEntry entry = queue.poll();
            temp.add(new QueueEntry(entry.vehicle(), entry.waitingTime() + 1));
        }
        queue.addAll(temp);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int getWaitingVehiclesCount() {
        return queue.size();
    }

    public boolean checkIfCanPass() {
        return (lightState == TrafficLightState.GREEN ||
                lightState == TrafficLightState.GREEN_LEFT ||
                lightState == TrafficLightState.GREEN_RIGHT) &&
                !queue.isEmpty();
    }
}
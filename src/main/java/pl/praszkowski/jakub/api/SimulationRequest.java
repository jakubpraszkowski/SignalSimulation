package pl.praszkowski.jakub.api;

import java.util.List;

public record SimulationRequest(List<Command> commands) {

    public record Command(
            String type,
            String vehicleId,
            String startRoad,
            String endRoad
    ) {}
}

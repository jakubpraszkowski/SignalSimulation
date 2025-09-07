package pl.praszkowski.jakub.simulation.utils;

import pl.praszkowski.jakub.api.SimulationRequest;
import pl.praszkowski.jakub.simulation.model.Direction;
import pl.praszkowski.jakub.simulation.model.StepStatus;
import pl.praszkowski.jakub.simulation.model.Vehicle;
import pl.praszkowski.jakub.simulation.service.IntersectionService;

import java.util.ArrayList;
import java.util.List;

public class SimulationRunner {

    private SimulationRunner() {}

    public static List<StepStatus> runSimulation(SimulationRequest request, IntersectionService service) {
        List<StepStatus> results = new ArrayList<>();

        for (SimulationRequest.Command cmd : request.commands()) {
            switch (cmd.type().toLowerCase()) {
                case "addvehicle":
                    Vehicle vehicle = new Vehicle(
                            cmd.vehicleId(),
                            Direction.fromString(cmd.startRoad()),
                            Direction.fromString(cmd.endRoad()),
                            null
                    );
                    service.addVehicle(vehicle);
                    break;

                case "step":
                    results.add(service.step());
                    break;

                default:
                    throw new IllegalArgumentException("Unknown command type: " + cmd.type());
            }
        }

        return results;
    }
}


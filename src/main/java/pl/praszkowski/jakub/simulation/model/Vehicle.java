package pl.praszkowski.jakub.simulation.model;

import java.util.Optional;

public record Vehicle(String id, Direction start, Direction end,
                      Optional<VehicleType> vehicleType) {}

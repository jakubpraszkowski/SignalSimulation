package pl.praszkowski.jakub.simulation.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum VehicleType {
    CAR(1),
    BUS(2),
    AMBULANCE(3);

    @Getter
    private final int priority;
}

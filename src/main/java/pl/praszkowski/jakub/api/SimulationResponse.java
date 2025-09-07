package pl.praszkowski.jakub.api;

import pl.praszkowski.jakub.simulation.model.StepStatus;

import java.util.List;

public record SimulationResponse(List<StepStatus> stepStatuses) {}

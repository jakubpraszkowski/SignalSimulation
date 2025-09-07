package pl.praszkowski.jakub.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.praszkowski.jakub.simulation.model.StepStatus;
import pl.praszkowski.jakub.simulation.service.IntersectionService;
import pl.praszkowski.jakub.simulation.utils.SimulationRunner;

import java.util.List;

@RestController
@RequestMapping("/simulation")
@RequiredArgsConstructor
public class SimulationController {

    private final IntersectionService intersectionService;

    @PostMapping
    public SimulationResponse runSimulation(@RequestBody SimulationRequest request) {
        List<StepStatus> results = SimulationRunner.runSimulation(request, intersectionService);
        return new SimulationResponse(results);
    }
}

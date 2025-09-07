package pl.praszkowski.jakub.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.praszkowski.jakub.api.SimulationRequest;
import pl.praszkowski.jakub.api.SimulationResponse;
import pl.praszkowski.jakub.simulation.model.StepStatus;
import pl.praszkowski.jakub.simulation.service.IntersectionService;
import pl.praszkowski.jakub.simulation.utils.SimulationRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CLIMain {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: java -jar simulation.jar <input.json> <output.json>");
            System.exit(1);
        }

        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);

        ObjectMapper mapper = new ObjectMapper();
        SimulationRequest request = mapper.readValue(inputFile, SimulationRequest.class);

        IntersectionService intersectionService = new IntersectionService();

        List<StepStatus> results = SimulationRunner.runSimulation(request, intersectionService);

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(outputFile, new SimulationResponse(results));

        System.out.println("Simulation finished. Output written to " + outputFile.getAbsolutePath());
    }
}

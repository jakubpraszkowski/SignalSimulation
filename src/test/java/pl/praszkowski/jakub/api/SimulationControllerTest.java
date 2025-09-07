package pl.praszkowski.jakub.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.praszkowski.jakub.simulation.model.StepStatus;
import pl.praszkowski.jakub.simulation.service.IntersectionService;
import pl.praszkowski.jakub.simulation.utils.SimulationRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimulationControllerTest {

    @Mock
    private IntersectionService intersectionService;

    @InjectMocks
    private SimulationController simulationController;

    private SimulationRequest request;
    private List<StepStatus> expectedStepStatuses;

    @BeforeEach
    void setUp() {
        SimulationRequest.Command command = new SimulationRequest.Command(
                "MOVE",
                "vehicle-1",
                "NORTH",
                "SOUTH"
        );

        request = new SimulationRequest(List.of(command));

        StepStatus stepStatus = new StepStatus(List.of("vehicle-1", "vehicle-2"));
        expectedStepStatuses = List.of(stepStatus);
    }

    @Test
    void runSimulation_ShouldReturnSimulationResponse() {
        // Given
        try (var mockedSimulationRunner = mockStatic(SimulationRunner.class)) {
            mockedSimulationRunner.when(() -> SimulationRunner.runSimulation(request, intersectionService))
                    .thenReturn(expectedStepStatuses);

            // When
            SimulationResponse response = simulationController.runSimulation(request);

            // Then
            assertNotNull(response);
            assertEquals(expectedStepStatuses, response.stepStatuses());
            mockedSimulationRunner.verify(() -> SimulationRunner.runSimulation(request, intersectionService));
        }
    }

    @Test
    void runSimulation_WithEmptyCommands_ShouldReturnEmptyResponse() {
        // Given
        SimulationRequest emptyRequest = new SimulationRequest(List.of());

        try (var mockedSimulationRunner = mockStatic(SimulationRunner.class)) {
            mockedSimulationRunner.when(() -> SimulationRunner.runSimulation(emptyRequest, intersectionService))
                    .thenReturn(List.of());

            // When
            SimulationResponse response = simulationController.runSimulation(emptyRequest);

            // Then
            assertNotNull(response);
            assertTrue(response.stepStatuses().isEmpty());
        }
    }

    @Test
    void runSimulation_WithMultipleCommands_ShouldReturnResponseWithMultipleStatuses() {
        // Given
        SimulationRequest.Command command1 = new SimulationRequest.Command(
                "MOVE",
                "vehicle-1",
                "NORTH",
                "SOUTH"
        );
        SimulationRequest.Command command2 = new SimulationRequest.Command(
                "STOP",
                "vehicle-2",
                "EAST",
                "WEST"
        );

        SimulationRequest multiRequest = new SimulationRequest(List.of(command1, command2));

        StepStatus status1 = new StepStatus(List.of("vehicle-1"));
        StepStatus status2 = new StepStatus(List.of("vehicle-2"));
        List<StepStatus> multipleStatuses = List.of(status1, status2);

        try (var mockedSimulationRunner = mockStatic(SimulationRunner.class)) {
            mockedSimulationRunner.when(() -> SimulationRunner.runSimulation(multiRequest, intersectionService))
                    .thenReturn(multipleStatuses);

            // When
            SimulationResponse response = simulationController.runSimulation(multiRequest);

            // Then
            assertNotNull(response);
            assertEquals(2, response.stepStatuses().size());
            assertEquals(multipleStatuses, response.stepStatuses());
        }
    }

    @Test
    void simulationRequest_Command_ShouldHaveCorrectFields() {
        // Given
        String type = "MOVE";
        String vehicleId = "test-vehicle";
        String startRoad = "NORTH";
        String endRoad = "SOUTH";

        // When
        SimulationRequest.Command command = new SimulationRequest.Command(type, vehicleId, startRoad, endRoad);

        // Then
        assertEquals(type, command.type());
        assertEquals(vehicleId, command.vehicleId());
        assertEquals(startRoad, command.startRoad());
        assertEquals(endRoad, command.endRoad());
    }

    @Test
    void simulationResponse_ShouldHaveCorrectStepStatuses() {
        // Given
        StepStatus status = new StepStatus(List.of("vehicle-1", "vehicle-2"));
        List<StepStatus> statuses = List.of(status);

        // When
        SimulationResponse response = new SimulationResponse(statuses);

        // Then
        assertNotNull(response);
        assertEquals(statuses, response.stepStatuses());
        assertEquals(1, response.stepStatuses().size());
        assertEquals(status, response.stepStatuses().get(0));
        assertEquals(2, response.stepStatuses().get(0).leftVehicles().size());
        assertTrue(response.stepStatuses().get(0).leftVehicles().contains("vehicle-1"));
    }
}
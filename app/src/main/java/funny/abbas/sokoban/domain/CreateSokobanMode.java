package funny.abbas.sokoban.domain;

import java.util.Optional;

import funny.abbas.sokoban.state.createsokoban.CreateSokobanAction;
import funny.abbas.sokoban.state.createsokoban.CreateSokobanState;

public class CreateSokobanMode {
    private CreateSokobanState lastState = CreateSokobanState.PUT_BASIC;
    private CreateSokobanState currentState = CreateSokobanState.PUT_BASIC;

    public Optional<CreateSokobanState> transferStatusByEvent(CreateSokobanAction event) {
        CreateSokobanState targetStatus = CreateSokobanStateMachine.getTargetStatus(currentState, event);
        if (targetStatus != null) {
            lastState = currentState;
            currentState = targetStatus;
            return Optional.of(currentState);
        }
        return Optional.empty();
    }

    public CreateSokobanState getLastState() {
        return lastState;
    }

    public CreateSokobanState getCurrentState() {
        return currentState;
    }
}

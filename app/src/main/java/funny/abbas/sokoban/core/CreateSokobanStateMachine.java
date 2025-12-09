package funny.abbas.sokoban.core;

import funny.abbas.sokoban.state.StateMachine;
import funny.abbas.sokoban.state.createsokoban.CreateSokobanAction;
import funny.abbas.sokoban.state.createsokoban.CreateSokobanState;

public class CreateSokobanStateMachine {
    private CreateSokobanStateMachine() {
    }

    private static final StateMachine<CreateSokobanState, CreateSokobanAction> stateMachine
             = new StateMachine<>();

    static {
        stateMachine.accept(CreateSokobanState.PUT_BASIC,CreateSokobanAction.TRANSFORM,CreateSokobanState.PUT_TARGET);
        stateMachine.accept(CreateSokobanState.PUT_TARGET,CreateSokobanAction.TRANSFORM,CreateSokobanState.PUT_BOX);
        stateMachine.accept(CreateSokobanState.PUT_BOX,CreateSokobanAction.TRANSFORM,CreateSokobanState.PUT_ROLE);

        stateMachine.accept(CreateSokobanState.PUT_ROLE,CreateSokobanAction.BACK,CreateSokobanState.PUT_BOX);
        stateMachine.accept(CreateSokobanState.PUT_BOX,CreateSokobanAction.BACK,CreateSokobanState.PUT_TARGET);
        stateMachine.accept(CreateSokobanState.PUT_TARGET,CreateSokobanAction.BACK,CreateSokobanState.PUT_BASIC);
    }

    public static CreateSokobanState getTargetStatus(CreateSokobanState status, CreateSokobanAction event) {
        return stateMachine.getTargetStatus(status, event);
    }

}

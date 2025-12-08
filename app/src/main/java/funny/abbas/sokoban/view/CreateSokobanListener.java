package funny.abbas.sokoban.view;

import funny.abbas.sokoban.state.createsokoban.CreateSokobanState;

public interface CreateSokobanListener {

    void onStateChange(CreateSokobanState newState);
}

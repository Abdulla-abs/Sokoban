package funny.abbas.sokoban.core;

import funny.abbas.sokoban.state.createsokoban.CreateSokobanState;

public interface CreateSokobanListener {

    void onStateChange(CreateSokobanState newState);
}

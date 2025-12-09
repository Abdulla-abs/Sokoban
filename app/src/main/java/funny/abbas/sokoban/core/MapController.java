package funny.abbas.sokoban.core;

import java.util.Optional;

public interface MapController {

    Optional<MapObject> getObjectFromPosition(int x, int y);
}

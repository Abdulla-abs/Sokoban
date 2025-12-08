package funny.abbas.sokoban.domain;

import java.util.Optional;

public interface MapController {

    Optional<MapObject> getObjectFromPosition(int x, int y);
}

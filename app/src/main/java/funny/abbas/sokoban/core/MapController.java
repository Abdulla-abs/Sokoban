package funny.abbas.sokoban.core;

import java.util.Optional;

import funny.abbas.sokoban.core.map.MapObject;

public interface MapController {

    Optional<MapObject> getObjectFromPosition(int x, int y);
}

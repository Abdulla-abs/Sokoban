package funny.abbas.sokoban.view;

import java.util.ArrayList;
import java.util.Optional;

import funny.abbas.sokoban.domain.Location;
import funny.abbas.sokoban.domain.MapObject;

public class MapController {

    public final MapObject[][] map;
    public final ArrayList<MapObject> boxes;
    public final MapObject role;

    public MapController(MapObject[][] map, ArrayList<MapObject> boxes, MapObject role) {
        this.map = map;
        this.boxes = boxes;
        this.role = role;
    }

    public Optional<MapObject> getObjectFromPosition(int x, int y) {
        if (x < 0 || y < 0) {
            return Optional.empty();
        }
        for (MapObject box : boxes) {
            if (box.getLocation().getX() == x && box.getLocation().getY() == y) {
                return Optional.of(box);
            }
        }
        for (MapObject[] mapObjects : map) {
            for (MapObject mapObject : mapObjects) {
                Location location = mapObject.getLocation();
                if (location.getX() == x && location.getY() == y) {
                    return Optional.of(mapObject);
                }
            }
        }
        return Optional.empty();
    }
}

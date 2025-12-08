package funny.abbas.sokoban.domain;

import java.util.List;
import java.util.Optional;

public class MapControllerImpl implements MapController {

    public long levelHashCode = -1;
    private List<MapObject> boxes;
    private MapObject[][] map;

    @Override
    public Optional<MapObject> getObjectFromPosition(int x, int y) {
        if (x < 0 || y < 0) {
            return Optional.empty();
        }
        for (MapObject box : boxes) {
            if (box.getLocation().getX() == x && box.getLocation().getY() == y) {
                return Optional.of(box);
            }
        }
        try {
            return Optional.of(map[y][x]);
        } catch (Exception e) {
            return Optional.empty();
        }
//        for (MapObject[] mapObjects : map) {
//            for (MapObject mapObject : mapObjects) {
//                Location location = mapObject.getLocation();
//                if (location.getX() == x && location.getY() == y) {
//                    return Optional.of(mapObject);
//                }
//            }
//        }
//        return Optional.empty();

    }

    public MapControllerImpl setBoxes(List<MapObject> boxes) {
        this.boxes = boxes;
        return this;
    }

    public MapControllerImpl setMap(MapObject[][] map) {
        this.map = map;
        return this;
    }

    public void setUpMapObject(MapObject[][] map, List<MapObject> boxes) {
        this.map = map;
        this.boxes = boxes;
    }
}

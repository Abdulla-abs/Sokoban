package funny.abbas.sokoban.domain;

import java.util.List;

public class LevelMapper {
    public static Level mapper(BoxType[][] map, List<Location> target) {
        MapObject[][] mapObjects = new MapObject[map.length][];
        for (int i = 0; i < map.length; i++) {
            mapObjects[i] = new MapObject[map[i].length];
            for (int j = 0; j < map[i].length; j++) {
                MapObject mapObject;
                switch (map[i][j]) {
                    case Wall:
                        mapObject = new Wall(new Location(j, i), mapObjects);
                        break;
                    case Empty:
                        mapObject = new Empty(new Location(j, i), mapObjects);
                        break;
                    case Role:
                        mapObject = new Role(new Location(j, i), mapObjects);
                        break;
                    case Box:
                        mapObject = new Box(new Location(j, i), mapObjects);
                        break;
                    default:
                        mapObject = new Empty(new Location(j, i), mapObjects);
                }
                mapObjects[i][j] = mapObject;
            }
        }

        return new Level(mapObjects, target);
    }
}

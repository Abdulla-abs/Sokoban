package funny.abbas.sokoban.domain;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LevelMapper {
    public static Level mapper(BoxType[][] map,Location role, List<Location> boxes, List<Location> targets) {
        Level.Builder builder = new Level.Builder();
        MapObject[][] mapObjects = new MapObject[map.length][];
        for (int i = 0; i < map.length; i++) {
            mapObjects[i] = new MapObject[map[i].length];
            for (int j = 0; j < map[i].length; j++) {
                mapObjects[i][j] = builder.getMapObjectWithType(map[i][j], new Location(j, i));
            }
        }

        List<MapObject> boxesMapObj = boxes.stream()
                .map(location -> builder.getMapObjectWithType(BoxType.Box, location)).collect(Collectors.toList());

        List<MapObject> targetMapObj = targets.stream()
                .map(location -> builder.getMapObjectWithType(BoxType.Target, location)).collect(Collectors.toList());

        MapObject roleMapObj = builder.getMapObjectWithType(BoxType.Role, role);

        return builder.setMap(mapObjects)
                .setRole(roleMapObj)
                .setBoxes(boxesMapObj)
                .setTargets(targetMapObj)
                .build();
    }
}

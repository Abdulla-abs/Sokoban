package funny.abbas.sokoban.core;

import java.util.List;
import java.util.stream.Collectors;

import funny.abbas.sokoban.core.map.BoxType;
import funny.abbas.sokoban.core.map.MapObject;

public class LevelMapper {
    public static Level mapper(BoxType[][] map, Location role, List<Location> boxes, List<Location> targets) {
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

    public static Level mapper(int[][] baseMap, Location playerLoc, List<Location> boxesList, List<Location> goalsList) {
        BoxType[][] map = new BoxType[baseMap.length][baseMap[0].length];
        for (int y = 0; y < baseMap.length; y++) {
            for (int x = 0; x < baseMap[y].length; x++) {
                map[y][x] = BoxType.valueOf(baseMap[y][x]);
            }
        }
        return mapper(map, playerLoc, boxesList, goalsList);
    }

    public static Level mapper(LevelVo levelVo) {
        if (levelVo == null) return null;
        return mapper(levelVo.map, levelVo.role, levelVo.boxes, levelVo.targets);
    }
}

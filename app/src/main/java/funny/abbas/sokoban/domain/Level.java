package funny.abbas.sokoban.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Level {

    public final MapObject[][] map;
    public final MapObject role;
    public final List<MapObject> boxes;
    private final List<MapObject> targets;

    public Level(MapObject[][] map, MapObject role, List<MapObject> boxes, List<MapObject> targets) {
        this.map = map;
        this.role = role;
        this.boxes = boxes;
        this.targets = targets;
    }

    public List<Integer> serialize() {
        ArrayList<Integer> integers = new ArrayList<>();

        integers.add(map.length);
        integers.add(map[0].length);
        for (MapObject[] mapObjects : map) {
            for (MapObject mapObject : mapObjects) {
                if (mapObject instanceof Wall) {
                    integers.add(BoxType.Wall.flag);
                } else if (mapObject instanceof Empty) {
                    integers.add(BoxType.Empty.flag);
                } else if (mapObject instanceof Target) {
                    integers.add(BoxType.Target.flag);
                }
            }
        }

        for (MapObject box : boxes) {
            integers.add(box.getLocation().getX());
            integers.add(box.getLocation().getY());
        }

        integers.add(role.getLocation().getX());
        integers.add(role.getLocation().getY());

        return integers;
    }

    public static Level parse(List<Integer> origin) {
        if (origin == null || origin.size() < 2) {
            throw new IllegalStateException(); // 数据不完整
        }

        Builder builder = new Builder();

        // 读取地图尺寸
        int rows = origin.get(0);
        int cols = origin.get(1);


        // 创建新地图
        MapObject[][] newMap = new MapObject[rows][cols];

        // 解析地图数据
        int index = 2; // 从第2个元素开始是地图数据
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int flag = origin.get(index++);
                newMap[i][j] = builder.getMapObjectWithType(flag, new Location(j, i));
            }
        }

        // 解析目标点（target位置）
        List<MapObject> newBoxes = new ArrayList<>();
        int boxRange = origin.size() - 2;
        while (index < boxRange) {
            // 检查是否有完整的目标点数据（需要x和y两个值）
            if (index + 1 >= origin.size()) {
                break; // 数据不完整，跳过
            }

            int x = origin.get(index++);
            int y = origin.get(index++);

            newBoxes.add(builder.getMapObjectWithType(BoxType.Box, new Location(x, y)));
        }

        MapObject newRole = builder.getMapObjectWithType(BoxType.Role, new Location(
                origin.get(origin.size() - 2),
                origin.get(origin.size() - 1)
        ));

        return builder.setMap(newMap)
                .setBoxes(newBoxes)
                .setRole(newRole)
                .build();
    }

    public MapObject getRole() {
        return role;
    }

    public List<MapObject> getBoxes() {
        return boxes;
    }

    public List<MapObject> getTargets() {
        return targets;
    }

    public static class Builder {

        public MapObject[][] map;
        public MapObject role;
        public List<MapObject> boxes;
        public List<MapObject> targets;
        private final MapControllerImpl mapController = new MapControllerImpl();

        public Builder setBoxes(List<MapObject> boxes) {
            this.boxes = boxes;
            return this;
        }

        public Builder setRole(MapObject role) {
            this.role = role;
            return this;
        }

        public Builder setMap(MapObject[][] map) {
            this.map = map;
            return this;
        }

        public Builder setTargets(List<MapObject> targets) {
            this.targets = targets;
            return this;
        }

        public Level build() {
            if (map == null || map.length == 0 || role == null || boxes == null || boxes.isEmpty() ||
                    targets == null || targets.isEmpty())
                throw new IllegalArgumentException("some game variable is null");
            mapController.setUpMapObject(map, boxes);
            Level level = new Level(map, role, boxes, targets);
            mapController.levelHashCode = level.hashCode();
            return level;
        }

        public MapObject getMapObjectWithType(BoxType boxType, Location location) {
            return getMapObjectWithType(boxType.flag, location);
        }

        public MapObject getMapObjectWithType(int boxTypeFlag, Location location) {
            if (boxTypeFlag == BoxType.Role.flag) {
                return new Role(location, mapController);
            } else if (boxTypeFlag == BoxType.Box.flag) {
                return new Box(location, mapController);
            } else if (boxTypeFlag == BoxType.Wall.flag) {
                return new Wall(location, mapController);
            } else if (boxTypeFlag == BoxType.Empty.flag) {
                return new Empty(location, mapController);
            } else if (boxTypeFlag == BoxType.Target.flag) {
                return new Target(location, mapController);
            } else {
                // 处理未知类型，默认为空
                return new Empty(location, mapController);
            }
        }
    }


}

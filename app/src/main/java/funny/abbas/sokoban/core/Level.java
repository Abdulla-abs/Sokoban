package funny.abbas.sokoban.core;

import java.util.ArrayList;
import java.util.List;

import funny.abbas.sokoban.core.map.Box;
import funny.abbas.sokoban.core.map.BoxType;
import funny.abbas.sokoban.core.map.Empty;
import funny.abbas.sokoban.core.map.MapObject;
import funny.abbas.sokoban.core.map.Role;
import funny.abbas.sokoban.core.map.Target;
import funny.abbas.sokoban.core.map.Wall;
import kotlin.jvm.JvmStatic;

public class Level {

    public final MapObject[][] map;
    public final MapObject role;
    public final List<MapObject> boxes;
    private final List<MapObject> targets;

    public final StepRemember stepRemember;

    public Level(MapObject[][] map, MapObject role, List<MapObject> boxes, List<MapObject> targets) {
        this.map = map;
        this.role = role;
        this.boxes = boxes;
        this.targets = targets;
        stepRemember = new StepRemember(boxes,role);
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
                }
            }
        }

        integers.add(targets.size());
        for (MapObject target : targets) {
            integers.add(target.getLocation().getX());
            integers.add(target.getLocation().getY());
        }
        for (MapObject box : boxes) {
            integers.add(box.getLocation().getX());
            integers.add(box.getLocation().getY());
        }

        integers.add(role.getLocation().getX());
        integers.add(role.getLocation().getY());

        return integers;
    }

    @JvmStatic
    public static Level parse(List<Integer> origin) {
        if (origin == null || origin.size() < 2) {
            throw new IllegalStateException("数据不完整");
        }

        Builder builder = new Builder();

        int index = 0;

        // 读取地图尺寸
        int rows = origin.get(index++);
        int cols = origin.get(index++);

        // 创建地图
        MapObject[][] newMap = new MapObject[rows][cols];

        // 解析地图数据
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int flag = origin.get(index++);
                newMap[i][j] = builder.getMapObjectWithType(flag, new Location(j, i));
            }
        }

        // 读取目标点和箱子的数量 N
        int count = origin.get(index++);  // 这个才是关键：有 count 个目标 和 count 个箱子

        // 解析目标点
        List<MapObject> newTargets = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int x = origin.get(index++);
            int y = origin.get(index++);
            newTargets.add(builder.getMapObjectWithType(BoxType.Target, new Location(x, y)));
        }

        // 解析箱子
        List<MapObject> newBoxes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int x = origin.get(index++);
            int y = origin.get(index++);
            newBoxes.add(builder.getMapObjectWithType(BoxType.Box, new Location(x, y)));
        }

        // 最后两个是玩家坐标
        int playerX = origin.get(index++);
        int playerY = origin.get(index++);
        MapObject newRole = builder.getMapObjectWithType(BoxType.Role, new Location(playerX, playerY));

        return builder.setMap(newMap)
                .setBoxes(newBoxes)
                .setRole(newRole)
                .setTargets(newTargets)
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

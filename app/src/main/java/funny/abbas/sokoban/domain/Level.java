package funny.abbas.sokoban.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Level {

    public final MapObject[][] map;
    public final List<Location> target;


    private MapObject role;
    private List<MapObject> boxes;
    public Level(MapObject[][] map, List<Location> target) {
        this.map = map;
        this.target = target;
    }

    public List<Integer> serialize(){
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(map.length);
        integers.add(map[0].length);
        for (MapObject[] mapObjects : map) {
            for (MapObject mapObject : mapObjects) {
                if (mapObject instanceof Role){
                    integers.add(BoxType.Role.flag);
                } else if (mapObject instanceof Box) {
                    integers.add(BoxType.Box.flag);
                } else if (mapObject instanceof Wall) {
                    integers.add(BoxType.Wall.flag);
                } else if (mapObject instanceof Empty) {
                    integers.add(BoxType.Empty.flag);
                }
            }
        }
        for (Location location : target) {
            integers.add(location.getX());
            integers.add(location.getY());
        }
        return integers;
    }

    public static Level parse(List<Integer> origin) {
        if (origin == null || origin.size() < 2) {
            throw new IllegalStateException(); // 数据不完整
        }

        // 读取地图尺寸
        int rows = origin.get(0);
        int cols = origin.get(1);

        // 检查数据长度是否足够
        int expectedSize = 2 + rows * cols; // 基本信息 + 地图数据
        if (origin.size() < expectedSize) {
            throw new IllegalStateException(); // 地图数据不完整
        }

        // 创建新地图
        MapObject[][] newMap = new MapObject[rows][cols];

        // 解析地图数据
        int index = 2; // 从第2个元素开始是地图数据
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int flag = origin.get(index++);
                newMap[i][j] = createMapObject(flag,new Location(j,i),newMap);
            }
        }

        // 解析目标点（target位置）
        List<Location> newTargets = new ArrayList<>();
        while (index < origin.size()) {
            // 检查是否有完整的目标点数据（需要x和y两个值）
            if (index + 1 >= origin.size()) {
                break; // 数据不完整，跳过
            }

            int x = origin.get(index++);
            int y = origin.get(index++);
            newTargets.add(new Location(x, y));
        }

        return new Level(newMap,newTargets);
    }

    // 根据flag创建对应的MapObject
    private static MapObject createMapObject(int flag, Location location, MapObject[][] newMap) {
        if (flag == BoxType.Role.flag) {
            return new Role(location,newMap);
        } else if (flag == BoxType.Box.flag) {
            return new Box(location,newMap);
        } else if (flag == BoxType.Wall.flag) {
            return new Wall(location,newMap);
        } else if (flag == BoxType.Empty.flag) {
            return new Empty(location,newMap);
        } else {
            // 处理未知类型，默认为空
            return new Empty(location,newMap);
        }
    }

    // 验证地图是否有效
    private boolean validateMap(MapObject[][] map) {
        if (map == null || map.length == 0) {
            return false;
        }

        int roleCount = 0;
        int firstRowLength = map[0].length;

        // 检查所有行长度是否一致
        for (int i = 0; i < map.length; i++) {
            if (map[i].length != firstRowLength) {
                return false; // 行长度不一致
            }

            // 统计角色数量
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] instanceof Role) {
                    roleCount++;
                }
            }
        }

        // 确保只有一个角色（根据推箱子游戏规则）
        if (roleCount != 1) {
            return false;
        }

        return true;
    }

    // 查找并设置角色位置
    private void findAndSetRolePosition() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] instanceof Role) {
                    // 如果Role类有位置属性，可以在这里设置
                    // ((Role) map[i][j]).setPosition(i, j);
                    return; // 找到第一个角色就返回
                }
            }
        }
    }

    public MapObject getRole() {
        if (role == null) {
            Optional<MapObject> roleOptional = Arrays.stream(map)
                    .flatMap(new Function<MapObject[], Stream<MapObject>>() {
                        @Override
                        public Stream<MapObject> apply(MapObject[] mapObjects) {
                            return Arrays.stream(mapObjects);
                        }
                    })
                    .filter(new Predicate<MapObject>() {
                        @Override
                        public boolean test(MapObject mapObject) {
                            return mapObject instanceof Role;
                        }
                    })
                    .findFirst();
            roleOptional.ifPresent(mapObject -> role = mapObject);
        }
        return role;
    }

    public List<MapObject> getBoxes(){
        if (boxes == null){
            boxes = new ArrayList<>();
            for (MapObject[] mapObjects : map) {
                for (MapObject mapObject : mapObjects) {
                    if (mapObject instanceof Box){
                        boxes.add(mapObject);
                    }
                }
            }
        }
        return boxes;
    }


}

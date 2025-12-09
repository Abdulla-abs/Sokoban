package funny.abbas.sokoban.domain;

import java.util.List;

public class LevelVo {

    public final int[][] map;
    public final Location role;
    public final List<Location> boxes;
    public final List<Location> targets;

    public LevelVo(int[][] map, Location role, List<Location> boxes, List<Location> targets) {
        this.map = map;
        this.role = role;
        this.boxes = boxes;
        this.targets = targets;
    }
}

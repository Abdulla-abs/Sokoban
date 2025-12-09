package funny.abbas.sokoban.core;

public enum BoxType {
    Empty(0),
    Wall(1),
    Box(2),
    Role(3),
    Target(4);

    public final int flag;

    BoxType(int flag) {
        this.flag = flag;
    }

    public static BoxType valueOf(int i) {
        switch (i) {
            case 0:
                return Empty;
            case 1:
                return Wall;
            case 2:
                return Box;
            case 3:
                return Role;
            case 4:
                return Target;
            default:
                return Empty;
        }
    }
}

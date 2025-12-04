package funny.abbas.sokoban.domain;

public enum BoxType {
    Empty(0),
    Wall(1),
    Box(2),
    Role(3);

    public final int flag;

    BoxType(int flag) {
        this.flag = flag;
    }
}

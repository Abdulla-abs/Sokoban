package funny.abbas.sokoban.domain;

public interface MapObjectAble {

    boolean canMoveLeft();

    boolean canMoveTop();

    boolean canMoveRight();

    boolean canMoveBottom();

    Location moveLeft(Location origin);

    Location moveUp(Location origin);

    Location moveRight(Location origin);

    Location moveBottom(Location origin);

}

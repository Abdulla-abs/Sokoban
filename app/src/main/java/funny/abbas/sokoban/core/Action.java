package funny.abbas.sokoban.core;

public interface Action {

    boolean moveLeft();

    boolean moveTop();

    boolean moveRight();

    boolean moveBottom();

    boolean backStep();

}

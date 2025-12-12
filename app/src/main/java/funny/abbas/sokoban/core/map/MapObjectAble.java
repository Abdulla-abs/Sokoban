package funny.abbas.sokoban.core.map;

import funny.abbas.sokoban.core.Location;
import funny.abbas.sokoban.core.StepRemember;

public interface MapObjectAble {

    boolean canMoveLeft();

    boolean canMoveTop();

    boolean canMoveRight();

    boolean canMoveBottom();

    Location moveLeft(Location origin, StepRemember stepRemember);

    Location moveUp(Location origin, StepRemember stepRemember);

    Location moveRight(Location origin, StepRemember stepRemember);

    Location moveBottom(Location origin, StepRemember stepRemember);

}

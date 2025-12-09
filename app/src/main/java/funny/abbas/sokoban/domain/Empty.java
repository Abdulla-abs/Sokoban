package funny.abbas.sokoban.domain;

import java.util.List;

public class Empty extends MapObject {


    public Empty(Location location, MapController mapController) {
        super(location, mapController);
    }

    @Override
    public BoxType getBoxType() {
        return BoxType.Empty;
    }

    @Override
    public boolean canMoveLeft() {
        return true;
    }

    @Override
    public boolean canMoveTop() {
        return true;
    }

    @Override
    public boolean canMoveRight() {
        return true;
    }

    @Override
    public boolean canMoveBottom() {
        return true;
    }

    @Override
    public Location moveLeft(Location origin,StepRemember stepRemember) {
        return location;
    }

    @Override
    public Location moveUp(Location origin,StepRemember stepRemember) {
        return location;
    }

    @Override
    public Location moveRight(Location origin,StepRemember stepRemember) {
        return location;
    }

    @Override
    public Location moveBottom(Location origin,StepRemember stepRemember) {
        return location;
    }


    @Override
    public String toString() {
        return "Empty{" + this.getClass().getSimpleName() +
                "location=" + location +
                '}';
    }
}

package funny.abbas.sokoban.core;

public class Target extends MapObject{

    public Target(Location location, MapController mapController) {
        super(location, mapController);
    }

    @Override
    public BoxType getBoxType() {
        return BoxType.Target;
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

}

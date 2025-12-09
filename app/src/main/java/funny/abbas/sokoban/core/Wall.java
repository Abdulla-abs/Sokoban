package funny.abbas.sokoban.core;

public class Wall extends MapObject {


    public Wall(Location location, MapController mapController) {
        super(location, mapController);
    }

    @Override
    public BoxType getBoxType() {
        return BoxType.Wall;
    }

    @Override
    public boolean canMoveLeft() {
        return false;
    }

    @Override
    public boolean canMoveTop() {
        return false;
    }

    @Override
    public boolean canMoveRight() {
        return false;
    }

    @Override
    public boolean canMoveBottom() {
        return false;
    }

    @Override
    public Location moveLeft(Location origin,StepRemember stepRemember) {
        throw new IllegalStateException();
    }

    @Override
    public Location moveUp(Location origin,StepRemember stepRemember) {
        throw new IllegalStateException();
    }

    @Override
    public Location moveRight(Location origin,StepRemember stepRemember) {
        throw new IllegalStateException();
    }

    @Override
    public Location moveBottom(Location origin,StepRemember stepRemember) {
        throw new IllegalStateException();
    }


    @Override
    public String toString() {
        return "Wall{" + this.getClass().getSimpleName() +
                "location=" + location +
                '}';
    }
}

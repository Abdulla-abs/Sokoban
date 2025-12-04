package funny.abbas.sokoban.domain;

public class Empty extends MapObject {


    public Empty(Location location, MapObject[][] map) {
        super(location, map);
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
    public Location moveLeft(Location origin) {
        Location currentlocation = location;
        location = origin;
        return currentlocation;
    }

    @Override
    public Location moveUp(Location origin) {
        Location currentlocation = location;
        location = origin;
        return currentlocation;
    }

    @Override
    public Location moveRight(Location origin) {
        Location currentlocation = location;
        location = origin;
        return currentlocation;
    }

    @Override
    public Location moveBottom(Location origin) {
        Location currentlocation = location;
        location = origin;
        return currentlocation;
    }


    @Override
    public String toString() {
        return "Empty{" + this.getClass().getSimpleName() +
                "location=" + location +
                '}';
    }
}

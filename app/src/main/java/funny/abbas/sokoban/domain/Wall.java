package funny.abbas.sokoban.domain;

public class Wall extends MapObject {


    public Wall(Location location, MapObject[][] map) {
        super(location, map);
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
    public Location moveLeft(Location origin) {
        throw new IllegalStateException();
    }

    @Override
    public Location moveUp(Location origin) {
        throw new IllegalStateException();
    }

    @Override
    public Location moveRight(Location origin) {
        throw new IllegalStateException();
    }

    @Override
    public Location moveBottom(Location origin) {
        throw new IllegalStateException();
    }


    @Override
    public String toString() {
        return "Wall{" + this.getClass().getSimpleName() +
                "location=" + location +
                '}';
    }
}

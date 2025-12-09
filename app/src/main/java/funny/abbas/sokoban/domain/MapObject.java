package funny.abbas.sokoban.domain;

import java.util.Optional;

public abstract class MapObject implements MapObjectAble {

    protected Location location;
    protected MapController mapController;

    public MapObject(Location location, MapController mapController) {
        this.location = location;
        this.mapController = mapController;
    }

    public abstract BoxType getBoxType();


    public Optional<MapObject> getLeftObj() {
        return mapController.getObjectFromPosition(location.getX() - 1, location.getY());
    }

    public Optional<MapObject> getRightObj() {
        return mapController.getObjectFromPosition(location.getX() + 1, location.getY());
    }

    public Optional<MapObject> getTopObj() {
        return mapController.getObjectFromPosition(location.getX(), location.getY() - 1);
    }

    public Optional<MapObject> getBottomObj() {
        return mapController.getObjectFromPosition(location.getX(), location.getY() + 1);
    }

    public Optional<MapObject> getDeepMapObj() {
        return mapController.getObjectFromPosition(location.getX(), location.getY());
    }

    public Location getLocation() {
        return location;
    }

    public MapObject setLocation(Location location) {
        this.location = location;
        return this;
    }
}

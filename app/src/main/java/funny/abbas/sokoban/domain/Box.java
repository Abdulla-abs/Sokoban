package funny.abbas.sokoban.domain;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Box extends MapObject {


    public Box(Location location, MapController mapController) {
        super(location, mapController);
    }

    @Override
    public BoxType getBoxType() {
        return BoxType.Box;
    }

    @Override
    public boolean canMoveLeft() {
        return getLeftObj().filter(new Predicate<MapObject>() {
            @Override
            public boolean test(MapObject mapObject) {
                return mapObject instanceof Empty || mapObject instanceof Target;
            }
        }).map(new Function<MapObject, Boolean>() {
            @Override
            public Boolean apply(MapObject mapObject) {
                return mapObject.canMoveLeft();
            }
        }).orElse(false);
    }

    @Override
    public boolean canMoveTop() {
        return getTopObj().filter(new Predicate<MapObject>() {
            @Override
            public boolean test(MapObject mapObject) {
                return mapObject instanceof Empty || mapObject instanceof Target;
            }
        }).map(new Function<MapObject, Boolean>() {
            @Override
            public Boolean apply(MapObject mapObject) {
                return mapObject.canMoveTop();
            }
        }).orElse(false);
    }

    @Override
    public boolean canMoveRight() {
        return getRightObj().filter(new Predicate<MapObject>() {
            @Override
            public boolean test(MapObject mapObject) {
                return mapObject instanceof Empty || mapObject instanceof Target;
            }
        }).map(new Function<MapObject, Boolean>() {
            @Override
            public Boolean apply(MapObject mapObject) {
                return mapObject.canMoveRight();
            }
        }).orElse(false);
    }

    @Override
    public boolean canMoveBottom() {
        return getBottomObj().filter(new Predicate<MapObject>() {
            @Override
            public boolean test(MapObject mapObject) {
                return mapObject instanceof Empty || mapObject instanceof Target;
            }
        }).map(new Function<MapObject, Boolean>() {
            @Override
            public Boolean apply(MapObject mapObject) {
                return mapObject.canMoveBottom();
            }
        }).orElse(false);
    }

    @Override
    public Location moveLeft(Location origin, StepRemember stepRemember) {
        Optional<MapObject> leftObj = getLeftObj();
        if (leftObj.isPresent()){
            stepRemember.push(this,location);
            MapObject mapObject = leftObj.get();
            Location currentLocation = location;
            location = mapObject.moveLeft(origin,stepRemember);

            return currentLocation;
        }else {
            throw new IllegalStateException();
        }
    }

    @Override
    public Location moveUp(Location origin, StepRemember stepRemember) {
        Optional<MapObject> topObj = getTopObj();
        if (topObj.isPresent()){
            stepRemember.push(this,location);
            MapObject mapObject = topObj.get();
            Location currentLocation = location;
            location = mapObject.moveLeft(origin,stepRemember);

            return currentLocation;
        }else {
            throw new IllegalStateException();
        }
    }

    @Override
    public Location moveRight(Location origin, StepRemember stepRemember) {
        Optional<MapObject> rightObj = getRightObj();
        if (rightObj.isPresent()){
            stepRemember.push(this,location);
            MapObject mapObject = rightObj.get();
            Location currentLocation = location;
            location = mapObject.moveLeft(origin,stepRemember);

            return currentLocation;
        }else {
            throw new IllegalStateException();
        }
    }

    @Override
    public Location moveBottom(Location origin, StepRemember stepRemember) {
        Optional<MapObject> bottomObj = getBottomObj();
        if (bottomObj.isPresent()){
            stepRemember.push(this,location);
            MapObject mapObject = bottomObj.get();
            Location currentLocation = location;
            location = mapObject.moveLeft(origin,stepRemember);

            return currentLocation;
        }else {
            throw new IllegalStateException();
        }
    }


    @Override
    public String toString() {
        return "Box{" + this.getClass().getSimpleName() +
                "location=" + location +
                '}';
    }
}

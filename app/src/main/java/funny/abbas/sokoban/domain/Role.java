package funny.abbas.sokoban.domain;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Role extends MapObject {


    public Role(Location location, MapController mapController) {
        super(location, mapController);
    }

    @Override
    public BoxType getBoxType() {
        return BoxType.Role;
    }

    @Override
    public boolean canMoveLeft() {
        return getLeftObj().map(new Function<MapObject, Boolean>() {
            @Override
            public Boolean apply(MapObject mapObject) {
                return mapObject.canMoveLeft();
            }
        }).orElse(false);
    }

    @Override
    public boolean canMoveTop() {
        return getTopObj().map(MapObjectAble::canMoveTop).orElse(false);
    }

    @Override
    public boolean canMoveRight() {
        return getRightObj().map(MapObjectAble::canMoveRight).orElse(false);
    }

    @Override
    public boolean canMoveBottom() {
        return getBottomObj().map(MapObjectAble::canMoveBottom).orElse(false);
    }

    @Override
    public Location moveLeft(Location origin) {
        getLeftObj().ifPresent(new Consumer<MapObject>() {
            @Override
            public void accept(MapObject mapObject) {
                location = mapObject.moveLeft(location);
            }
        });
        return location;
    }

    @Override
    public Location moveUp(Location origin) {
        getTopObj().ifPresent(new Consumer<MapObject>() {
            @Override
            public void accept(MapObject mapObject) {
                location = mapObject.moveUp(location);
            }
        });
        return location;
    }

    @Override
    public Location moveRight(Location origin) {
        getRightObj().ifPresent(new Consumer<MapObject>() {
            @Override
            public void accept(MapObject mapObject) {
                location = mapObject.moveRight(location);
            }
        });
        return location;
    }

    @Override
    public Location moveBottom(Location origin) {
        getBottomObj().ifPresent(new Consumer<MapObject>() {
            @Override
            public void accept(MapObject mapObject) {
                location = mapObject.moveBottom(location);
            }
        });
        return location;
    }

    @Override
    public String toString() {
        return "Role{" + this.getClass().getSimpleName() +
                "location=" + location +
                '}';
    }
}

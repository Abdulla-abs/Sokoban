package funny.abbas.sokoban.core.map;

import java.util.function.Consumer;
import java.util.function.Function;

import funny.abbas.sokoban.core.Location;
import funny.abbas.sokoban.core.MapController;
import funny.abbas.sokoban.core.StepRemember;
import funny.abbas.sokoban.core.Direction;

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
    public Location moveLeft(Location origin, StepRemember stepRemember) {
        direction = Direction.LEFT;
        stepRemember.pushStart();
        getLeftObj().ifPresent(mapObject -> {
            stepRemember.push(Role.this, location);
            location = mapObject.moveLeft(location, stepRemember);

        });
        stepRemember.pushEnd();
        return location;
    }

    @Override
    public Location moveUp(Location origin,StepRemember stepRemember) {
        direction = Direction.TOP;
        stepRemember.pushStart();
        getTopObj().ifPresent(new Consumer<MapObject>() {
            @Override
            public void accept(MapObject mapObject) {
                stepRemember.push(Role.this,location);
                location = mapObject.moveUp(location,stepRemember);

            }
        });
        stepRemember.pushEnd();
        return location;
    }

    @Override
    public Location moveRight(Location origin,StepRemember stepRemember) {
        direction = Direction.RIGHT;
        stepRemember.pushStart();
        getRightObj().ifPresent(new Consumer<MapObject>() {
            @Override
            public void accept(MapObject mapObject) {
                stepRemember.push(Role.this,location);
                location = mapObject.moveRight(location,stepRemember);

            }
        });
        stepRemember.pushEnd();
        return location;
    }

    @Override
    public Location moveBottom(Location origin,StepRemember stepRemember) {
        direction = Direction.BOTTOM;
        stepRemember.pushStart();
        getBottomObj().ifPresent(new Consumer<MapObject>() {
            @Override
            public void accept(MapObject mapObject) {
                stepRemember.push(Role.this,location);
                location = mapObject.moveBottom(location,stepRemember);

            }
        });
        stepRemember.pushEnd();
        return location;
    }

    @Override
    public String toString() {
        return "Role{" + this.getClass().getSimpleName() +
                "location=" + location +
                '}';
    }
}

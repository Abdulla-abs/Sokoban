package funny.abbas.sokoban.domain;

import java.util.Optional;

public abstract class MapObject implements MapObjectAble {

    protected Location location;
    protected MapObject[][] map;

    public MapObject(Location location, MapObject[][] map) {
        this.location = location;
        this.map = map;
    }

    public Optional<MapObject> getObjectFromPosition(int x, int y) {
        if (x < 0 || y < 0) {
            return Optional.empty();
        }
        try {
            for (MapObject[] mapObjects : map) {
                for (MapObject mapObject : mapObjects) {
                    Location location = mapObject.getLocation();
                    if (location.getX() == x && location.getY() == y){
                        return Optional.of(mapObject);
                    }
                }
            }
            return Optional.empty();

            //注意，这里没写错。map的构建第一个数组长度是y，而不是x
//            return Arrays.stream(map).flatMap(new Function<MapObject[], Stream<MapObject>>() {
//                @Override
//                public Stream<MapObject> apply(MapObject[] mapObjects) {
//                    return Arrays.stream(mapObjects);
//                }
//            }).filter(new Predicate<MapObject>() {
//                @Override
//                public boolean test(MapObject mapObject) {
//                    Location mapLocation = mapObject.getLocation();
//                    return mapLocation.getX() == x && mapLocation.getY() == y;
//                }
//            }).findFirst();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<MapObject> getLeftObj() {
        return getObjectFromPosition(location.getX() - 1, location.getY());
    }

    public Optional<MapObject> getRightObj() {
        return getObjectFromPosition(location.getX() + 1, location.getY());
    }

    public Optional<MapObject> getTopObj() {
        return getObjectFromPosition(location.getX(), location.getY() - 1);
    }

    public Optional<MapObject> getBottomObj() {
        return getObjectFromPosition(location.getX(), location.getY() + 1);
    }

    public Location getLocation() {
        return location;
    }

    public void setMap(MapObject[][] map) {
        this.map = map;
    }

}

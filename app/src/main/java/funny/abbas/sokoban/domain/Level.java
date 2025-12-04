package funny.abbas.sokoban.domain;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Level {

    public final MapObject[][] map;
    private MapObject role;
    public final Location[] target;

    public Level(MapObject[][] map, Location[] target) {
        this.map = map;
        this.target = target;
    }

    public MapObject getRole() {
        if (role == null) {
            Optional<MapObject> roleOptional = Arrays.stream(map)
                    .flatMap(new Function<MapObject[], Stream<MapObject>>() {
                        @Override
                        public Stream<MapObject> apply(MapObject[] mapObjects) {
                            return Arrays.stream(mapObjects);
                        }
                    })
                    .filter(new Predicate<MapObject>() {
                        @Override
                        public boolean test(MapObject mapObject) {
                            return mapObject instanceof Role;
                        }
                    })
                    .findFirst();
            roleOptional.ifPresent(mapObject -> role = mapObject);
        }
        return role;
    }
}

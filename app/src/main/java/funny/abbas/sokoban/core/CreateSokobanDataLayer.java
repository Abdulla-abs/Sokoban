package funny.abbas.sokoban.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import funny.abbas.sokoban.state.createsokoban.CreateSokobanState;
import funny.abbas.sokoban.util.ResizableSparseArray2D;

public class CreateSokobanDataLayer {

    private CreateSokobanState state = CreateSokobanState.PUT_BASIC;

    private final ResizableSparseArray2D<BoxType> basicMap = new ResizableSparseArray2D<>(6, 6, BoxType.Empty);
    private final ArrayList<Location> targets = new ArrayList<>();
    private final ArrayList<Location> boxes = new ArrayList<>();
    private Location role = null;

    public void put(BoxType boxType, int x, int y) {
        switch (state) {
            case PUT_BASIC:
                basicMap.set(y, x, boxType);
                break;
            case PUT_TARGET:
                targets.add(new Location(x, y));
                break;
            case PUT_BOX:
                boxes.add(new Location(x, y));
                break;
            case PUT_ROLE:
                role = new Location(x, y);
                break;
        }
    }

    public void remove(int x, int y) {
        switch (state) {
            case PUT_BASIC:
                basicMap.clear(y, x);
                break;
            case PUT_TARGET:
                Iterator<Location> iterator = targets.iterator();
                while (iterator.hasNext()) {
                    Location next = iterator.next();
                    if (next.getX() == x && next.getY() == y) {
                        iterator.remove();
                        return;
                    }
                }
                break;
            case PUT_BOX:
                Iterator<Location> iterator1 = boxes.iterator();
                while (iterator1.hasNext()) {
                    Location next = iterator1.next();
                    if (next.getX() == x && next.getY() == y) {
                        iterator1.remove();
                        return;
                    }
                }
                break;
            case PUT_ROLE:
                role = null;
                break;
        }
    }

    public CreateSokobanDataLayer setState(CreateSokobanState state) {
        this.state = state;
        return this;
    }

    public void expandRows() {
        basicMap.expandRows(1);
    }

    public void expandColumns() {
        basicMap.expandColumns(1);
    }

    public void shrinkRows() {
        basicMap.shrinkRows(1, true);
    }

    public void shrinkColumns() {
        basicMap.shrinkColumns(1, true);
    }

    public Optional<Location> getRole() {
        return Optional.ofNullable(role);
    }

    public ArrayList<Location> getBoxes() {
        return boxes;
    }

    public ArrayList<Location> getTargets() {
        return targets;
    }

    public ResizableSparseArray2D<BoxType> getBasicMap() {
        return basicMap;
    }

}

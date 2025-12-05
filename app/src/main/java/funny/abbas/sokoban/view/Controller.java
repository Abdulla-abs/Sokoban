package funny.abbas.sokoban.view;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import funny.abbas.sokoban.domain.Level;
import funny.abbas.sokoban.domain.Location;
import funny.abbas.sokoban.domain.MapObject;

public abstract class Controller {

    protected final Action action = new ActionImpl();

    protected Level level;


    protected abstract void onMoveLeft();

    protected abstract void onMoveTop();

    protected abstract void onMoveRight();

    protected abstract void onMoveBottom();

    protected abstract void onGameOver();

    private class ActionImpl implements Action {

        @Override
        public void moveLeft() {
            onMoveLeft();
            if (checkGameOver()) {
                onGameOver();
            }
        }

        @Override
        public void moveTop() {
            onMoveTop();
            if (checkGameOver()) {
                onGameOver();
            }
        }

        @Override
        public void moveRight() {
            onMoveRight();
            if (checkGameOver()) {
                onGameOver();
            }
        }

        @Override
        public void moveBottom() {
            onMoveBottom();
            if (checkGameOver()) {
                onGameOver();
            }
        }
    }

    private boolean checkGameOver() {
        List<MapObject> boxes = level.getBoxes();
        if (boxes.isEmpty()) return false;
        List<Location> currentBoxes = boxes.stream().map(MapObject::getLocation).collect(Collectors.toList());
        List<Location> targets = level.target;
        return currentBoxes.size() == targets.size()
                && new HashSet<>(currentBoxes).equals(new HashSet<>(targets));
    }
}

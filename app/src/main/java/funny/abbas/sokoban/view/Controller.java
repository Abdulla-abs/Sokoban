package funny.abbas.sokoban.view;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import funny.abbas.sokoban.domain.Level;
import funny.abbas.sokoban.domain.Location;
import funny.abbas.sokoban.domain.MapObject;

public class Controller implements Action {
    protected Level level;
    public GameStateListener stateListener;

    protected boolean onMoveLeft() {
        if (level.getRole().canMoveLeft()) {
            level.getRole().moveLeft(null);
            return true;
        }
        return false;
    }

    protected boolean onMoveTop() {
        if (level.getRole().canMoveTop()) {
            level.getRole().moveUp(null);
            return true;
        }
        return false;
    }


    protected boolean onMoveRight() {
        if (level.getRole().canMoveRight()) {
            level.getRole().moveRight(null);
            return true;
        }
        return false;
    }


    protected boolean onMoveBottom() {
        if (level.getRole().canMoveBottom()) {
            level.getRole().moveBottom(null);
            return true;
        }
        return false;
    }


    protected void onGameOver() {
        if (stateListener != null){
            stateListener.onSuccess();
        }
    }

    @Override
    public boolean moveLeft() {
        boolean move = onMoveLeft();
        if (move && checkGameOver()) {
            onGameOver();
        }
        return move;
    }

    @Override
    public boolean moveTop() {
        boolean move = onMoveTop();
        if (move && checkGameOver()) {
            onGameOver();
        }
        return move;
    }

    @Override
    public boolean moveRight() {
        boolean move = onMoveRight();
        if (move && checkGameOver()) {
            onGameOver();
        }
        return move;
    }

    @Override
    public boolean moveBottom() {
        boolean move = onMoveBottom();
        if (move && checkGameOver()) {
            onGameOver();
        }
        return move;
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

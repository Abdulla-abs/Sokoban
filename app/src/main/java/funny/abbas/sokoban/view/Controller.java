package funny.abbas.sokoban.view;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import funny.abbas.sokoban.domain.Level;
import funny.abbas.sokoban.domain.Location;
import funny.abbas.sokoban.domain.MapObject;
import funny.abbas.sokoban.domain.Target;

public class Controller implements Action {
    protected Level level;
    public GameStateListener stateListener;

    protected boolean onMoveLeft() {
        if (level.getRole().canMoveLeft()) {
            level.getRole().moveLeft(null, level.stepRemember);
            return true;
        }
        return false;
    }

    protected boolean onMoveTop() {
        if (level.getRole().canMoveTop()) {
            level.getRole().moveUp(null, level.stepRemember);
            return true;
        }
        return false;
    }


    protected boolean onMoveRight() {
        if (level.getRole().canMoveRight()) {
            level.getRole().moveRight(null, level.stepRemember);
            return true;
        }
        return false;
    }


    protected boolean onMoveBottom() {
        if (level.getRole().canMoveBottom()) {
            level.getRole().moveBottom(null, level.stepRemember);
            return true;
        }
        return false;
    }


    protected void onGameOver() {
        if (stateListener != null) {
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

    @Override
    public boolean backStep() {
        return level.stepRemember.backStep(1);
    }


    private boolean checkGameOver() {
        List<MapObject> boxes = level.getBoxes();
        if (boxes.isEmpty()) return false;
        List<MapObject> targets = level.getTargets();
        if (targets.isEmpty()) return false;

        HashSet<Location> boxesHashSet = new HashSet<>();
        for (MapObject box : boxes) {
            boxesHashSet.add(box.getLocation());
        }
        HashSet<Location> targetHashSet = new HashSet<>();
        for (MapObject target : targets) {
            targetHashSet.add(target.getLocation());
        }
        return boxesHashSet.containsAll(targetHashSet);
    }
}

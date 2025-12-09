package funny.abbas.sokoban.core;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StepRemember {

    private final HashMap<Integer, List<Pair<MapObject, Location>>> remember = new HashMap<>();
    private int step = 0;


    private List<Pair<MapObject, Location>> currentStepList;

    public StepRemember(List<MapObject> boxes, MapObject role) {
        pushStart();
        for (MapObject box : boxes) {
            push(box, box.getLocation());
        }
        push(role, role.getLocation());
        pushEnd();
    }

    public void pushStart() {
        currentStepList = new ArrayList<>(2);
    }

    public void push(MapObject mapObject, Location location) {
        currentStepList.add(new Pair<>(mapObject, location));
    }

    public void pushEnd() {
        remember.put(step, currentStepList);
        ++step;
    }

    public boolean backStep(int backStepNum) {
        if (step != remember.size()) return false;
        if (step == 0) return false;
        if (step - backStepNum < 0) return false;

        int targetStep = step - backStepNum;
        while (step != targetStep) {
            List<Pair<MapObject, Location>> pairs = remember.get(step - 1);
            if (pairs != null && !pairs.isEmpty()) {
                for (Pair<MapObject, Location> pair : pairs) {
                    pair.first.setLocation(pair.second);
                }
            }
            remember.remove(step - 1);
            step--;
        }

        return true;
    }

    public List<Pair<MapObject, Location>> getCurrentStep() {
        return remember.get(step-1);
    }
}

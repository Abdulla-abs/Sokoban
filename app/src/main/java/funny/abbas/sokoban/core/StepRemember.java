package funny.abbas.sokoban.core;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import funny.abbas.sokoban.core.map.MapObject;

/**
 * 移动记录
 */
public class StepRemember {

    //每一步的数据快照
    private final HashMap<Integer, List<Pair<MapObject, Location>>> remember = new HashMap<>();
    //步数 你可以添加回调展示使用步数等
    private int step = 0;
    //推箱子的次数
    private int pushBoxTimes = 0;

    //下一次将要存储的快照列表
    private List<Pair<MapObject, Location>> currentStepList;

    private StepListener listener;

    public StepRemember(List<MapObject> boxes, MapObject role) {
        //初始化时记录最初的坐标
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
        if (currentStepList.size() > 1) {
            pushBoxTimes++;
        }
        if (listener != null) {
            listener.onStepChange(step-1, pushBoxTimes);
        }
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
                if (pairs.size() > 1) {
                    pushBoxTimes--;
                }
            }
            remember.remove(step - 1);
            step--;

        }

        if (listener != null) {
            listener.onStepChange(step-1, pushBoxTimes);
        }

        return true;
    }

    public List<Pair<MapObject, Location>> getCurrentStep() {
        return remember.get(step - 1);
    }

    /**
     * 获取当前推了箱子的步数
     * 实现很简单，在所有的步数当中检查有1以上个物体移动记录的计总
     * @return
     */
    public int getPushBoxStep() {
        return (int) remember.values().stream()
                .filter(pairs -> pairs.size() > 1).count() - 1;
    }

    public void setListener(StepListener listener) {
        this.listener = listener;
    }
}

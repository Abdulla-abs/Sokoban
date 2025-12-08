package funny.abbas.sokoban.page.vm;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.Collections;
import java.util.List;

import funny.abbas.sokoban.domain.Level;

public class MainViewModel extends ViewModel {

    public final MutableLiveData<List<Level>> allLevel = new MutableLiveData<>(Collections.emptyList());
    public final MutableLiveData<Integer> levelIndex = new MutableLiveData<>(0);
    public final MediatorLiveData<Level> currentLevel = new MediatorLiveData<>();

    public MainViewModel() {
        currentLevel.addSource(allLevel, new Observer<List<Level>>() {
            @Override
            public void onChanged(List<Level> levels) {
                onLevelIndexCombine(levels,levelIndex.getValue());
            }
        });
        currentLevel.addSource(levelIndex, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                onLevelIndexCombine(allLevel.getValue(),integer);
            }
        });
    }

    private void onLevelIndexCombine(List<Level> list, Integer index){
        if (list == null) return;
        if (list.isEmpty()) return;
        try {
            Level level = list.get(index);
            currentLevel.postValue(level);
        } catch (Exception e) {
            //wtf
        }
    }


    public void nextLevel() {
        Integer value = levelIndex.getValue();
        if (value != null){
            levelIndex.setValue(++value);
        }else {
            levelIndex.setValue(0);
        }
    }

    public void preLevel() {
        Integer value = levelIndex.getValue();
        if (value != null && value > 0){
            levelIndex.setValue(--value);
        }else {
            levelIndex.setValue(0);
        }
    }
}

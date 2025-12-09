package funny.abbas.sokoban.core;

import android.content.SharedPreferences;
import android.graphics.Bitmap;

import androidx.preference.PreferenceManager;

import java.util.Objects;

import funny.abbas.sokoban.MyApplication;
import funny.abbas.sokoban.view.Direction;

public class Skin {

    private static final Skin instance = new Skin();

    private Skin() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.application);
        String skinCodeStr = defaultSharedPreferences.getString("skin", "0");

        try {
            theme = valueOfThemeCode(Integer.parseInt(skinCodeStr));
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static Skin getInstance() {
        return instance;
    }

    private Theme theme;

    public Bitmap loadSkin(BoxType type, Direction direction, float width, float height) {
        switch (type) {
            case Role:
                return theme.getRole(direction,width, height);
            case Wall:
                return theme.getWall(direction,width, height);
            case Box:
                return theme.getBox(direction,width, height);
            case Empty:
                return theme.getEmpty(direction,width, height);
            case Target:
                return theme.getTarget(direction,width, height);
        }
        throw new IllegalArgumentException("wft,how did you do that?");
    }

    public Bitmap loadAnimeSkin(BoxType type, Direction direction, float width, float height) {
        if (Objects.requireNonNull(type) == BoxType.Role) {
            return theme.getAnimateRole(direction, width, height);
        }
        throw new IllegalArgumentException("wft,how did you do that?");
    }


    public void setTheme(Theme theme) {
        this.theme.recycleBitmaps();
        this.theme = theme;
    }

    public static Theme valueOfThemeCode(int themeCode){
        switch (themeCode){
            case 0:
                return new ClassicTheme1();
            case 1:
                return new DefaultSkin();
        }
        throw new IllegalArgumentException("wtf");
    }
}

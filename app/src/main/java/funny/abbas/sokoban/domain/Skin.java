package funny.abbas.sokoban.domain;

import android.content.SharedPreferences;
import android.graphics.Bitmap;

import androidx.preference.PreferenceManager;

import funny.abbas.sokoban.MyApplication;

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

    public Bitmap loadSkin(BoxType type, float width, float height) {
        switch (type) {
            case Role:
                return theme.getRole(width, height);
            case Wall:
                return theme.getWall(width, height);
            case Box:
                return theme.getBox(width, height);
            case Empty:
                return theme.getEmpty(width, height);
            case Target:
                return theme.getTarget(width, height);
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

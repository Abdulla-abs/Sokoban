package funny.abbas.sokoban.domain;

import android.graphics.Bitmap;

public class Skin {

    private static final Skin instance = new Skin();

    private Skin() {
        //theme = new DefaultSkin();
        theme = new ClassicTheme1();
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
}

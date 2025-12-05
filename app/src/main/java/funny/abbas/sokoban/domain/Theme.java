package funny.abbas.sokoban.domain;

import android.graphics.Bitmap;

public interface Theme{

    Bitmap getRole(float width,float height);

    Bitmap getBox(float width,float height);

    Bitmap getWall(float width,float height);

    Bitmap getEmpty(float width,float height);

    Bitmap getTarget(float width,float height);

    void recycleBitmaps();

}
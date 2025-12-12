package funny.abbas.sokoban.core.skin;

import android.graphics.Bitmap;

import funny.abbas.sokoban.core.Direction;

public interface Theme{

    Bitmap getRole(Direction direction, float width, float height);

    Bitmap getBox(Direction direction, float width,float height);

    Bitmap getWall(Direction direction, float width,float height);

    Bitmap getEmpty(Direction direction, float width,float height);

    Bitmap getTarget(Direction direction, float width,float height);

    Bitmap getAnimateRole(Direction direction, float width, float height);

    void recycleBitmaps();

}
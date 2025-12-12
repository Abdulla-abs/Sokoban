package funny.abbas.sokoban.core.skin;

import android.graphics.Bitmap;
import android.util.Pair;

import java.util.HashMap;

import funny.abbas.sokoban.core.map.BoxType;
import funny.abbas.sokoban.core.Direction;

public abstract class CacheThemeAbs implements Theme {
    protected final HashMap<BoxType, Bitmap> bitmapCache = new HashMap<>();
    protected final HashMap<BoxType, Pair<Float, Float>> sizeRemember = new HashMap<>();

    @Override
    public Bitmap getRole(Direction direction, float width, float height) {
        onLoadRoleBefore();
        Bitmap cacheBitmap = bitmapCache.get(BoxType.Role);
        Pair<Float, Float> rememberPair = sizeRemember.get(BoxType.Role);
        if (rememberPair != null) {
            if (rememberPair.first != width || rememberPair.second != height) {
                sizeRemember.put(BoxType.Role, new Pair<>(width, height));
                cacheBitmap = null;
            }
        } else {
            sizeRemember.put(BoxType.Role, new Pair<>(width, height));
        }
        if (cacheBitmap == null) {
            bitmapCache.put(BoxType.Role, getRoleBitmap(direction,width, height));
        }
        return bitmapCache.get(BoxType.Role);
    }

    @Override
    public Bitmap getBox(Direction direction, float width, float height) {
        onLoadBoxBefore();
        Bitmap cacheBitmap = bitmapCache.get(BoxType.Box);
        Pair<Float, Float> rememberPair = sizeRemember.get(BoxType.Box);
        if (rememberPair != null) {
            if (rememberPair.first != width || rememberPair.second != height) {
                sizeRemember.put(BoxType.Box, new Pair<>(width, height));
                cacheBitmap = null;
            }
        } else {
            sizeRemember.put(BoxType.Box, new Pair<>(width, height));
        }
        if (cacheBitmap == null) {
            bitmapCache.put(BoxType.Box, getBoxBitmap(direction,width, height));
        }
        return bitmapCache.get(BoxType.Box);
    }

    @Override
    public Bitmap getWall(Direction direction, float width, float height) {
        onLoadWallBefore();
        Bitmap cacheBitmap = bitmapCache.get(BoxType.Wall);
        Pair<Float, Float> rememberPair = sizeRemember.get(BoxType.Wall);
        if (rememberPair != null) {
            if (rememberPair.first != width || rememberPair.second != height) {
                sizeRemember.put(BoxType.Wall, new Pair<>(width, height));
                cacheBitmap = null;
            }
        } else {
            sizeRemember.put(BoxType.Wall, new Pair<>(width, height));
        }
        if (cacheBitmap == null) {
            bitmapCache.put(BoxType.Wall, getWallBitmap(direction,width, height));
        }
        return bitmapCache.get(BoxType.Wall);
    }

    @Override
    public Bitmap getEmpty(Direction direction, float width, float height) {
        onLoadEmptyBefore();
        Bitmap cacheBitmap = bitmapCache.get(BoxType.Empty);
        Pair<Float, Float> rememberPair = sizeRemember.get(BoxType.Empty);
        if (rememberPair != null) {
            if (rememberPair.first != width || rememberPair.second != height) {
                sizeRemember.put(BoxType.Empty, new Pair<>(width, height));
                cacheBitmap = null;
            }
        } else {
            sizeRemember.put(BoxType.Empty, new Pair<>(width, height));
        }
        if (cacheBitmap == null) {
            bitmapCache.put(BoxType.Empty, getEmptyBitmap(direction,width, height));
        }
        return bitmapCache.get(BoxType.Empty);
    }

    @Override
    public Bitmap getTarget(Direction direction, float width, float height) {
        onLoadTargetBefore();
        Bitmap cacheBitmap = bitmapCache.get(BoxType.Target);
        Pair<Float, Float> rememberPair = sizeRemember.get(BoxType.Target);
        if (rememberPair != null) {
            if (rememberPair.first != width || rememberPair.second != height) {
                sizeRemember.put(BoxType.Target, new Pair<>(width, height));
                cacheBitmap = null;
            }
        } else {
            sizeRemember.put(BoxType.Target, new Pair<>(width, height));
        }
        if (cacheBitmap == null) {
            bitmapCache.put(BoxType.Target, getTargetBitmap(direction,width, height));
        }
        return bitmapCache.get(BoxType.Target);
    }

    @Override
    public Bitmap getAnimateRole(Direction direction, float width, float height) {
        return getRole(direction, width, height);
    }

    @Override
    public void recycleBitmaps() {
        bitmapCache.clear();
    }

    protected void onLoadRoleBefore() {
        //nothing
    }

    protected void onLoadBoxBefore() {

    }

    protected void onLoadWallBefore() {

    }

    protected void onLoadEmptyBefore() {

    }

    protected void onLoadTargetBefore() {

    }

    protected abstract Bitmap getRoleBitmap(Direction direction, float width, float height);

    protected abstract Bitmap getBoxBitmap(Direction direction, float width, float height);

    protected abstract Bitmap getWallBitmap(Direction direction, float width, float height);

    protected abstract Bitmap getEmptyBitmap(Direction direction, float width, float height);

    protected abstract Bitmap getTargetBitmap(Direction direction, float width, float height);

}

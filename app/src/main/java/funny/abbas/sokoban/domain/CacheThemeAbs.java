package funny.abbas.sokoban.domain;

import android.graphics.Bitmap;
import android.util.Pair;

import java.util.HashMap;

public abstract class CacheThemeAbs implements Theme {
    private final HashMap<BoxType, Bitmap> bitmapCache = new HashMap<>();
    private final HashMap<BoxType, Pair<Float, Float>> sizeRemember = new HashMap<>();

    @Override
    public Bitmap getRole(float width, float height) {
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
            bitmapCache.put(BoxType.Role, getRoleBitmap(width, height));
        }
        return bitmapCache.get(BoxType.Role);
    }

    @Override
    public Bitmap getBox(float width, float height) {
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
            bitmapCache.put(BoxType.Box, getBoxBitmap(width, height));
        }
        return bitmapCache.get(BoxType.Box);
    }

    @Override
    public Bitmap getWall(float width, float height) {
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
            bitmapCache.put(BoxType.Wall, getWallBitmap(width, height));
        }
        return bitmapCache.get(BoxType.Wall);
    }

    @Override
    public Bitmap getEmpty(float width, float height) {
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
            bitmapCache.put(BoxType.Empty, getEmptyBitmap(width, height));
        }
        return bitmapCache.get(BoxType.Empty);
    }

    @Override
    public Bitmap getTarget(float width, float height) {
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
            bitmapCache.put(BoxType.Target, getTargetBitmap(width, height));
        }
        return bitmapCache.get(BoxType.Target);
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

    protected abstract Bitmap getRoleBitmap(float width, float height);

    protected abstract Bitmap getBoxBitmap(float width, float height);

    protected abstract Bitmap getWallBitmap(float width, float height);

    protected abstract Bitmap getEmptyBitmap(float width, float height);

    protected abstract Bitmap getTargetBitmap(float width, float height);

}

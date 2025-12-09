package funny.abbas.sokoban.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import funny.abbas.sokoban.MyApplication;
import funny.abbas.sokoban.view.Direction;

public class ClassicTheme1 extends CacheThemeAbs implements Theme {

    private Bitmap skinBitmaps;
    private final Map<Direction, Bitmap> roleCacheMap = new HashMap<>();
    private List<Bitmap> leftRoleFrame;
    private List<Bitmap> topRoleFrame;
    private List<Bitmap> rightRoleFrame;
    private List<Bitmap> bottomRoleFrame;

    private int leftTimes;
    private int topTimes;
    private int rightTimes;
    private int bottomTimes;

    public ClassicTheme1() {

    }

    private void ensureSkinGroup() {
        if (skinBitmaps == null) {
            initSkinGroup();
        }
    }

    private void initSkinGroup() {
        try (InputStream skins = MyApplication.application.getAssets()
                .open("skin/sokoban_sprites.png")) {
            skinBitmaps = BitmapFactory.decodeStream(skins);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Bitmap> loadLeftRoleFrame(int width, int height) {
        Bitmap leftFrame0 = clipRoleBitmap(320, 187, width, height);
        Bitmap leftFrame1 = clipRoleBitmap(320, 305, width, height);
        return Arrays.asList(leftFrame0, leftFrame1);
    }

    private List<Bitmap> loadTopRoleFrame(int width, int height) {
        Bitmap topFrame0 = clipRoleBitmap(384, 0, width, height);
        Bitmap topFrame1 = clipRoleBitmap(362, 128, width, height);
        Bitmap topFrame2 = clipRoleBitmap(362, 187, width, height);
        return Arrays.asList(topFrame0, topFrame1, topFrame2);
    }

    private List<Bitmap> loadRightRoleFrame(int width, int height) {
        Bitmap rightFrame0 = clipRoleBitmap(320, 246, width, height);
        Bitmap rightFrame1 = clipRoleBitmap(320, 128, width, height);
        return Arrays.asList(rightFrame0, rightFrame1);
    }

    private List<Bitmap> loadBottomRoleFrame(int width, int height) {
        Bitmap bottomFrame0 = clipRoleBitmap(362, 248, width, height);
        Bitmap bottomFrame1 = clipRoleBitmap(320, 362, width, height);
        Bitmap bottomFrame2 = clipRoleBitmap(357, 362, width, height);
        return Arrays.asList(bottomFrame0, bottomFrame1, bottomFrame2);
    }

    private Bitmap clipRoleBitmap(int x, int y, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(skinBitmaps, x, y, 37, 59);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (width * 0.8), (int) (height * 0.8), true);
        Bitmap combainBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas combainCanvas = new Canvas(combainBitmap);
        combainCanvas.drawBitmap(scaledBitmap, width * 0.1f, height * 0.1f, null);
        bitmap.recycle();
        scaledBitmap.recycle();
        return combainBitmap;
    }

    @Override
    protected void onLoadRoleBefore() {
        ensureSkinGroup();
    }

    @Override
    public Bitmap getRole(Direction direction, float width, float height) {
        onLoadRoleBefore();
        Bitmap cacheBitmap = roleCacheMap.get(direction);
        Pair<Float, Float> rememberPair = sizeRemember.get(BoxType.Role);
        if (rememberPair != null) {
            if (rememberPair.first != width || rememberPair.second != height) {
                sizeRemember.put(BoxType.Role, new Pair<>(width, height));
                cacheBitmap = null;
                roleCacheMap.clear();
            }
        } else {
            sizeRemember.put(BoxType.Role, new Pair<>(width, height));
        }
        if (cacheBitmap == null) {
            roleCacheMap.put(direction, getRoleBitmap(direction, width, height));
        }
        return roleCacheMap.get(direction);
    }

    @Override
    protected Bitmap getRoleBitmap(Direction direction, float width, float height) {
//        Bitmap bitmap = Bitmap.createBitmap(skinBitmaps, 361, 248, 37, 59);
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (width * 0.8), (int) (height * 0.8), true);
//        Bitmap combainBitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
//        Canvas combainCanvas = new Canvas(combainBitmap);
//        combainCanvas.drawBitmap(scaledBitmap, width * 0.1f, height * 0.1f, null);
//        bitmap.recycle();
//        scaledBitmap.recycle();
//        return combainBitmap;
        switch (direction) {
            case BOTTOM:
                bottomRoleFrame = loadBottomRoleFrame((int) width, (int) height);
                return bottomRoleFrame.get(0);
            case TOP:
                topRoleFrame = loadTopRoleFrame((int) width, (int) height);
                return topRoleFrame.get(0);
            case RIGHT:
                rightRoleFrame = loadRightRoleFrame((int) width, (int) height);
                return rightRoleFrame.get(0);
            case LEFT:
                leftRoleFrame = loadLeftRoleFrame((int) width, (int) height);
                return leftRoleFrame.get(0);
        }
        throw new IllegalStateException("error");
    }


    @Override
    protected void onLoadBoxBefore() {
        ensureSkinGroup();
    }

    @Override
    protected Bitmap getBoxBitmap(Direction direction, float width, float height) {
        Bitmap bitmap = Bitmap.createBitmap(skinBitmaps, 192, 256, 64, 64);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
        bitmap.recycle();
        return scaledBitmap;
    }

    @Override
    protected void onLoadWallBefore() {
        ensureSkinGroup();
    }

    @Override
    protected Bitmap getWallBitmap(Direction direction, float width, float height) {
        Bitmap bitmap = Bitmap.createBitmap(skinBitmaps, 0, 0, 64, 64);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
        bitmap.recycle();
        return scaledBitmap;
    }

    @Override
    protected void onLoadEmptyBefore() {
        ensureSkinGroup();
    }

    @Override
    protected Bitmap getEmptyBitmap(Direction direction, float width, float height) {
        Bitmap bitmap = Bitmap.createBitmap(skinBitmaps, 64, 128, 64, 64);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
        bitmap.recycle();
        return scaledBitmap;
    }

    @Override
    protected void onLoadTargetBefore() {
        ensureSkinGroup();
    }

    @Override
    protected Bitmap getTargetBitmap(Direction direction, float width, float height) {
        Bitmap bitmap = Bitmap.createBitmap(skinBitmaps, 0, 384, 32, 32);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (width * 0.7f), (int) (height * 0.7f), true);
        Bitmap block = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
        Canvas combaineCanvas = new Canvas(block);
        combaineCanvas.drawBitmap(scaledBitmap, width * 0.15f, height * 0.15f, null);
        bitmap.recycle();
        scaledBitmap.recycle();
        return block;
    }

    @Override
    public Bitmap getAnimateRole(Direction direction, float width, float height) {
        Bitmap cacheBitmap = roleCacheMap.get(direction);
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
            roleCacheMap.put(direction, getRoleBitmap(direction, width, height));
        }
        switch (direction) {
            case LEFT:
                return leftRoleFrame.get(leftTimes++ % leftRoleFrame.size());
            case TOP:
                return topRoleFrame.get(topTimes++ % topRoleFrame.size());
            case RIGHT:
                return rightRoleFrame.get(rightTimes++ % rightRoleFrame.size());
            case BOTTOM:
                return bottomRoleFrame.get(bottomTimes++ % bottomRoleFrame.size());
        }
        return roleCacheMap.get(direction);
    }
}

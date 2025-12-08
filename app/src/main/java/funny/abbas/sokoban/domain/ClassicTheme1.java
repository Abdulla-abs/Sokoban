package funny.abbas.sokoban.domain;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import funny.abbas.sokoban.MyApplication;

public class ClassicTheme1 implements Theme {

    private Bitmap skinBitmaps;
    private final HashMap<BoxType, Bitmap> bitmapCache = new HashMap<>();
    private final HashMap<BoxType, Pair<Float,Float>> sizeRemember = new HashMap<>();

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

    @Override
    public Bitmap getRole(float width, float height) {
        ensureSkinGroup();
        Bitmap cacheBitmap = bitmapCache.get(BoxType.Role);
        Pair<Float, Float> rememberPair = sizeRemember.get(BoxType.Role);
        if (rememberPair != null) {
            if (rememberPair.first != width || rememberPair.second != height){
                sizeRemember.put(BoxType.Role,new Pair<>(width,height));
                cacheBitmap = null;
            }
        }else {
            sizeRemember.put(BoxType.Role,new Pair<>(width,height));
        }
        if (cacheBitmap == null) {
            Bitmap bitmap = Bitmap.createBitmap(skinBitmaps, 361, 248, 37, 59);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (width * 0.8), (int) (height * 0.8), true);
            Bitmap combainBitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
            Canvas combainCanvas = new Canvas(combainBitmap);
            combainCanvas.drawBitmap(scaledBitmap, width * 0.1f, height * 0.1f,null);
            bitmapCache.put(BoxType.Role, combainBitmap);
            bitmap.recycle();
            scaledBitmap.recycle();
        }
        return bitmapCache.get(BoxType.Role);
    }

    @Override
    public Bitmap getBox(float width, float height) {
        ensureSkinGroup();
        Bitmap cacheBitmap = bitmapCache.get(BoxType.Box);
        Pair<Float, Float> rememberPair = sizeRemember.get(BoxType.Box);
        if (rememberPair != null) {
            if (rememberPair.first != width || rememberPair.second != height){
                sizeRemember.put(BoxType.Box,new Pair<>(width,height));
                cacheBitmap = null;
            }
        }else {
            sizeRemember.put(BoxType.Box,new Pair<>(width,height));
        }
        if (cacheBitmap == null) {
            Bitmap bitmap = Bitmap.createBitmap(skinBitmaps, 192, 256, 64, 64);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
            bitmapCache.put(BoxType.Box, scaledBitmap);
            bitmap.recycle();
        }
        return bitmapCache.get(BoxType.Box);
    }

    @Override
    public Bitmap getWall(float width, float height) {
        ensureSkinGroup();
        Bitmap cacheBitmap = bitmapCache.get(BoxType.Wall);
        Pair<Float, Float> rememberPair = sizeRemember.get(BoxType.Wall);
        if (rememberPair != null) {
            if (rememberPair.first != width || rememberPair.second != height){
                sizeRemember.put(BoxType.Wall,new Pair<>(width,height));
                cacheBitmap = null;
            }
        }else {
            sizeRemember.put(BoxType.Wall,new Pair<>(width,height));
        }
        if (cacheBitmap == null) {
            Bitmap bitmap = Bitmap.createBitmap(skinBitmaps, 0, 0, 64, 64);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
            bitmapCache.put(BoxType.Wall, scaledBitmap);
            bitmap.recycle();
        }
        return bitmapCache.get(BoxType.Wall);
    }

    @Override
    public Bitmap getEmpty(float width, float height) {
        ensureSkinGroup();
        Bitmap cacheBitmap = bitmapCache.get(BoxType.Empty);
        Pair<Float, Float> rememberPair = sizeRemember.get(BoxType.Empty);
        if (rememberPair != null) {
            if (rememberPair.first != width || rememberPair.second != height){
                sizeRemember.put(BoxType.Empty,new Pair<>(width,height));
                cacheBitmap = null;
            }
        }else {
            sizeRemember.put(BoxType.Empty,new Pair<>(width,height));
        }
        if (cacheBitmap == null) {
            Bitmap bitmap = Bitmap.createBitmap(skinBitmaps, 64, 128, 64, 64);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
            bitmapCache.put(BoxType.Empty, scaledBitmap);
            bitmap.recycle();
        }
        return bitmapCache.get(BoxType.Empty);
    }

    @Override
    public Bitmap getTarget(float width, float height) {
        ensureSkinGroup();
        Bitmap cacheBitmap = bitmapCache.get(BoxType.Target);
        Pair<Float, Float> rememberPair = sizeRemember.get(BoxType.Target);
        if (rememberPair != null) {
            if (rememberPair.first != width || rememberPair.second != height){
                sizeRemember.put(BoxType.Target,new Pair<>(width,height));
                cacheBitmap = null;
            }
        }else {
            sizeRemember.put(BoxType.Target,new Pair<>(width,height));
        }
        if (cacheBitmap == null) {
            Bitmap bitmap = Bitmap.createBitmap(skinBitmaps, 0, 384, 32, 32);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (width * 0.7f), (int) (height * 0.7f), true);
            Bitmap block = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
            Canvas combaineCanvas = new Canvas(block);
            combaineCanvas.drawBitmap(scaledBitmap, width * 0.15f, height * 0.15f, null);
            bitmapCache.put(BoxType.Target, block);
            bitmap.recycle();
            scaledBitmap.recycle();
        }
        return bitmapCache.get(BoxType.Target);
    }

    @Override
    public void recycleBitmaps() {
        bitmapCache.clear();
    }
}

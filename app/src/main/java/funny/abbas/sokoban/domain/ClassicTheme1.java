package funny.abbas.sokoban.domain;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import funny.abbas.sokoban.MyApplication;

public class ClassicTheme1 extends CacheThemeAbs implements Theme {

    private Bitmap skinBitmaps;

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
    protected void onLoadRoleBefore() {
        ensureSkinGroup();
    }

    @Override
    protected Bitmap getRoleBitmap(float width, float height) {
        Bitmap bitmap = Bitmap.createBitmap(skinBitmaps, 361, 248, 37, 59);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (width * 0.8), (int) (height * 0.8), true);
        Bitmap combainBitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
        Canvas combainCanvas = new Canvas(combainBitmap);
        combainCanvas.drawBitmap(scaledBitmap, width * 0.1f, height * 0.1f,null);
        bitmap.recycle();
        scaledBitmap.recycle();
        return combainBitmap;
    }


    @Override
    protected void onLoadBoxBefore() {
        ensureSkinGroup();
    }

    @Override
    protected Bitmap getBoxBitmap(float width, float height) {
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
    protected Bitmap getWallBitmap(float width, float height) {
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
    protected Bitmap getEmptyBitmap(float width, float height) {
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
    protected Bitmap getTargetBitmap(float width, float height) {
        Bitmap bitmap = Bitmap.createBitmap(skinBitmaps, 0, 384, 32, 32);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (width * 0.7f), (int) (height * 0.7f), true);
        Bitmap block = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
        Canvas combaineCanvas = new Canvas(block);
        combaineCanvas.drawBitmap(scaledBitmap, width * 0.15f, height * 0.15f, null);
        bitmap.recycle();
        scaledBitmap.recycle();
        return block;
    }
}

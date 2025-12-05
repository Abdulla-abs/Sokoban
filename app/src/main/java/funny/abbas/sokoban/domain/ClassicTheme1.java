package funny.abbas.sokoban.domain;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import funny.abbas.sokoban.MyApplication;

public class ClassicTheme1 implements Theme{

    private Bitmap skinBitmaps;
    private final HashMap<BoxType,Bitmap> bitmapCache = new HashMap<>();

    public ClassicTheme1() {

    }

    private void ensureSkinGroup(){
        if (skinBitmaps == null){
            initSkinGroup();
        }
    }

    private void initSkinGroup(){
        try (InputStream skins = MyApplication.application.getAssets()
                .open("skin/sokoban_sprites.png")){
            skinBitmaps = BitmapFactory.decodeStream(skins);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Bitmap getRole(float width, float height) {
        ensureSkinGroup();
        Bitmap cacheBitmap = bitmapCache.get(BoxType.Role);
        if (cacheBitmap == null){
            Bitmap bitmap = Bitmap.createBitmap(skinBitmaps, 362, 248, 37, 59);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
            bitmapCache.put(BoxType.Role,scaledBitmap);
        }
        return bitmapCache.get(BoxType.Role);
    }

    @Override
    public Bitmap getBox(float width, float height) {
        ensureSkinGroup();
        Bitmap cacheBitmap = bitmapCache.get(BoxType.Box);
        if (cacheBitmap == null){
            Bitmap bitmap = Bitmap.createBitmap(skinBitmaps, 192, 256, 64, 64);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
            bitmapCache.put(BoxType.Box,scaledBitmap);
        }
        return bitmapCache.get(BoxType.Box);
    }

    @Override
    public Bitmap getWall(float width, float height) {
        ensureSkinGroup();
        Bitmap cacheBitmap = bitmapCache.get(BoxType.Wall);
        if (cacheBitmap == null){
            Bitmap bitmap = Bitmap.createBitmap(skinBitmaps, 0, 0, 64, 64);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
            bitmapCache.put(BoxType.Wall,scaledBitmap);
        }
        return bitmapCache.get(BoxType.Wall);
    }

    @Override
    public Bitmap getEmpty(float width, float height) {
        ensureSkinGroup();
        Bitmap cacheBitmap = bitmapCache.get(BoxType.Empty);
        if (cacheBitmap == null){
            Bitmap bitmap = Bitmap.createBitmap(skinBitmaps, 64, 128, 64, 64);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
            bitmapCache.put(BoxType.Empty,scaledBitmap);
        }
        return bitmapCache.get(BoxType.Empty);
    }

    @Override
    public Bitmap getTarget(float width, float height) {
        ensureSkinGroup();
        Bitmap cacheBitmap = bitmapCache.get(BoxType.Target);
        if (cacheBitmap == null){
            Bitmap bitmap = Bitmap.createBitmap(skinBitmaps, 0, 384, 30, 30);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
            bitmapCache.put(BoxType.Target,scaledBitmap);
        }
        return bitmapCache.get(BoxType.Target);
    }

    @Override
    public void recycleBitmaps() {
        bitmapCache.clear();
    }
}

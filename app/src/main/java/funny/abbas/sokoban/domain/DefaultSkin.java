package funny.abbas.sokoban.domain;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.HashMap;

public class DefaultSkin implements Theme {

    private final HashMap<BoxType, Bitmap> boxBitmapCache = new HashMap<>();
    private final Paint paint = new Paint();
    private final Paint targetPaint = new Paint();

    public DefaultSkin() {
        targetPaint.setColor(Color.RED);
        targetPaint.setAntiAlias(true);
        targetPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public Bitmap getRole(float width, float height) {
        Bitmap cacheBitmap = boxBitmapCache.get(BoxType.Role);
        if (cacheBitmap == null) {
            Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            paint.setColor(Color.CYAN);
            canvas.drawRect(0, 0, width, height, paint);
            boxBitmapCache.put(BoxType.Role, bitmap);
        }

        return boxBitmapCache.get(BoxType.Role);
    }

    @Override
    public Bitmap getBox(float width, float height) {
        Bitmap cacheBitmap = boxBitmapCache.get(BoxType.Box);
        if (cacheBitmap == null) {
            Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            paint.setColor(Color.GREEN);
            canvas.drawRect(0, 0, width, height, paint);
            boxBitmapCache.put(BoxType.Box, bitmap);
        }

        return boxBitmapCache.get(BoxType.Box);
    }

    @Override
    public Bitmap getWall(float width, float height) {
        Bitmap cacheBitmap = boxBitmapCache.get(BoxType.Wall);
        if (cacheBitmap == null) {
            Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            paint.setColor(Color.BLACK);
            canvas.drawRect(0, 0, width, height, paint);
            boxBitmapCache.put(BoxType.Wall, bitmap);
        }

        return boxBitmapCache.get(BoxType.Wall);
    }

    @Override
    public Bitmap getEmpty(float width, float height) {
        Bitmap cacheBitmap = boxBitmapCache.get(BoxType.Empty);
        if (cacheBitmap == null) {
            Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            paint.setColor(Color.WHITE);
            canvas.drawRect(0, 0, width, height, paint);
            boxBitmapCache.put(BoxType.Empty, bitmap);
        }

        return boxBitmapCache.get(BoxType.Empty);
    }

    @Override
    public Bitmap getTarget(float width, float height) {
        Bitmap cacheBitmap = boxBitmapCache.get(BoxType.Target);
        if (cacheBitmap == null) {
            Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.TRANSPARENT);
            targetPaint.setStrokeWidth(width * 0.1f);
            canvas.drawCircle(width / 2, height / 2, (width - width * 0.2f) / 2, targetPaint);
            boxBitmapCache.put(BoxType.Target, bitmap);
        }

        return boxBitmapCache.get(BoxType.Target);
    }

    @Override
    public void recycleBitmaps() {
        boxBitmapCache.clear();
    }
}

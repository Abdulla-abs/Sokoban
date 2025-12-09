package funny.abbas.sokoban.domain;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.HashMap;

public class DefaultSkin extends CacheThemeAbs implements Theme {

    private final Paint paint = new Paint();
    private final Paint targetPaint = new Paint();

    public DefaultSkin() {
        targetPaint.setColor(Color.RED);
        targetPaint.setAntiAlias(true);
        targetPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected Bitmap getRoleBitmap(float width, float height) {
        Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        paint.setColor(Color.CYAN);
        canvas.drawRect(0, 0, width, height, paint);
        return bitmap;
    }

    @Override
    protected Bitmap getBoxBitmap(float width, float height) {
        Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        paint.setColor(Color.GREEN);
        canvas.drawRect(0, 0, width, height, paint);
        return bitmap;
    }

    @Override
    protected Bitmap getWallBitmap(float width, float height) {
        Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, width, height, paint);
        return bitmap;
    }

    @Override
    protected Bitmap getEmptyBitmap(float width, float height) {
        Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, width, height, paint);
        return bitmap;
    }

    @Override
    protected Bitmap getTargetBitmap(float width, float height) {
        Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);
        targetPaint.setStrokeWidth(width * 0.1f);
        canvas.drawCircle(width / 2, height / 2, (width - width * 0.2f) / 2, targetPaint);
        return bitmap;
    }
}

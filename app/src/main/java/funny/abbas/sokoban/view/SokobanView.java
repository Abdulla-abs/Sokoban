package funny.abbas.sokoban.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import funny.abbas.sokoban.domain.Box;
import funny.abbas.sokoban.domain.BoxType;
import funny.abbas.sokoban.domain.Empty;
import funny.abbas.sokoban.domain.Level;
import funny.abbas.sokoban.domain.Location;
import funny.abbas.sokoban.domain.MapObject;
import funny.abbas.sokoban.domain.Role;
import funny.abbas.sokoban.domain.Skin;
import funny.abbas.sokoban.domain.Theme;
import funny.abbas.sokoban.domain.Wall;

public class SokobanView extends View implements Action {

    private final float standerBoxSize = 80f;
    private float measuredBoxSize = standerBoxSize;

    private final Skin skin = Skin.getInstance();


    private final Controller controller = new Controller();

    public SokobanView(Context context) {
        super(context);
    }

    public SokobanView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (controller.level == null) return;
        int boxColumnCount = controller.level.map.length;
        int boxRowCount = controller.level.map[0].length;

        float standardWidth = boxRowCount * standerBoxSize;
        float standardHeight = boxColumnCount * standerBoxSize;

        int widthModel = MeasureSpec.getMode(widthMeasureSpec);
        int heightModel = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (standardHeight > heightSize || standardWidth > widthSize) {
            //测量宽高比预期宽高要小，调整格子大小比例
            float widthOverSize = standardWidth - widthSize;
            float heightOverSize = standardHeight - heightSize;

            if (widthOverSize > 0 && heightOverSize > 0) {
                if (widthOverSize > heightOverSize) {
                    float scanPercent = standardWidth / widthSize;
                    //result
                    measuredBoxSize = standerBoxSize * scanPercent;
                } else {
                    float scanPercent = standardHeight / heightSize;
                    //result
                    measuredBoxSize = standerBoxSize * scanPercent;
                }
            } else if (widthOverSize > 0) {
                float scanPercent = standardWidth / widthSize;
                //result
                measuredBoxSize = standerBoxSize * scanPercent;
            } else if (heightOverSize > 0) {
                float scanPercent = standardHeight / heightSize;
                //result
                measuredBoxSize = standerBoxSize * scanPercent;
            }
        } else {
            float roamingHeight = heightSize - standardHeight;
            float roamingWidth = widthSize - standardWidth;

            if (roamingWidth > roamingHeight) {
                float heightPercent = heightSize / standardHeight;
                measuredBoxSize = standerBoxSize * heightPercent;
            } else {
                float widthPercent = widthSize / standardWidth;
                measuredBoxSize = standerBoxSize * widthPercent;
            }
        }

        float measuredWidth = boxRowCount * measuredBoxSize;
        float measureHeight = boxColumnCount * measuredBoxSize;

        setMeasuredDimension((int) Math.ceil(measuredWidth), (int) Math.ceil(measureHeight));
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if (controller.level == null) return;

        for (int i = 0; i < controller.level.map.length; i++) {
            for (int j = 0; j < controller.level.map[0].length; j++) {
                MapObject box = controller.level.map[i][j];
                Bitmap bitmap = skin.loadSkin(box.getBoxType(), measuredBoxSize, measuredBoxSize);
                canvas.drawBitmap(bitmap, box.getLocation().getX() * measuredBoxSize,
                        box.getLocation().getY() * measuredBoxSize, null);
            }
        }

        for (Location location : controller.level.target) {
            Bitmap bitmap = skin.loadSkin(BoxType.Target, measuredBoxSize, measuredBoxSize);
            canvas.drawBitmap(bitmap, location.getX() * measuredBoxSize, location.getY() * measuredBoxSize,
                    null);
        }
    }

    public void setStateListener(GameStateListener stateListener) {
        this.controller.stateListener = stateListener;
    }

    public void setLevel(Level level) {
        controller.level = level;
        requestLayout();
    }

    public void replaceSkin(Theme theme) {
        skin.setTheme(theme);
        invalidate();
    }

    @Override
    public boolean moveLeft() {
        if (controller.moveLeft()) {
            invalidate();
            return true;
        }
        return false;
    }

    @Override
    public boolean moveTop() {
        if (controller.moveTop()) {
            invalidate();
            return true;
        }
        return false;
    }

    @Override
    public boolean moveRight() {
        if (controller.moveRight()) {
            invalidate();
            return true;
        }
        return false;
    }

    @Override
    public boolean moveBottom() {
        if (controller.moveBottom()) {
            invalidate();
            return true;
        }
        return false;
    }
}

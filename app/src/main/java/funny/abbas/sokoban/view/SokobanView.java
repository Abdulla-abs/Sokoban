package funny.abbas.sokoban.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import funny.abbas.sokoban.domain.Box;
import funny.abbas.sokoban.domain.Empty;
import funny.abbas.sokoban.domain.Level;
import funny.abbas.sokoban.domain.Location;
import funny.abbas.sokoban.domain.MapObject;
import funny.abbas.sokoban.domain.Role;
import funny.abbas.sokoban.domain.Wall;

public class SokobanView extends View {

    private GameStateListener stateListener;
    private final float standerBoxSize = 80f;
    private float measuredBoxSize = standerBoxSize;
    private float targetCircleRadius = (standerBoxSize - standerBoxSize * 0.2f) / 2;


    private final Paint defaultPaint = new Paint();
    private final Paint targetCirclePaint = new Paint();


    private final Controller controller = new MyController();

    public SokobanView(Context context) {
        super(context);

        targetCirclePaint.setColor(Color.RED);
        targetCirclePaint.setAntiAlias(true);
        targetCirclePaint.setStyle(Paint.Style.STROKE);
        targetCirclePaint.setStrokeWidth(standerBoxSize * 0.1f);
    }

    public SokobanView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        targetCirclePaint.setColor(Color.RED);
        targetCirclePaint.setAntiAlias(true);
        targetCirclePaint.setStyle(Paint.Style.STROKE);
        targetCirclePaint.setStrokeWidth(standerBoxSize * 0.1f);
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
        targetCircleRadius = (measuredBoxSize - measuredBoxSize * 0.2f) / 2;
        targetCirclePaint.setStrokeWidth(measuredBoxSize * 0.1f);

        setMeasuredDimension((int) Math.ceil(measuredWidth), (int) Math.ceil(measureHeight));
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if (controller.level == null) return;

        for (int i = 0; i < controller.level.map.length; i++) {
            for (int j = 0; j < controller.level.map[0].length; j++) {
                MapObject box = controller.level.map[i][j];
                if (box instanceof Wall) {
                    defaultPaint.setColor(Color.BLACK);
                } else if (box instanceof Empty) {
                    defaultPaint.setColor(Color.WHITE);
                } else if (box instanceof Role) {
                    defaultPaint.setColor(Color.CYAN);
                } else if (box instanceof Box) {
                    defaultPaint.setColor(Color.GREEN);
                }
                canvas.drawRect(box.getLocation().getX() * measuredBoxSize,
                        box.getLocation().getY() * measuredBoxSize,
                        box.getLocation().getX() * measuredBoxSize + measuredBoxSize,
                        box.getLocation().getY() * measuredBoxSize + measuredBoxSize,
                        defaultPaint);
            }
        }

        for (Location location : controller.level.target) {
            canvas.drawCircle(location.getX() * measuredBoxSize + measuredBoxSize / 2,
                    location.getY() * measuredBoxSize + measuredBoxSize / 2, targetCircleRadius,
                    targetCirclePaint);
        }
    }

    public void setStateListener(GameStateListener stateListener) {
        this.stateListener = stateListener;
    }

    public void setLevel(Level level) {
        controller.level = level;
        requestLayout();
    }

    public Action getController() {
        return controller.action;
    }

    private class MyController extends Controller {

        @Override
        protected void onMoveLeft() {
            if (controller.level.getRole().canMoveLeft()) {
                controller.level.getRole().moveLeft(null);
            }
            invalidate();
        }

        @Override
        protected void onMoveTop() {
            if (controller.level.getRole().canMoveTop()) {
                controller.level.getRole().moveUp(null);
            }
            invalidate();
        }

        @Override
        protected void onMoveRight() {
            if (controller.level.getRole().canMoveRight()) {
                controller.level.getRole().moveRight(null);
            }
            invalidate();
        }

        @Override
        protected void onMoveBottom() {
            if (controller.level.getRole().canMoveBottom()) {
                controller.level.getRole().moveBottom(null);
            }
            invalidate();
        }

        @Override
        protected void onGameOver() {
            if (stateListener != null) {
                stateListener.onSuccess();
            }
        }
    }
}

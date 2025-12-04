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
import funny.abbas.sokoban.domain.MapObject;
import funny.abbas.sokoban.domain.Role;
import funny.abbas.sokoban.domain.Wall;

public class SokobanView extends View {

    private Level level;

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
        if (level == null) return;
        int boxColumnCount = level.map.length;
        int boxRowCount = level.map[0].length;

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
        for (int i = 0; i < level.map.length; i++) {
            for (int j = 0; j < level.map[0].length; j++) {
                MapObject box = level.map[i][j];
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

//        canvas.drawCircle(j * measuredBoxSize + measuredBoxSize / 2,
//                i * measuredBoxSize + measuredBoxSize / 2, targetCircleRadius,
//                targetCirclePaint);


    }

    public void setLevel(Level level) {
        this.level = level;
        invalidate();
    }

    public Action getController() {
        return controller.action;
    }

    private class MyController extends Controller {

        @Override
        protected void onMoveLeft() {
            if (level.getRole().canMoveLeft()) {
                level.getRole().moveLeft(null);
            }
            invalidate();
        }

        @Override
        protected void onMoveTop() {
            if (level.getRole().canMoveTop()) {
                level.getRole().moveUp(null);
            }
            invalidate();
        }

        @Override
        protected void onMoveRight() {
            if (level.getRole().canMoveRight()) {
                level.getRole().moveRight(null);
            }
            invalidate();
        }

        @Override
        protected void onMoveBottom() {
            if (level.getRole().canMoveBottom()) {
                level.getRole().moveBottom(null);
            }
            invalidate();
        }
    }
}

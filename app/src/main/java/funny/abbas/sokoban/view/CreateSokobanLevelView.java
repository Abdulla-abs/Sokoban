package funny.abbas.sokoban.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import funny.abbas.sokoban.core.CreateSokobanListener;
import funny.abbas.sokoban.core.Direction;
import funny.abbas.sokoban.core.map.BoxType;
import funny.abbas.sokoban.core.CreateSokobanDataLayer;
import funny.abbas.sokoban.core.CreateSokobanMode;
import funny.abbas.sokoban.core.Level;
import funny.abbas.sokoban.core.LevelMapper;
import funny.abbas.sokoban.core.Location;
import funny.abbas.sokoban.core.skin.Skin;
import funny.abbas.sokoban.state.createsokoban.CreateSokobanAction;
import funny.abbas.sokoban.state.createsokoban.CreateSokobanState;
import funny.abbas.sokoban.util.Result;

public class CreateSokobanLevelView extends View {

    private static final String TAG = "CreateSokobanLevelView";
    //调整此参数以间隔棋盘和工具栏距离
    private final float defaultBottomBarMargin = 120f;
    private float measuredBoxSize = 80f;
    private int gridRows = 6;    // 网格实际行数（固定）
    private int gridCols = 6;    // 网格实际列数（固定）


    // 记录被拖动的方块类型
    private BoxType draggedBoxType = null;
    // 记录拖动时的起始触摸位置（用于计算偏移量）
    private float startTouchX = 0f;
    private float startTouchY = 0f;
    // 记录拖动时的当前触摸位置（用于绘制跟随手指的拖动阴影）
    private float currentDragX = -1f;
    private float currentDragY = -1f;

    private int dropCol = -1;
    private int dropRow = -1;

    private final Paint halfAlphaPaint = new Paint();
    private final Paint cancelAreaPaint = new Paint();
    private final Paint cancelTextPaint = new Paint();
    private final Rect textBounds = new Rect();

    private final Skin skin = Skin.getInstance();
    private final CreateSokobanMode stateMode = new CreateSokobanMode();
    private final CreateSokobanDataLayer dataLayer = new CreateSokobanDataLayer();
    private CreateSokobanListener listener;

    public CreateSokobanLevelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        halfAlphaPaint.setAlpha(120);
        cancelAreaPaint.setColor(Color.RED);
        cancelAreaPaint.setAlpha(160);

        cancelTextPaint.setColor(Color.RED);
        cancelTextPaint.setAntiAlias(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSize < heightSize) {
            measuredBoxSize = (float) widthSize / gridRows;
        } else {
            measuredBoxSize = (float) heightSize / gridCols;
        }

        float measuredWidth = gridCols * measuredBoxSize;
        float measureHeight = gridRows * measuredBoxSize + measuredBoxSize + defaultBottomBarMargin;
        float sp = spFromCell(measuredBoxSize);
        cancelTextPaint.setTextSize(sp * getResources().getDisplayMetrics().density);

        setMeasuredDimension((int) Math.ceil(measuredWidth), (int) Math.ceil(measureHeight));
    }

    private final List<BoxType> basicBoxType = Arrays.asList(BoxType.Empty, BoxType.Wall);
    private final List<BoxType> targetBoxType = List.of(BoxType.Target);
    private final List<BoxType> boxBoxType = List.of(BoxType.Box);
    private final List<BoxType> roleBoxType = List.of(BoxType.Role);

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < gridRows; i++) {
            for (int j = 0; j < gridCols; j++) {
                Bitmap bitmap = skin.loadSkin(dataLayer.getBasicMap().get(i, j), Direction.BOTTOM, measuredBoxSize, measuredBoxSize);
                canvas.drawBitmap(bitmap, j * measuredBoxSize, i * measuredBoxSize,
                        null);
            }
        }

        for (Location target : dataLayer.getTargets()) {
            Bitmap bitmap = skin.loadSkin(BoxType.Target,Direction.BOTTOM, measuredBoxSize, measuredBoxSize);
            canvas.drawBitmap(bitmap, target.getX() * measuredBoxSize,
                    target.getY() * measuredBoxSize,
                    null);
        }

        for (Location box : dataLayer.getBoxes()) {
            Bitmap bitmap = skin.loadSkin(BoxType.Box,Direction.BOTTOM, measuredBoxSize, measuredBoxSize);
            canvas.drawBitmap(bitmap, box.getX() * measuredBoxSize,
                    box.getY() * measuredBoxSize,
                    null);
        }

        dataLayer.getRole().ifPresent(new Consumer<Location>() {
            @Override
            public void accept(Location location) {
                Bitmap bitmap = skin.loadSkin(BoxType.Role,Direction.BOTTOM, measuredBoxSize, measuredBoxSize);
                canvas.drawBitmap(bitmap, location.getX() * measuredBoxSize,
                        location.getY() * measuredBoxSize,
                        null);
            }
        });


        //这部分要拖动到上面的创建的格子当中
        List<BoxType> stateBoxType = Collections.emptyList();
        switch (stateMode.getCurrentState()) {
            case PUT_BASIC:
                stateBoxType = basicBoxType;
                break;
            case PUT_TARGET:
                stateBoxType = targetBoxType;
                break;
            case PUT_BOX:
                stateBoxType = boxBoxType;
                break;
            case PUT_ROLE:
                stateBoxType = roleBoxType;
                break;
            default:
                break;
        }
        float boxWidth = stateBoxType.size() * measuredBoxSize;
        float margin = (getWidth() - boxWidth) / 2;
        float bottomBarY = gridRows * measuredBoxSize + defaultBottomBarMargin;
        for (int i = 0; i < stateBoxType.size(); i++) {
            BoxType value = stateBoxType.get(i);
            Bitmap bitmap = skin.loadSkin(value,Direction.BOTTOM, measuredBoxSize, measuredBoxSize);
            canvas.drawBitmap(bitmap, i * measuredBoxSize + margin,
                    bottomBarY,
                    null);
            Log.i(TAG, "measuredBoxSize: " + measuredBoxSize + "\nmargin:" + margin);
        }


        // --- 绘制拖动阴影 ---
        if (draggedBoxType != null && currentDragX != -1f) {
            Bitmap bitmap = skin.loadSkin(draggedBoxType,Direction.BOTTOM, measuredBoxSize, measuredBoxSize);
            //绘制预放置格子
            if (dropCol >= 0 && dropCol < gridCols && dropRow >= 0 && dropRow < gridRows) {
                canvas.drawBitmap(bitmap, dropCol * measuredBoxSize, dropRow * measuredBoxSize,
                        null);
            }
            // 2. 绘制拖动阴影，以 currentDragX, currentDragY 为中心点或左上角
            // 这里使用 currentDragX/Y 作为左上角绘制
            canvas.drawBitmap(bitmap, currentDragX - measuredBoxSize / 2, currentDragY - measuredBoxSize / 2,
                    halfAlphaPaint);
            //绘制取消区域
            canvas.drawRect(0, gridRows * measuredBoxSize, getWidth(),
                    getHeight(), cancelAreaPaint);
            String text = "拖到此处取消";
            cancelTextPaint.getTextBounds(text, 0, text.length(), textBounds);
            canvas.drawText(text, (getWidth() - textBounds.width()) / 2f,
                    gridRows * measuredBoxSize + defaultBottomBarMargin + textBounds.height() / 2f,
                    cancelTextPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        dropCol = (int) (x / measuredBoxSize);
        dropRow = (int) (y / measuredBoxSize);
        float bottomBarY = gridRows * measuredBoxSize + defaultBottomBarMargin;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 1. 检测是否点击了网格中的方块
                int col = (int) (x / measuredBoxSize);
                int row = (int) (y / measuredBoxSize);

                // 检查是否在网格范围内
                if (row >= 0 && row < gridRows && col >= 0 && col < gridCols) {
                    draggedBoxType = dataLayer.getBasicMap().get(row, col);
                    // 在拖动开始时，将原始位置设为空
                    dataLayer.remove(col,row );
                    invalidate(); // 重绘，隐藏被拖动的方块
                }
                // 2. 检测是否点击了底部的工具栏方块
                else if (y > bottomBarY && y < bottomBarY + measuredBoxSize) {

                    List<BoxType> stateBoxType = Collections.emptyList();
                    switch (stateMode.getCurrentState()) {
                        case PUT_BASIC:
                            stateBoxType = basicBoxType;
                            break;
                        case PUT_TARGET:
                            stateBoxType = targetBoxType;
                            break;
                        case PUT_BOX:
                            stateBoxType = boxBoxType;
                            break;
                        case PUT_ROLE:
                            stateBoxType = roleBoxType;
                            break;
                        default:
                            break;
                    }
                    float roamingWidth = getWidth() - stateBoxType.size() * measuredBoxSize;
                    int toolCol = (int) ((x - roamingWidth / 2) / measuredBoxSize);
                    if (toolCol >= 0 && toolCol < stateBoxType.size()) {
                        // 从工具栏拖出
                        draggedBoxType = stateBoxType.get(toolCol);
                    }
                }

                if (draggedBoxType != null) {
                    // 开始拖动
                    startTouchX = x;
                    startTouchY = y;
                    currentDragX = x;
                    currentDragY = y;
                    return true; // 消费事件
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (draggedBoxType != null) {
                    // 移动拖动阴影
                    currentDragX = x;
                    currentDragY = y;
                    invalidate(); // 重绘，更新拖动阴影位置
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (draggedBoxType != null) {
                    // 放置操作
                    // 检查是否放置到网格的有效位置
                    if (dropRow >= 0 && dropRow < gridRows &&
                            dropCol >= 0 && dropCol < gridCols) {

                        // 放置方块，更新level数组
                        dataLayer.put(draggedBoxType, dropCol, dropRow);
                    }
                    // 如果放置在网格外部（或者您想实现拖回原始位置的逻辑），则可以在这里处理

                    // 重置状态
                    draggedBoxType = null;
                    currentDragX = -1f;
                    currentDragY = -1f;
                    dropCol = -1;
                    dropRow = -1;
                    invalidate(); // 重绘，清除拖动阴影并显示新的方块
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void addRow() {
        gridRows++;                    // 加上这行
        dataLayer.expandRows();
        requestLayout();
        invalidate();
    }

    public void addColumn() {
        gridCols++;                    // 加上这行
        dataLayer.expandColumns();
        requestLayout();
        invalidate();
    }

    public void reduceRow() {
        if (gridRows <= 3) return;     // 建议加个最小限制
        gridRows--;
        dataLayer.shrinkRows();
        requestLayout();
        invalidate();
    }

    public void reduceColumn() {
        if (gridCols <= 3) return;
        gridCols--;
        dataLayer.shrinkColumns();
        requestLayout();
        invalidate();
    }

    public Result<Level> getResult() {
        BoxType[][] array = dataLayer.getBasicMap().toArray(BoxType.class);
        Optional<Location> role = dataLayer.getRole();
        ArrayList<Location> targets = dataLayer.getTargets();
        ArrayList<Location> boxes = dataLayer.getBoxes();
        if (role.isEmpty()) {
            return Result.failure(new IllegalArgumentException("还没有设置主角"));
        }
        if (targets.isEmpty()) {
            return Result.failure(new IllegalArgumentException("还没有设置目标地块"));
        }
        if (boxes.isEmpty()) {
            return Result.failure(new IllegalArgumentException("还没有设置箱子"));
        }
        if (targets.size() != boxes.size()) {
            return Result.failure(new IllegalArgumentException("目标地块和箱子数量不对等"));
        }
        return Result.success(LevelMapper.mapper(array, role.get(), boxes, targets));
    }

    public void nextState() {
        stateMode.transferStatusByEvent(CreateSokobanAction.TRANSFORM).ifPresent(new Consumer<CreateSokobanState>() {
            @Override
            public void accept(CreateSokobanState createSokobanState) {
                dataLayer.setState(createSokobanState);
                invalidate();
                if (listener != null) {
                    listener.onStateChange(createSokobanState);
                }
            }
        });
    }

    public void preState() {
        stateMode.transferStatusByEvent(CreateSokobanAction.BACK).ifPresent(new Consumer<CreateSokobanState>() {
            @Override
            public void accept(CreateSokobanState createSokobanState) {
                dataLayer.setState(createSokobanState);
                invalidate();
                if (listener != null) {
                    listener.onStateChange(createSokobanState);
                }
            }
        });
    }

    // cellSizePx 是当前每个格子的实际像素大小（比如 80px、100px、128px）
    public float spFromCell(float cellSizePx) {
        // 1sp ≈ 1dp，能随系统字体大小设置放大，极度推荐
        return cellSizePx * 0.158f;   // 黄金系数！！！
    }

    public CreateSokobanLevelView setListener(CreateSokobanListener listener) {
        this.listener = listener;
        return this;
    }

    public CreateSokobanState currentState() {
        return stateMode.getCurrentState();
    }
}

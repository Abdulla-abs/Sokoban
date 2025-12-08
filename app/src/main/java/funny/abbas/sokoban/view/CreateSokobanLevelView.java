package funny.abbas.sokoban.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import funny.abbas.sokoban.domain.Box;
import funny.abbas.sokoban.domain.BoxType;
import funny.abbas.sokoban.domain.Level;
import funny.abbas.sokoban.domain.LevelMapper;
import funny.abbas.sokoban.domain.Location;
import funny.abbas.sokoban.domain.MapObject;
import funny.abbas.sokoban.util.Bitmap2DSparseArray;

public class CreateSokobanLevelView extends View {

    private final float standerBoxSize = 80f;
    private float defaultBottomBarMargin = 120f;
    private float measuredBoxSize = standerBoxSize;
    private float targetCircleRadius = (standerBoxSize - standerBoxSize * 0.2f) / 2;

    private BoxType[][] level = new BoxType[6][6];
    //java.lang.ArrayIndexOutOfBoundsException: length=6; index=6
    //at funny.abbas.sokoban.view.CreateSokobanLevelView.onDraw(CreateSokobanLevelView.java:131)
    // 在类成员位置添加
    private int gridRows = 6;    // 网格实际行数（固定）
    private int gridCols = 6;    // 网格实际列数（固定）
    private final Bitmap2DSparseArray targets = new Bitmap2DSparseArray();

    private final Paint defaultPaint = new Paint();
    private final Paint targetCirclePaint = new Paint();


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

    public CreateSokobanLevelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        for (BoxType[] boxTypes : level) {
            Arrays.fill(boxTypes, BoxType.Empty);
        }
        gridRows = level.length;
        gridCols = level[0].length;
        targetCirclePaint.setColor(Color.RED);
        targetCirclePaint.setAntiAlias(true);
        targetCirclePaint.setStyle(Paint.Style.STROKE);
        targetCirclePaint.setStrokeWidth(standerBoxSize * 0.1f);
    }

    //信 宇 人

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        float standardWidth = gridCols * standerBoxSize;
        float standardHeight = gridRows * standerBoxSize + standerBoxSize + defaultBottomBarMargin;

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

        float measuredWidth = gridCols * measuredBoxSize;
        float measureHeight = gridRows * measuredBoxSize + measuredBoxSize + defaultBottomBarMargin;
        targetCircleRadius = (measuredBoxSize - measuredBoxSize * 0.2f) / 2;
        targetCirclePaint.setStrokeWidth(measuredBoxSize * 0.1f);

        setMeasuredDimension((int) Math.ceil(measuredWidth), (int) Math.ceil(measureHeight));
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < gridRows; i++) {
            for (int j = 0; j < gridCols; j++) {
                switch (level[i][j]) {
                    case Role:
                        defaultPaint.setColor(Color.CYAN);
                        break;
                    case Box:
                        defaultPaint.setColor(Color.GREEN);
                        break;
                    case Wall:
                        defaultPaint.setColor(Color.BLACK);
                        break;
                    case Empty:
                        defaultPaint.setColor(Color.WHITE);

                        break;
                }
                canvas.drawRect(j * measuredBoxSize, i * measuredBoxSize,
                        j * measuredBoxSize + measuredBoxSize,
                        i * measuredBoxSize + measuredBoxSize,
                        defaultPaint);

                boolean in = targets.get(i, j);
                if (in) {
                    canvas.drawCircle(j * measuredBoxSize + measuredBoxSize / 2,
                            i * measuredBoxSize + measuredBoxSize / 2, targetCircleRadius,
                            targetCirclePaint);
                }
            }
        }


        //这部分要拖动到上面的创建的格子当中
        BoxType[] boxTypes = BoxType.values();
        float boxWidth = boxTypes.length * measuredBoxSize;
        float margin = (getWidth() - boxWidth) / 2;
        float bottomBarY = gridRows * measuredBoxSize + defaultBottomBarMargin;
        for (int i = 0; i < boxTypes.length; i++) {
            BoxType value = boxTypes[i];
            switch (value) {
                case Role:
                    defaultPaint.setColor(Color.CYAN);
                    break;
                case Box:
                    defaultPaint.setColor(Color.GREEN);
                    break;
                case Wall:
                    defaultPaint.setColor(Color.BLACK);
                    break;
                case Empty:
                    defaultPaint.setColor(Color.WHITE);
                    break;
                case Target:
                    defaultPaint.setColor(Color.RED);
                    break;
            }
            canvas.drawRect(i * measuredBoxSize + margin,
                    bottomBarY,
                    i * measuredBoxSize + measuredBoxSize + margin,
                    bottomBarY + measuredBoxSize,
                    defaultPaint);
        }


        // --- 绘制拖动阴影 ---
        if (draggedBoxType != null && currentDragX != -1f) {

            // 1. 设置被拖动方块的颜色 (可以添加透明度，使其看起来像“阴影”)
            switch (draggedBoxType) {
                case Role:
                    defaultPaint.setColor(Color.CYAN);
                    break;
                case Box:
                    defaultPaint.setColor(Color.GREEN);
                    break;
                case Wall:
                    defaultPaint.setColor(Color.BLACK);
                    break;
                case Empty:
                    defaultPaint.setColor(Color.WHITE);

                    break;
                case Target:
                    defaultPaint.setColor(Color.RED);
                    break;
            }
// 可以通过设置 alpha 值让拖动阴影变透明
            defaultPaint.setAlpha(180); // 180 表示半透明

            // 2. 绘制拖动阴影，以 currentDragX, currentDragY 为中心点或左上角
            // 这里使用 currentDragX/Y 作为左上角绘制
            canvas.drawRect(currentDragX - measuredBoxSize / 2, currentDragY - measuredBoxSize / 2,
                    currentDragX + measuredBoxSize / 2,
                    currentDragY + measuredBoxSize / 2,
                    defaultPaint);

            if (dropCol >= 0 && dropRow >= 0) {
                canvas.drawRect(dropCol * measuredBoxSize, dropRow * measuredBoxSize,
                        dropCol * measuredBoxSize + measuredBoxSize,
                        dropRow * measuredBoxSize + measuredBoxSize,
                        defaultPaint);
            }

            // 3. 恢复画笔的透明度
            defaultPaint.setAlpha(255);
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
                    boolean in = targets.get(row, col);
                    if (in) {
                        targets.set(row, col, false);
                        invalidate();
                    } else {
                        draggedBoxType = level[row][col];
// 在拖动开始时，将原始位置设为空
                        level[row][col] = BoxType.Empty;
                        invalidate(); // 重绘，隐藏被拖动的方块
                    }
                }
                // 2. 检测是否点击了底部的工具栏方块
                else if (y > bottomBarY && y < bottomBarY + measuredBoxSize) {

                    BoxType[] boxTypes = BoxType.values();
                    float roamingWidth = getWidth() - boxTypes.length * measuredBoxSize;
                    int toolCol = (int) ((x - roamingWidth / 2) / measuredBoxSize);
                    if (toolCol >= 0 && toolCol < boxTypes.length) {
                        // 从工具栏拖出
                        draggedBoxType = boxTypes[toolCol];
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
                        if (draggedBoxType == BoxType.Target) {
                            targets.set(dropRow, dropCol, true);
                        } else {
                            level[dropRow][dropCol] = draggedBoxType;
                        }
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
        level = addRow(level);
        gridRows++;                    // 加上这行
        requestLayout();
        invalidate();
    }

    public void addColumn() {
        level = addColumn(level);
        gridCols++;                    // 加上这行
        requestLayout();
        invalidate();
    }

    public void reduceRow() {
        if (gridRows <= 3) return;     // 建议加个最小限制
        level = removeRow(level, level.length - 1);
        gridRows--;
        requestLayout();
        invalidate();
    }

    public void reduceColumn() {
        if (gridCols <= 3) return;
        level = removeColumn(level, level[0].length - 1);
        gridCols--;
        requestLayout();
        invalidate();
    }

    // 新增一行（到末尾）
    private BoxType[][] addRow(BoxType[][] original) {
        int oldRows = original.length;
        int cols = oldRows > 0 ? original[0].length : 0;

        BoxType[][] newArray = new BoxType[oldRows + 1][cols];

        // 复制原数据
        for (int i = 0; i < oldRows; i++) {
            System.arraycopy(original[i], 0, newArray[i], 0, cols);
        }

        // 初始化新行
        newArray[oldRows] = new BoxType[cols];
        Arrays.fill(newArray[oldRows], BoxType.Empty);

        return newArray;
    }

    // 新增一列（到末尾）
    private BoxType[][] addColumn(BoxType[][] original) {
        int rows = original.length;
        int oldCols = rows > 0 ? original[0].length : 0;

        BoxType[][] newArray = new BoxType[rows][oldCols + 1];

        for (int i = 0; i < rows; i++) {
            System.arraycopy(original[i], 0, newArray[i], 0, oldCols);
            // 新列元素默认是null，可以初始化
            newArray[i][oldCols] = BoxType.Empty;
        }

        return newArray;
    }

    // 减少一行（删除指定行，返回新数组）
    private BoxType[][] removeRow(BoxType[][] original, int rowToRemove) {
        if (original == null || rowToRemove < 0 || rowToRemove >= original.length) {
            return original;
        }

        int oldRows = original.length;
        int cols = oldRows > 0 ? original[0].length : 0;

        BoxType[][] newArray = new BoxType[oldRows - 1][cols];

        // 复制 rowToRemove 之前的数据
        for (int i = 0; i < rowToRemove; i++) {
            System.arraycopy(original[i], 0, newArray[i], 0, cols);
        }

        // 复制 rowToRemove 之后的数据
        for (int i = rowToRemove + 1; i < oldRows; i++) {
            System.arraycopy(original[i], 0, newArray[i - 1], 0, cols);
        }

        return newArray;
    }

    // 减少一列（删除指定列，返回新数组）
    private BoxType[][] removeColumn(BoxType[][] original, int colToRemove) {
        if (original == null || original.length == 0) {
            return original;
        }

        int rows = original.length;
        int oldCols = original[0].length;

        if (colToRemove < 0 || colToRemove >= oldCols) {
            return original;
        }

        BoxType[][] newArray = new BoxType[rows][oldCols - 1];

        for (int i = 0; i < rows; i++) {
            // 复制 colToRemove 之前的列
            if (colToRemove > 0) {
                System.arraycopy(original[i], 0, newArray[i], 0, colToRemove);
            }

            // 复制 colToRemove 之后的列
            if (colToRemove < oldCols - 1) {
                System.arraycopy(
                        original[i],
                        colToRemove + 1,
                        newArray[i],
                        colToRemove,
                        oldCols - colToRemove - 1
                );
            }
        }

        return newArray;
    }

    public Level getResult() {
//        List<Pair<Integer, Integer>> allTruePositions = targets.getAllTruePositions();
//        List<Location> target = allTruePositions.stream().map(new Function<Pair<Integer, Integer>, Location>() {
//            @Override
//            public Location apply(Pair<Integer, Integer> integerIntegerPair) {
//                return new Location(integerIntegerPair.second, integerIntegerPair.first);
//            }
//        }).collect(Collectors.toList());
//        return LevelMapper.mapper(level, target);
        return null;
    }

}

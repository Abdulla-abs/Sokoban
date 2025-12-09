package funny.abbas.sokoban.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import funny.abbas.sokoban.core.BoxType;
import funny.abbas.sokoban.core.Level;
import funny.abbas.sokoban.core.Location;
import funny.abbas.sokoban.core.MapObject;
import funny.abbas.sokoban.core.Skin;
import funny.abbas.sokoban.core.Theme;

public class SokobanView extends View implements Action {

    private float measuredBoxSize = 80f;
    private float paddingTop = 0f;
    private float paddingStart = 0f;

    private final Skin skin = Skin.getInstance();
    private final Controller controller = new Controller();
    private volatile boolean inAnimation = false;

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

        //measure不强制改变自身的大小；
        //自身将判断宽高更少的一边而进行最大化显示格子
        //由于是更具最小的边计算大小，所以较大的边将额外计算居中距离，也就是paddingStart和paddingTop
        int boxColumnCount = controller.level.map.length;
        int boxRowCount = controller.level.map[0].length;

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSize < heightSize) {
            measuredBoxSize = (float) widthSize / boxRowCount;
            paddingTop = (heightSize - measuredBoxSize * boxColumnCount) / 2;
        } else {
            measuredBoxSize = (float) heightSize / boxColumnCount;
            paddingStart = (widthSize - measuredBoxSize * boxRowCount) / 2;
        }

        //setMeasuredDimension((int) (measuredBoxSize * boxRowCount), (int) (measuredBoxSize * boxColumnCount));
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if (controller.level == null) return;

        Bitmap immutableMap = drawBasicMap();
        canvas.drawBitmap(immutableMap, 0, 0, null);

        for (MapObject target : controller.level.getTargets()) {
            Bitmap bitmap = skin.loadSkin(BoxType.Target, target.getDirection(), measuredBoxSize, measuredBoxSize);
            canvas.drawBitmap(bitmap, paddingStart + target.getLocation().getX() * measuredBoxSize,
                    paddingTop + target.getLocation().getY() * measuredBoxSize,
                    null);
        }


        for (MapObject box : controller.level.boxes) {
            Bitmap bitmap = skin.loadSkin(BoxType.Box, box.getDirection(), measuredBoxSize, measuredBoxSize);
            if (box.inAnimation) {
                canvas.drawBitmap(bitmap, paddingStart + box.renderX,
                        paddingTop + box.renderY,
                        null);
            } else {
                canvas.drawBitmap(bitmap, paddingStart + box.getLocation().getX() * measuredBoxSize,
                        paddingTop + box.getLocation().getY() * measuredBoxSize,
                        null);
            }

        }

        MapObject role = controller.level.getRole();
        Bitmap bitmap = null;
        if (role.inAnimation) {
            bitmap = skin.loadAnimeSkin(BoxType.Role, role.getDirection(), measuredBoxSize, measuredBoxSize);
            canvas.drawBitmap(bitmap, paddingStart + role.renderX,
                    paddingTop + role.renderY, null);
        } else {
            bitmap = skin.loadSkin(BoxType.Role, role.getDirection(), measuredBoxSize, measuredBoxSize);
            canvas.drawBitmap(bitmap, paddingStart + role.getLocation().getX() * measuredBoxSize,
                    paddingTop + role.getLocation().getY() * measuredBoxSize, null);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        basicMapNeedInvalidate = true;
    }

    private Bitmap basicMap;
    private boolean basicMapNeedInvalidate = false;

    /**
     * 对于基础地图，即不可动的方块部分，仅绘制一次，此后一直复用，以减少绘制压力
     * @return 基础地图
     */
    private Bitmap drawBasicMap() {
        if (basicMap == null || basicMapNeedInvalidate) {
            if (basicMap != null) {//重绘地图情况下，回收上一次的地图内存
                basicMap.recycle();
            }
            basicMap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(basicMap);
            for (int i = 0; i < controller.level.map.length; i++) {
                for (int j = 0; j < controller.level.map[0].length; j++) {
                    MapObject box = controller.level.map[i][j];
                    Bitmap bitmap = skin.loadSkin(box.getBoxType(),box.getDirection(), measuredBoxSize, measuredBoxSize);
                    canvas.drawBitmap(bitmap, paddingStart + box.getLocation().getX() * measuredBoxSize,
                            paddingTop + box.getLocation().getY() * measuredBoxSize, null);
                }
            }
            basicMapNeedInvalidate = false;
        }
        return basicMap;
    }

    public void setStateListener(GameStateListener stateListener) {
        this.controller.stateListener = stateListener;
    }

    public void setLevel(Level level) {
        controller.level = level;
        basicMapNeedInvalidate = true;
        requestLayout();
    }

    public void replaceSkin(Theme theme) {
        skin.setTheme(theme);
        invalidate();
    }

    @Override
    public boolean moveLeft() {
        if (!inAnimation && controller.moveLeft()) {
            startAnimation(Direction.LEFT);
            return true;
        }
        return false;
    }

    @Override
    public boolean moveTop() {
        if (!inAnimation && controller.moveTop()) {
            startAnimation(Direction.TOP);
            return true;
        }
        return false;
    }

    @Override
    public boolean moveRight() {
        if (!inAnimation && controller.moveRight()) {
            startAnimation(Direction.RIGHT);
            return true;
        }
        return false;
    }

    @Override
    public boolean moveBottom() {
        if (!inAnimation && controller.moveBottom()) {
            startAnimation(Direction.BOTTOM);
            return true;
        }
        return false;
    }

    @Override
    public boolean backStep() {
        if (!inAnimation && controller.backStep()) {
            invalidate();
            return true;
        }
        return false;
    }

    private void startAnimation(final Direction direction) {
        inAnimation = true;

        List<Pair<MapObject, Location>> currentStep = controller.level.stepRemember.getCurrentStep();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(150);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                float progress = (float) valueAnimator.getAnimatedValue();
                for (Pair<MapObject, Location> mapObjectLocationPair : currentStep) {
                    render(direction, mapObjectLocationPair, progress);
                }
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                for (Pair<MapObject, Location> mapObjectLocationPair : currentStep) {
                    mapObjectLocationPair.first.inAnimation = true;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                for (Pair<MapObject, Location> mapObjectLocationPair : currentStep) {
                    mapObjectLocationPair.first.inAnimation = false;
                }
                inAnimation = false;
                invalidate();
            }
        });
        valueAnimator.start();
    }

    private void render(Direction direction, Pair<MapObject, Location> mapObjectLocationPair, float animationProgress) {
        switch (direction) {
            case LEFT:
                float leftStartX = mapObjectLocationPair.second.getX() * measuredBoxSize;
                mapObjectLocationPair.first.renderX = leftStartX - measuredBoxSize * animationProgress;
                mapObjectLocationPair.first.renderY = mapObjectLocationPair.second.getY() * measuredBoxSize;
                break;
            case TOP:
                float topStartY = mapObjectLocationPair.second.getY() * measuredBoxSize;
                mapObjectLocationPair.first.renderX = mapObjectLocationPair.second.getX() * measuredBoxSize;
                mapObjectLocationPair.first.renderY = topStartY - measuredBoxSize * animationProgress;
                break;
            case RIGHT:
                float rightStartX = mapObjectLocationPair.second.getX() * measuredBoxSize;
                mapObjectLocationPair.first.renderX = rightStartX + measuredBoxSize * animationProgress;
                mapObjectLocationPair.first.renderY = mapObjectLocationPair.second.getY() * measuredBoxSize;
                break;
            case BOTTOM:
                float bottomStartY = mapObjectLocationPair.second.getY() * measuredBoxSize;
                mapObjectLocationPair.first.renderX = mapObjectLocationPair.second.getX() * measuredBoxSize;
                mapObjectLocationPair.first.renderY = bottomStartY + measuredBoxSize * animationProgress;
                break;
        }
    }
}

package funny.abbas.sokoban.util;

import android.graphics.RectF;

/**
 * @author deepseek
 */
public class CoodinateInRange {
    private float checkX;
    private float checkY;

    /**
     * 设置要检查的坐标点
     * @param x X坐标
     * @param y Y坐标
     */
    public void setCheckXY(float x, float y) {
        this.checkX = x;
        this.checkY = y;
    }

    /**
     * 检查点是否在圆角矩形内
     * @param rect 矩形区域
     * @param rx 圆角在X方向的半径
     * @param ry 圆角在Y方向的半径
     * @return 如果点在圆角矩形内返回true，否则返回false
     */
    public boolean inRangeRectangleCornerRadius(RectF rect, float rx, float ry) {
        if (rect == null) return false;

        float left = rect.left;
        float top = rect.top;
        float right = rect.right;
        float bottom = rect.bottom;

        // 1. 首先检查点是否在矩形的基本边界内（不考虑圆角）
        if (checkX < left || checkX > right || checkY < top || checkY > bottom) {
            return false;
        }

        // 2. 检查点是否在四个圆角区域之外
        // 左上圆角
        if (checkX <= left + rx && checkY <= top + ry) {
            float dx = checkX - (left + rx);
            float dy = checkY - (top + ry);
            return (dx * dx) / (rx * rx) + (dy * dy) / (ry * ry) <= 1;
        }

        // 右上圆角
        if (checkX >= right - rx && checkY <= top + ry) {
            float dx = checkX - (right - rx);
            float dy = checkY - (top + ry);
            return (dx * dx) / (rx * rx) + (dy * dy) / (ry * ry) <= 1;
        }

        // 左下圆角
        if (checkX <= left + rx && checkY >= bottom - ry) {
            float dx = checkX - (left + rx);
            float dy = checkY - (bottom - ry);
            return (dx * dx) / (rx * rx) + (dy * dy) / (ry * ry) <= 1;
        }

        // 右下圆角
        if (checkX >= right - rx && checkY >= bottom - ry) {
            float dx = checkX - (right - rx);
            float dy = checkY - (bottom - ry);
            return (dx * dx) / (rx * rx) + (dy * dy) / (ry * ry) <= 1;
        }

        // 如果不在任何圆角区域，且在矩形内，则返回true
        return true;
    }

    /**
     * 检查点是否在圆形内（假设矩形是正方形，圆形是矩形的内切圆）
     * @param rect 作为圆形边界的矩形（应该是正方形）
     * @return 如果点在圆形内返回true，否则返回false
     */
    public boolean inRangeCircle(RectF rect) {
        if (rect == null) return false;

        // 计算圆心（矩形的中心）
        float centerX = (rect.left + rect.right) / 2;
        float centerY = (rect.top + rect.bottom) / 2;

        // 计算半径（取矩形宽度和高度的最小值的一半）
        float radius = Math.min(rect.width(), rect.height()) / 2;

        // 计算点到圆心的距离
        float dx = checkX - centerX;
        float dy = checkY - centerY;
        float distanceSquared = dx * dx + dy * dy;

        // 检查距离是否小于等于半径的平方
        return distanceSquared <= (radius * radius);
    }

    /**
     * 检查点是否在普通矩形内
     * @param rect 矩形区域
     * @return 如果点在矩形内返回true，否则返回false
     */
    public boolean inRangeRectangle(RectF rect) {
        if (rect == null) return false;
        return rect.contains(checkX, checkY);
    }

    // Getter方法（可选）
    public float getCheckX() {
        return checkX;
    }

    public float getCheckY() {
        return checkY;
    }
}

package ballapp.mtm.eti.pg.ballmtm.model;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Obstacle {
    private int left, top, right, bottom, width, height, centerX, centerY;
    private Rect rect;
    private Paint paint;

    public Obstacle(int left, int top, int right, int bottom) {
        this.width = right - left;
        this.height = bottom - top;
        this.centerX = width / 2 + left;
        this.centerY = height / 2 + top;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        rect = new Rect(left, top, right, bottom);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.CYAN);
    }

    public int getLeft() {
        return left;
    }

    public int getTop() {
        return top;
    }

    public int getRight() {
        return right;
    }

    public int getBottom() {
        return bottom;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public Rect getRect() {
        return rect;
    }

    public Paint getPaint() {
        return paint;
    }
}

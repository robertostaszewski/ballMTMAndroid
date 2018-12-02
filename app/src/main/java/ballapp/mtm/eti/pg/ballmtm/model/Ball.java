package ballapp.mtm.eti.pg.ballmtm.model;

import android.graphics.Color;
import android.graphics.Paint;

public class Ball {
    private float x, y, r, xSpeed, ySpeed;
    private float top, bottom, right, left;
    private Paint paint;

    public Ball(float x, float y, float r) {
        this.r = r;
        setX(x);
        setY(y);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
    }

    public void accelerate(float accelerationX, float accelerationY) {
        setxSpeed(xSpeed - accelerationX);
        setySpeed(ySpeed + accelerationY);
    }

    public void move() {
        setX(x + xSpeed);
        setY(y + ySpeed);
    }

    public void setX(float x) {
        this.x = x;
        right = x + r;
        left = x - r;
    }

    public void setY(float y) {
        this.y = y;
        top = y - r;
        bottom = y + r;
    }

    public void setxSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    public void setySpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getR() {
        return r;
    }

    public float getxSpeed() {
        return xSpeed;
    }

    public float getySpeed() {
        return ySpeed;
    }

    public float getTop() {
        return top;
    }

    public float getBottom() {
        return bottom;
    }

    public float getRight() {
        return right;
    }

    public float getLeft() {
        return left;
    }

    public Paint getPaint() {
        return paint;
    }
}

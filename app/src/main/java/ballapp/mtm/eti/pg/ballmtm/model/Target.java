package ballapp.mtm.eti.pg.ballmtm.model;

import android.graphics.Color;
import android.graphics.Paint;

public class Target {
    private float x;
    private float y;
    private float r;
    private Paint paint;

    public Target(float r) {
        this.r = r;
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.paint.setColor(Color.BLACK);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
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

    public Paint getPaint() {
        return paint;
    }
}

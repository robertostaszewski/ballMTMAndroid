package ballapp.mtm.eti.pg.ballmtm.model;

import android.graphics.Color;
import android.graphics.Paint;

public class PointsText {
    private Paint paint;
    private int x;
    private int y;
    private int value;

    public PointsText(int x, int y) {
        this.x = x;
        this.y = y;
        paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setTextSize(100);
    }

    public Paint getPaint() {
        return paint;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getValue() {
        return value;
    }

    public void incrementPoints() {
        value ++;
    }
}

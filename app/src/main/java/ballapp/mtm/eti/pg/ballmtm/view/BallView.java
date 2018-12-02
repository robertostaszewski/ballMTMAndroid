package ballapp.mtm.eti.pg.ballmtm.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import java.util.List;

import ballapp.mtm.eti.pg.ballmtm.model.Ball;
import ballapp.mtm.eti.pg.ballmtm.model.Obstacle;
import ballapp.mtm.eti.pg.ballmtm.model.PointsText;
import ballapp.mtm.eti.pg.ballmtm.model.Target;

public class BallView extends View {
    private final Ball ball;
    private final Target target;
    private final PointsText pointsText;
    private final List<Obstacle> obstacles;

    public BallView(Context context,
                    Ball ball,
                    Target target,
                    PointsText pointsText,
                    List<Obstacle> obstacles) {
        super(context);
        this.ball = ball;
        this.target = target;
        this.pointsText = pointsText;
        this.obstacles = obstacles;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Obstacle obstacle : obstacles) {
            canvas.drawRect(obstacle.getLeft(), obstacle.getTop(), obstacle.getRight(), obstacle.getBottom(), obstacle.getPaint());
        }
        canvas.drawCircle(target.getX(), target.getY(), target.getR(), target.getPaint());
        canvas.drawCircle(ball.getX(), ball.getY(), ball.getR(), ball.getPaint());
        canvas.drawText(String.valueOf(pointsText.getValue()), pointsText.getX(), pointsText.getY(), pointsText.getPaint());
        invalidate();
    }
}

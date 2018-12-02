package ballapp.mtm.eti.pg.ballmtm.controller;

import ballapp.mtm.eti.pg.ballmtm.model.Ball;
import ballapp.mtm.eti.pg.ballmtm.model.Screen;
import ballapp.mtm.eti.pg.ballmtm.activity.BallActivity;

public class ScreenController implements CollisionDetector {
    private Screen screen;

    public ScreenController(Screen screen) {
        this.screen = screen;
    }

    @Override
    public void detectCollision(Ball ball) {
        if (ball.getRight() > screen.getWidth()) {
            ball.setxSpeed(-ball.getxSpeed() * BallActivity.OBSTACLE_SPEED_LOOS_CONST);
            ball.setX(screen.getWidth() - ball.getR());
        } else if (ball.getLeft() < 0) {
            ball.setxSpeed(-ball.getxSpeed() * BallActivity.OBSTACLE_SPEED_LOOS_CONST);
            ball.setX(ball.getR());
        }

        if (ball.getBottom() > screen.getHeight()) {
            ball.setySpeed(-ball.getySpeed() * BallActivity.OBSTACLE_SPEED_LOOS_CONST);
            ball.setY(screen.getHeight() - ball.getR());
        } else if (ball.getTop() < 0) {
            ball.setySpeed(-ball.getySpeed() * BallActivity.OBSTACLE_SPEED_LOOS_CONST);
            ball.setY(ball.getR());
        }
    }
}

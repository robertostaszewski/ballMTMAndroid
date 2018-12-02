package ballapp.mtm.eti.pg.ballmtm.controller;

import ballapp.mtm.eti.pg.ballmtm.model.Ball;
import ballapp.mtm.eti.pg.ballmtm.model.Obstacle;
import ballapp.mtm.eti.pg.ballmtm.activity.BallActivity;

public class ObstacleController implements CollisionDetector {

    private Obstacle obstacle;

    public ObstacleController(Obstacle obstacle) {
        this.obstacle = obstacle;
    }

    @Override
    public void detectCollision(Ball ball) {
        float xDistance = ball.getX() - obstacle.getCenterX();
        float yDistance = ball.getY() - obstacle.getCenterY();
        float xMaxDistance = ball.getR() + obstacle.getWidth()/2;
        float yMaxDistance = ball.getR()  + obstacle.getHeight()/2;

        if (Math.abs(xDistance) <= xMaxDistance && Math.abs(yDistance) <= yMaxDistance) {
            float wy = xMaxDistance * yDistance;
            float hx = yMaxDistance * xDistance;

            if (wy > hx) {
                if (wy > -hx) {
                    ball.setySpeed(-ball.getySpeed() * BallActivity.OBSTACLE_SPEED_LOOS_CONST);
                    ball.setY(obstacle.getBottom() + ball.getR());
                } else {
                    ball.setxSpeed(-ball.getxSpeed() * BallActivity.OBSTACLE_SPEED_LOOS_CONST);
                    ball.setX(obstacle.getLeft() - ball.getR());
                }
            } else {
                if (wy > -hx) {
                    ball.setxSpeed(-ball.getxSpeed() * BallActivity.OBSTACLE_SPEED_LOOS_CONST);
                    ball.setX(obstacle.getRight() + ball.getR());
                } else {
                    ball.setySpeed(-ball.getySpeed() * BallActivity.OBSTACLE_SPEED_LOOS_CONST);
                    ball.setY(obstacle.getTop() - ball.getR());
                }
            }
        }
    }
}

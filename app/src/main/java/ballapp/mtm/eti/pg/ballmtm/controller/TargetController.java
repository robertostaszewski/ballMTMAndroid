package ballapp.mtm.eti.pg.ballmtm.controller;

import android.graphics.Rect;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import ballapp.mtm.eti.pg.ballmtm.model.Ball;
import ballapp.mtm.eti.pg.ballmtm.model.Obstacle;
import ballapp.mtm.eti.pg.ballmtm.model.PointsText;
import ballapp.mtm.eti.pg.ballmtm.model.Screen;
import ballapp.mtm.eti.pg.ballmtm.model.Target;

public class TargetController implements CollisionDetector {

    private Target target;
    private Screen screen;
    private PointsText pointsText;
    private List<Obstacle> obstacles;

    public TargetController(Target target, Screen screen, PointsText pointsText, List<Obstacle> obstacles) {
        this.target = target;
        this.screen = screen;
        this.pointsText = pointsText;
        this.obstacles = obstacles;
        changeLocation();
    }

    @Override
    public void detectCollision(Ball ball) {
        if (Math.abs(ball.getX() - target.getX()) < target.getR() * 2
                && Math.abs(ball.getY() - target.getY()) < target.getR() * 2) {
            changeLocation();
            pointsText.incrementPoints();
        }
    }

    private void changeLocation() {
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();
        target.setX(localRandom.nextInt((int) target.getR(), (int) (screen.getWidth() - target.getR())));
        target.setY(localRandom.nextInt((int) target.getR(), (int) (screen.getHeight() - target.getR())));
        for (Obstacle obstacle : obstacles) {
            if (new Rect((int) (target.getX() - target.getR()),
                    (int) (target.getY() - target.getR()),
                    (int) (target.getX() + target.getR()),
                    (int) (target.getY() + target.getR())).intersect(obstacle.getRect())) {
                changeLocation();
            }
        }
    }

}

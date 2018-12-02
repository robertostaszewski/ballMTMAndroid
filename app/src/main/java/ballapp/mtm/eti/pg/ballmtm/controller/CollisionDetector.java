package ballapp.mtm.eti.pg.ballmtm.controller;

import ballapp.mtm.eti.pg.ballmtm.model.Ball;

@FunctionalInterface
public interface CollisionDetector {
    void detectCollision(Ball ball);
}

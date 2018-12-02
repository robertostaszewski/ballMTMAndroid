package ballapp.mtm.eti.pg.ballmtm.controller;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.ArrayList;
import java.util.List;

import ballapp.mtm.eti.pg.ballmtm.model.Ball;

public class BallSensorListener implements SensorEventListener {
    private final Ball ball;
    private List<CollisionDetector> watchers = new ArrayList<>();

    public BallSensorListener(Ball ball) {
        this.ball = ball;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float alpha = 0.2f;
            float gravX = 0, gravY = 0;
            gravX = alpha * gravX + (1 - alpha) * event.values[0];
            gravY = alpha * gravY + (1 - alpha) * event.values[1];
            ball.accelerate(event.values[0] - gravX, event.values[1] - gravY);
            ball.move();
            for (CollisionDetector watcher : watchers) {
                watcher.detectCollision(ball);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void addWatcher(CollisionDetector watcher) {
        this.watchers.add(watcher);
    }
}

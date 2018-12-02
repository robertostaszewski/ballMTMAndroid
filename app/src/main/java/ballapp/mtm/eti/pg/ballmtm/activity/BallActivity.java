package ballapp.mtm.eti.pg.ballmtm.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import ballapp.mtm.eti.pg.ballmtm.controller.BallSensorListener;
import ballapp.mtm.eti.pg.ballmtm.controller.ObstacleController;
import ballapp.mtm.eti.pg.ballmtm.controller.ScreenController;
import ballapp.mtm.eti.pg.ballmtm.controller.TargetController;
import ballapp.mtm.eti.pg.ballmtm.model.Ball;
import ballapp.mtm.eti.pg.ballmtm.model.Obstacle;
import ballapp.mtm.eti.pg.ballmtm.model.PointsText;
import ballapp.mtm.eti.pg.ballmtm.model.Screen;
import ballapp.mtm.eti.pg.ballmtm.model.Target;
import ballapp.mtm.eti.pg.ballmtm.view.BallView;

public class BallActivity extends AppCompatActivity {

    public final static float OBSTACLE_SPEED_LOOS_CONST = 0.6f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Screen screen = new Screen(width, height);
        Target target = new Target(40f);
        PointsText pointsText = new PointsText(50, 100);
        Ball ball = new Ball(200, 700, 50f);
        List<Obstacle> obstacles = new ArrayList<>();
        obstacles.add(new Obstacle(400, 150, 800, 350));
        obstacles.add(new Obstacle(300, 500, 650, 620));
        obstacles.add(new Obstacle(600, 1600, 950, 1650));
        obstacles.add(new Obstacle(200, 1000, 300, 1400));
        obstacles.add(new Obstacle(950, 800, 1050, 1300));
        obstacles.add(new Obstacle(400, 1800, 800, 1900));

        BallView ballView = new BallView(this, ball, target, pointsText, obstacles);
        setContentView(ballView);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        assert sensorManager != null;
        BallSensorListener sensorListener = new BallSensorListener(ball);
        sensorManager.registerListener(
                sensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);

        sensorListener.addWatcher(new ScreenController(screen));
        sensorListener.addWatcher(new TargetController(target, screen, pointsText, obstacles));
        for (Obstacle obstacle : obstacles) {
            sensorListener.addWatcher(new ObstacleController(obstacle));
        }
    }

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

}

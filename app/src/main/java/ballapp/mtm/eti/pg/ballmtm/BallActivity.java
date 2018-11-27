package ballapp.mtm.eti.pg.ballmtm;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BallActivity extends AppCompatActivity {

    public final static float OBSTACLE_SPEED_LOOS_CONST = 0.6f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        hideStatusBar();
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
        SensorListener sensorListener = new SensorListener(ball);
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

    private static class Obstacle {
        private int left, top, right, bottom;
        private Rect rect;
        private Paint paint;

        Obstacle(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
            rect = new Rect(left, top, right, bottom);
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.CYAN);
        }
    }

    private static class PointsText {
        private Paint paint;
        private int x;
        private int y;
        private int value = 0;

        PointsText(int x, int y) {
            this.x = x;
            this.y = y;
            paint = new Paint();
            paint.setColor(Color.GRAY);
            paint.setTextSize(100);
        }
    }

    private static class Target {
        private float x;
        private float y;
        private float r;
        private Paint paint;

        Target(float r) {
            this.r = r;
            this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            this.paint.setColor(Color.BLACK);
        }

    }

    private static class Screen {
        private final float width;
        private final float height;

        Screen(int width, int height) {
            this.width = width;
            this.height = height;
        }

    }

    private static class Ball {
        private float x, y, r, xSpeed, ySpeed;
        private float top, bottom, right, left;
        private Paint paint;

        Ball(float x, float y, float r) {
            this.r = r;
            setX(x);
            setY(y);
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.RED);
        }

        void accelerate(float accelerationX, float accelerationY) {
            setxSpeed(xSpeed - accelerationX);
            setySpeed(ySpeed + accelerationY);
        }

        void move() {
            setX(x + xSpeed);
            setY(y + ySpeed);
        }

        void setX(float x) {
            this.x = x;
            right = x + r;
            left = x - r;
        }

        void setY(float y) {
            this.y = y;
            top = y - r;
            bottom = y + r;
        }

        void setxSpeed(float xSpeed) {
            this.xSpeed = xSpeed;
        }

        void setySpeed(float ySpeed) {
            this.ySpeed = ySpeed;
        }
    }

    @FunctionalInterface
    private interface CollisionDetector {
        void detectCollision(Ball ball);
    }

    private static class ObstacleController implements CollisionDetector {

        private Obstacle obstacle;

        ObstacleController(Obstacle obstacle) {
            this.obstacle = obstacle;
        }

        @Override
        public void detectCollision(Ball ball) {
            float obstacleWidth = obstacle.right - obstacle.left;
            float obstacleHight = obstacle.bottom - obstacle.top;
            float obstacleCenterX = obstacleWidth / 2 + obstacle.left;
            float obstacleCenterY = obstacleHight / 2 + obstacle.top;
            float distX = ball.x - obstacleCenterX;
            float distY = ball.y - obstacleCenterY;

            float w = (float) (0.5 * (2 * ball.r + obstacleWidth));
            float h = (float) (0.5 * (2 * ball.r  + obstacleHight));

            Rect p = obstacle.rect;
            if (Math.abs(distX) <= w && Math.abs(distY) <= h) {
                float wy = w * distY;
                float hx = h * distX;

                if (wy > hx) {
                    if (wy > -hx) {
                        ball.setySpeed(-ball.ySpeed * OBSTACLE_SPEED_LOOS_CONST);
                        ball.setY(p.bottom + ball.r);
                        /* collision at the top */
                    } else {
                        ball.setxSpeed(-ball.xSpeed * OBSTACLE_SPEED_LOOS_CONST);
                        ball.setX(p.left - ball.r);
                        /* on the left */
                    }
                } else {
                    if (wy > -hx) {
                        ball.setxSpeed(-ball.xSpeed * OBSTACLE_SPEED_LOOS_CONST);
                        ball.setX(p.right + ball.r);
                        /* on the right */
                    } else {
                        ball.setySpeed(-ball.ySpeed * OBSTACLE_SPEED_LOOS_CONST);
                        ball.setY(p.top - ball.r);
                        /* at the bottom */
                    }
                }

            }

        }
    }

    private static class ScreenController implements CollisionDetector {
        private Screen screen;

        private ScreenController(Screen screen) {
            this.screen = screen;
        }

        @Override
        public void detectCollision(Ball ball) {
            if (ball.right > screen.width) {
                ball.setxSpeed(-ball.xSpeed * OBSTACLE_SPEED_LOOS_CONST);
                ball.setX(screen.width - ball.r);
            } else if (ball.left < 0) {
                ball.setxSpeed(-ball.xSpeed * OBSTACLE_SPEED_LOOS_CONST);
                ball.setX(ball.r);
            }

            if (ball.bottom > screen.height) {
                ball.setySpeed(-ball.ySpeed * OBSTACLE_SPEED_LOOS_CONST);
                ball.setY(screen.height - ball.r);
            } else if (ball.top < 0) {
                ball.setySpeed(-ball.ySpeed * OBSTACLE_SPEED_LOOS_CONST);
                ball.setY(ball.r);
            }
        }
    }

    private static class TargetController implements CollisionDetector {

        private Target target;
        private Screen screen;
        private PointsText pointsText;
        private List<Obstacle> obstacles;

        TargetController(Target target, Screen screen, PointsText pointsText, List<Obstacle> obstacles) {
            this.target = target;
            this.screen = screen;
            this.pointsText = pointsText;
            this.obstacles = obstacles;
            changeLocation();
        }

        @Override
        public void detectCollision(Ball ball) {
            if (Math.abs(ball.x - target.x) < target.r * 2
                    && Math.abs(ball.y - target.y) < target.r * 2) {
                changeLocation();
                pointsText.value++;
            }
        }

        private void changeLocation() {
            ThreadLocalRandom localRandom = ThreadLocalRandom.current();
            target.x = localRandom.nextInt((int) target.r, (int) (screen.width - target.r));
            target.y = localRandom.nextInt((int) target.r, (int) (screen.height - target.r));
            for (Obstacle obstacle : obstacles) {
                if (new Rect((int) (target.x - target.r),
                        (int) (target.y - target.r),
                        (int) (target.x + target.r),
                        (int) (target.y + target.r)).intersect(obstacle.rect)) {
                    changeLocation();
                }
            }
        }

    }

    private static class SensorListener implements SensorEventListener {
        private final Ball ball;
        private List<CollisionDetector> watchers = new ArrayList<>();

        private SensorListener(Ball ball) {
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

        void addWatcher(CollisionDetector watcher) {
            this.watchers.add(watcher);
        }
    }

    private static class BallView extends View {
        private final Ball ball;
        private final Target target;
        private final PointsText pointsText;
        private final List<Obstacle> obstacles;

        public BallView(Context context, Ball ball, Target target, PointsText pointsText, List<Obstacle> obstacles) {
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
                canvas.drawRect(obstacle.left, obstacle.top, obstacle.right, obstacle.bottom, obstacle.paint);
            }
            canvas.drawCircle(target.x, target.y, target.r, target.paint);
            canvas.drawCircle(ball.x, ball.y, ball.r, ball.paint);
            canvas.drawText(String.valueOf(pointsText.value), pointsText.x, pointsText.y, pointsText.paint);
            invalidate();
        }
    }
}

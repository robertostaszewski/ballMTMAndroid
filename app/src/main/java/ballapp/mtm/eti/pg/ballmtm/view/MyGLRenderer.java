package ballapp.mtm.eti.pg.ballmtm.view;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ballapp.mtm.eti.pg.ballmtm.model.Ball;
import ballapp.mtm.eti.pg.ballmtm.model.Obstacle;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private final Ball ball;
    private final List<Obstacle> obstacles;
    private List<ObstacleView> obstacleViews;
    private BallSphere ballSphere;
    private final ResourceReader resourceReader;
    private final float[] mViewMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private int numStrips;
    private Sphere sphere;

    public MyGLRenderer(ResourceReader resourceReader, Ball ball, List<Obstacle> obstacles) {
        this.resourceReader = resourceReader;
        this.ball = ball;
        this.obstacles = obstacles;
        obstacleViews = new ArrayList<>();
        this.numStrips = 40;
    }

    boolean changed = false;

    public void setNumStrips(int delta) {
        if (delta != 0) {
            if (delta + numStrips > 4 && delta + numStrips < 50) {
                numStrips += delta;
                sphere.setNumStrips(numStrips);
                changed = true;
            } else if (delta + numStrips >= 50 && numStrips != 50) {
                numStrips = 50;
                sphere.setNumStrips(numStrips);
                changed = true;
            } else if (delta + numStrips <= 4 && numStrips != 4) {
                numStrips = 4;
                sphere.setNumStrips(numStrips);
                changed = true;
            }
        }

    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0.0f, 1.5f, 0.0f, 0.0f, 0.0f, 0f, 1.0f, 1.0f);

        float[] sphereMoveMatrix = ballSphere.move(mViewMatrix, mProjectionMatrix);
        if (changed) {
            sphere.initBuffers();
            changed = false;
        }
        ballSphere.draw(sphereMoveMatrix);
        for (int i = 0; i < obstacleViews.size(); i++) {
            ObstacleView obstacleView = obstacleViews.get(i);
            Obstacle obstacle = obstacles.get(i);
            float[] mMVPMatrix = new float[16];
            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
            Matrix.translateM(mMVPMatrix, 0, obstacle.getLeft(), obstacle.getTop(), 0f);
            obstacleView.draw(mMVPMatrix);
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        sphere = new Sphere(resourceReader, ball.getR(), numStrips);
        ballSphere = new BallSphere(ball, sphere);
        obstacles.forEach(obstacle ->
                obstacleViews.add(new ObstacleView(resourceReader, obstacle.getWidth(), obstacle.getHeight(), 100f)));
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        final float near = 1f;
        final float far = 1000.0f;

        Matrix.orthoM(mProjectionMatrix, 0, 0, width, height, 0, near, far);
    }
}
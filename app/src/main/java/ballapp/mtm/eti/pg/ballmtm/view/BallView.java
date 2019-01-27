package ballapp.mtm.eti.pg.ballmtm.view;

import android.content.Context;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import ballapp.mtm.eti.pg.ballmtm.model.Ball;
import ballapp.mtm.eti.pg.ballmtm.model.Obstacle;
import ballapp.mtm.eti.pg.ballmtm.model.PointsText;
import ballapp.mtm.eti.pg.ballmtm.model.Target;

public class BallView extends GLSurfaceView {
    private final Ball ball;
    private final Target target;
    private final PointsText pointsText;
    private final List<Obstacle> obstacles;


    private final MyGLRenderer mRenderer;
    private final ResourceReader resourceReader;

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

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        resourceReader = new ResourceReader(getContext());
        mRenderer = new MyGLRenderer(resourceReader, ball, obstacles);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
    }
    private float mPreviousY;
    private final float TOUCH_SCALE_FACTOR = 45.0f / 320;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dy = y - mPreviousY;

                mRenderer.setNumStrips((int) (dy*TOUCH_SCALE_FACTOR));
                break;
        }
        mPreviousY = y;
        return true;
    }
}

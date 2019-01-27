package ballapp.mtm.eti.pg.ballmtm.view;

import android.opengl.Matrix;

import ballapp.mtm.eti.pg.ballmtm.model.Ball;

public class BallSphere {
    private final Ball ball;
    private final Sphere sphere;
    private final float[] mMVPMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    public BallSphere(Ball ball, Sphere sphere) {
        this.ball = ball;
        this.sphere = sphere;
    }

    public float[] move(float[] mViewMatrix, float[] mProjectionMatrix) {
        float[] scratchX = new float[16];
        float[] scratchY = new float[16];
        float[] scratchRR = new float[16];

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        Matrix.translateM(mMVPMatrix, 0, ball.getX(), ball.getY(), 0f);
        Matrix.setRotateM(scratchX, 0, ball.getX(), 0.0f, -1, 0f);
        Matrix.setRotateM(scratchY, 0, ball.getY(), -1, 0.0f, 0);
        Matrix.multiplyMM(mRotationMatrix, 0, scratchX, 0, scratchY, 0);
        Matrix.multiplyMM(scratchRR, 0, mMVPMatrix, 0, mRotationMatrix, 0);
        return scratchRR;
    }

    public void draw(float[] mvpMatrix) {
        sphere.draw(mvpMatrix);
    }
}

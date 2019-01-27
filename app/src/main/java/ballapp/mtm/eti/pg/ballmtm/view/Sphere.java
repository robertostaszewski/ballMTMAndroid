package ballapp.mtm.eti.pg.ballmtm.view;

import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import ballapp.mtm.eti.pg.ballmtm.R;

public class Sphere {
    private FloatBuffer vertexBuffer;
    private FloatBuffer texBuffer;
    static private FloatBuffer sphereNormal;

    // Use to access and set the view transformation
    private int mMVPMatrixHandle;
    int mPoints;
    protected int crateTextureDataHandle;
    private int numStrips;
    private double radius;
    private final int mProgram;
    static final int COORDS_PER_VERTEX = 3;


    private int diffuseTextureHandle;

    public Sphere(ResourceReader resourceReader, double radius, int numStrips) {
        this.numStrips = numStrips;
        this.radius = radius;
        initBuffers();

        crateTextureDataHandle = resourceReader.readTexture(R.drawable.a4_no_ice_clouds_mts_8k);

        int vertexShader = ShaderLoader.loadShader(GLES20.GL_VERTEX_SHADER,
                resourceReader.readShaderFile(R.raw.vertex_shader));
        int fragmentShader = ShaderLoader.loadShader(GLES20.GL_FRAGMENT_SHADER,
                resourceReader.readShaderFile(R.raw.fragment_shader));

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);

        diffuseTextureHandle = GLES20.glGetUniformLocation(mProgram, "diffuseTexture");
    }

    public void setNumStrips(int numStrips) {
        this.numStrips = numStrips;
    }

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, crateTextureDataHandle);
        GLES20.glUniform1i(diffuseTextureHandle, 0);
//
        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        int texPositionHandle = GLES20.glGetAttribLocation(mProgram, "givenTexPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(texPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glVertexAttribPointer(texPositionHandle, 2,
                GLES20.GL_FLOAT, false, 0, texBuffer);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

//        int vertexCount = vertexBuffer.le.length / COORDS_PER_VERTEX;
        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, mPoints);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(texPositionHandle);
    }

    public void initBuffers() {
        mPoints = 0;
        ByteBuffer bb = ByteBuffer.allocateDirect(6*4*numStrips*(1+numStrips));
        bb.order(ByteOrder.nativeOrder());
        ByteBuffer tbb = ByteBuffer.allocateDirect(4*4*numStrips*(1+numStrips));
        tbb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        texBuffer = tbb.asFloatBuffer();

        double e2 = 0.0067; //Geoid's squared eccentricity, derived from flattening factor

        float texY1 = 0f;
        double lat1 = (Math.PI * 0) / numStrips - 0.5 * Math.PI;
        double s1 = Math.sin(lat1);
        double c1 = Math.cos(lat1);
        double chi1 = Math.sqrt(1.0 - e2 * s1 * s1);
        double xy1 = radius / chi1 * c1;
        double z1 = radius * (1.0 - e2) / chi1 * s1;

        int count = 0;

        Log.d("-------num", "" + numStrips);
//* Draw latitude quad strips: *
        for (int i = 1; i <= numStrips; ++i) {
            float texY0 = texY1;
            double xy0 = xy1;
            double z0 = z1;
            texY1 = (float) i / (float) numStrips;
            lat1 = (Math.PI * i) / numStrips - 0.5 * Math.PI;
            s1 = Math.sin(lat1);
            c1 = Math.cos(lat1);
            chi1 = Math.sqrt(1.0 - e2 * s1 * s1);
            xy1 = radius / chi1 * c1;
            z1 = radius * (1.0 - e2) / chi1 * s1;

            for (int j = 0; j <= numStrips; ++j) {
                float texX = (float) j / (float) numStrips;
                double lng = (2.0 * Math.PI * j) / numStrips;
                double cl = Math.cos(lng);
                double sl = Math.sin(lng);

                texBuffer.put(texX);
                texBuffer.put(texY1);
                vertexBuffer.put((float) (xy1 * cl));
                vertexBuffer.put((float) (xy1 * sl));
                vertexBuffer.put((float) (z1));
//                glNormal3d(c1*cl,c1*sl,s1);


                texBuffer.put(texX);
                texBuffer.put(texY0);
                vertexBuffer.put((float) (xy0 * cl));
                vertexBuffer.put((float) (xy0 * sl));
                vertexBuffer.put((float) (z0));
                mPoints += 2;
                count++;
//                glNormal3d(c0*cl,c0*sl,s0);
            }
        }

        Log.d("-------------wyszlo", ""+count);
        vertexBuffer.position(0);
        texBuffer.position(0);
    }
}

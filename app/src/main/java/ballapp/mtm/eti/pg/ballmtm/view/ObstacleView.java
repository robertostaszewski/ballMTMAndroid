package ballapp.mtm.eti.pg.ballmtm.view;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import ballapp.mtm.eti.pg.ballmtm.R;

public class ObstacleView {
    private FloatBuffer vertexBuffer;
    private FloatBuffer texBuffer;

    final float[] texCoordData = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };

    private float[] positionData;
    protected int crateTextureDataHandle;
    private final int mProgram;
    private int diffuseTextureHandle;

    public ObstacleView(ResourceReader resourceReader, float width, float height, float z) {
        positionData = new float[]{
                0.0f, height, z,
                0.0f, 0.0f, z,
                width, height, z,
                0.0f, 0.0f, z,
                width, 0.0f, z,
                width, height, z,

                width, height, z,
                width, 0.0f, z,
                width, height, -z,
                width, 0.0f, z,
                width, 0.0f, -z,
                width, height, -z,

                width, height, -z,
                width, 0.0f, -z,
                0.0f, height, -z,
                width, 0.0f, -z,
                0.0f, 0.0f, -z,
                0.0f, height, -z,

                0.0f, height, -z,
                0.0f, 0.0f, -z,
                0.0f, height, z,
                0.0f, 0.0f, -z,
                0.0f, 0.0f, z,
                0.0f, height, z,

                width, height, -z,
                width, height, z,
                0.0f, height, -z,
                width, height, z,
                0.0f, height, z,
                0.0f, height, -z,

                width, 0.0f, -z,
                width, 0.0f, z,
                0.0f, 0.0f, -z,
                width, 0.0f, z,
                0.0f, 0.0f, z,
                0.0f, 0.0f, -z,
        };

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                positionData.length * 4);
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoordData.length*4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());
        tbb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        texBuffer = tbb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(positionData);
        texBuffer.put(texCoordData);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
        texBuffer.position(0);

        crateTextureDataHandle = resourceReader.readTexture(R.drawable.crate_borysses_deviantart_com);

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
    static final int COORDS_PER_VERTEX = 3;
    private int mMVPMatrixHandle;

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
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);
        GLES20.glVertexAttribPointer(texPositionHandle, 2, GLES20.GL_FLOAT, false, 0, texBuffer);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

//        int vertexCount = vertexBuffer.le.length / COORDS_PER_VERTEX;
        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, positionData.length/COORDS_PER_VERTEX);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(texPositionHandle);
    }
}

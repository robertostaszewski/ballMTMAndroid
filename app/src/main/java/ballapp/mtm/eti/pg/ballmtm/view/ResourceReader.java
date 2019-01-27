package ballapp.mtm.eti.pg.ballmtm.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceReader {
    private final Context appContext;

    public ResourceReader(Context appContext) {
        this.appContext = appContext;
    }

    // Metoda wczytująca teksturę z katalogu drawable.
    public int readTexture(int resourceId) {
        Log.d("KSG", "Wczytywanie tekstury.");
        final int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0); // Wygenerowanie tekstury i pobranie jej adresu.

        if (textureHandle[0] == 0) {
            Log.e("KSG", "Błąd przy wczytywaniu tekstury.");
            return -1;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false; // Wyłączenie skalowania.

        if (appContext == null) {
            Log.e("KSG", "appContext = null");
        } else {
            final Bitmap bitmap = BitmapFactory.decodeResource(appContext.getResources(), resourceId, options);
            Log.d("KSG", " bitmap resolution: " + bitmap.getWidth() + " x " + bitmap.getHeight());
            // Podpięcie tekstury.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            // Ustawienie rodzaju filtrowania tekstury.
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

            // Wczytanie zawartości bitmapy do tekstury.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            // Zwolnienie pamięci zajmowanej przez zmienną bitmap.
            bitmap.recycle();
        }

        return textureHandle[0];
    }

    public String readShaderFile(int resourceId)
    {
        if (appContext == null)
        {
            Log.e("KSG", "readShaderFile: appContext == null");
            return null;
        }

        InputStream inputStream = appContext.getResources().openRawResource(resourceId);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = "";
        StringBuilder sb = new StringBuilder();

        try
        {
            while ((line = bufferedReader.readLine()) != null)
            {
                sb.append(line);
                sb.append('\n');
                //Log.d("KSG", line);
            }
        }
        catch (Exception e)
        {
            Log.e("KSG", "Błąd przy wczytywaniu pliku: " + e.getMessage());
            return null;
        }

        return sb.toString();
    }
}

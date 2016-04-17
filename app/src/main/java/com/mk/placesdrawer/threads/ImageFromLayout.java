package com.mk.placesdrawer.threads;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.mk.placesdrawer.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by max on 06.04.16.
 */
public class ImageFromLayout extends AsyncTask<String, Integer, String> {

    private File openPath;
    private Context context;
    private String location;
    private Bitmap bitmap;
    private String TAG = "ImageFromLayout";

    public ImageFromLayout(Context context, String location, Bitmap bitmap) {
        this.context = context;
        this.location = location;
        this.bitmap = bitmap;
    }

    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        FileOutputStream output = null;

        String imageName = location + ",page.jpeg";
        File path = new File(Environment.getExternalStorageDirectory().toString());
        File myDir = new File(path, context.getResources().getString(R.string.app_name));

        if (!myDir.exists()) {
            myDir.mkdir();
            Log.d(TAG, "New Places folder created: ");

        } else
            Log.d(TAG, "Places folder does exist. Image will be downloaded in this folder. ");

        openPath = myDir;

        File file = new File(myDir, imageName);

        try {

            output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                Log.d(TAG, "Image was successfully downloaded.");

                if (input != null)
                    input.close();

            } catch (IOException ignored) {
            }

        }

        return null;
    }

    public File getOpenPath() {
        return openPath;
    }

}


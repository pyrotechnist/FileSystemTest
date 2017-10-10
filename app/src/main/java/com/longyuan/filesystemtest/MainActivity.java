package com.longyuan.filesystemtest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;


public class MainActivity extends Activity {



    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textFromFile);
    }


    public void loadFromInternal(View v){

        String FILENAME = "hello_file";

        StringBuilder contentBuilder = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(FILENAME))))
        {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
            {
                contentBuilder.append(sCurrentLine).append("\n");
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        mTextView.setText(contentBuilder.toString());

    }


    public void saveToInternal(View v){

        FileOutputStream fos = null;
        try {
            String FILENAME = "hello_file";
            String string = "hello world!";

            // solution 1
            // from android official doc
            // path to /data/data/yourapp/app_data/files
            // openFileOutput  is specifically used for file writing into internal storage and disallow writing to external storage
            //fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);



            // solution  2
            // path to /data/data/yourapp/app_data/files
            // FileOutputStream allows you to write to both internal and external storage
            fos = new FileOutputStream(new File(getFilesDir(), FILENAME));


            fos.write(string.getBytes());
            fos.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return;
    }



    public void saveToExternal(View v ){

        if (!isExternalStorageWritable())
        {
            Toast.makeText(this,"External storage is not writable",Toast.LENGTH_LONG ).show();
        }


        String FILENAME = "hello_file";

        String string = "hello world!";

        // public path
        // path to /storage/sdcard/Documents/files/yourpath
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                        + "/GalleryTest");

        // private file path
        // path to /storage/sdcard/Android/data/com.longyuan.filesystemtest/files
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, FILENAME);
            //savedImagePath = imageFile.getAbsolutePath();
            try {

                // path to /storage/sdcard/DIRECTORY_DOCUMENTS/yourapp/
                OutputStream fOut = new FileOutputStream(imageFile);

                fOut.write(string.getBytes());

                //image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);


                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            //galleryAddPic(savedImagePath);
            //Toast.makeText(mContext, "IMAGE SAVED"), Toast.LENGTH_LONG).show();
        }
    }

    public void loadFromExternal(View v){

        String FILENAME = "hello_file";

        String string = "hello world!";

        // public path
        // path to /storage/sdcard/Documents/files/yourpath
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                        + "/GalleryTest");

        if (!storageDir.exists()) {
            Toast.makeText(this, "Cannot finfd  folder" + storageDir.getAbsolutePath(), Toast.LENGTH_LONG).show();
            return;
        }

        File imageFile = new File(storageDir, FILENAME);

        StringBuilder contentBuilder = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(imageFile))))
        {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
            {
                contentBuilder.append(sCurrentLine).append("\n");
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        mTextView.setText(contentBuilder.toString());
    }


    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {

            return false;
        }

        return  true;

        //Toast.makeText(this,"External storage found",Toast.LENGTH_LONG ).show();
    }
}

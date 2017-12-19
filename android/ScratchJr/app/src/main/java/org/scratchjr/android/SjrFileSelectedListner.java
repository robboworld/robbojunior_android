package org.scratchjr.android;

import android.app.Activity;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yarila on 14.08.17.
 */

public class SjrFileSelectedListner implements FileChooser.FileSelectedListener {

    private final ScratchJrActivity activity;

    SjrFileSelectedListner(ScratchJrActivity activity){

       this.activity = activity;

    }

    @Override
    public void fileSelected(File file)  {

        String result;

            try {
                FileInputStream in = new FileInputStream(file);
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int len;
                    byte[] buffer = new byte[1024];
                    while ((len = in.read(buffer)) != -1) {
                        bos.write(buffer, 0, len);
                    }
                    bos.close();
                    byte[] data = bos.toByteArray();
                    result = Base64.encodeToString(data, Base64.NO_WRAP);

                    this.activity.runJavaScript( "UI.SjrRead_Android('" + result + "');");

                } catch (FileNotFoundException e) {


                } catch (IOException e) {


                } finally {
                    in.close();
                }
            } catch(FileNotFoundException e){



            }
            catch (IOException e) {


            }





        }
}

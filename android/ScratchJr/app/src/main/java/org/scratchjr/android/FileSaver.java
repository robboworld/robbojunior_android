package org.scratchjr.android;

/**
 * Created by yarila on 15.08.17.
 */

import android.app.Activity;
import android.app.Dialog;
import android.os.Environment;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Space;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class FileSaver{

    private static final String PARENT_DIR = "..";

    private final Activity activity;
    private ListView save_list;
    private Dialog save_dialog;


    private File currentPath;

    // filter on file extension
    private String extension = null;

    private final String TAG = "[FileChooser]";

    public void setExtension(String extension){
        this.extension = (extension == null) ? null : extension.toLowerCase();
    }

    // file selection event handling


    public FileSaver(Activity activity){

        Log.i(TAG,"constructor");

        this.activity = activity;


    }



    public void Save_File_Dialog( final String filename, final String base64ContentStr){


        Log.i(TAG,"Save_File_Dialog()");

        save_dialog = new Dialog(activity);

        save_list = new ListView(activity);

        save_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int which, long id){
                String fileChosen = (String) save_list.getItemAtPosition(which);
                File chosenFile = getChosenFile(fileChosen);
                if(chosenFile.isDirectory()){
                    refresh(chosenFile);
                }

            }
        });


        LinearLayout ll = new LinearLayout(activity);
        ll.setOrientation(LinearLayout.VERTICAL);

        LayoutParams save_list_layout_params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,3);

        LinearLayout save_list_layout = new LinearLayout(activity);
       // save_list_layout.addView(save_list,LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        LinearLayout save_button_layout = new LinearLayout(activity);

        Button save_btn = new Button(activity);
        save_btn.setText("Save");

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FileSaver.this.SaveFile(filename,  base64ContentStr);

                save_dialog.dismiss();

            }
        });





       // save_button_layout.addView(save_btn,save_button_layout_params);


        DisplayMetrics display_metrics = new DisplayMetrics();



        activity.getWindowManager().getDefaultDisplay().getMetrics(display_metrics);

        int save_list_height = (int)(display_metrics.heightPixels * 0.6);

        int save_btn_height = (int)(display_metrics.heightPixels * 0.2);

        ll.addView(save_list,LayoutParams.MATCH_PARENT,save_list_height);

        LayoutParams save_button_layout_params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);

        // save_btn.setGravity(Gravity.CENTER);




         save_button_layout.setGravity(Gravity.CENTER_VERTICAL);

        save_button_layout.setLayoutParams(save_button_layout_params);

        save_button_layout.addView(save_btn,LayoutParams.MATCH_PARENT, save_btn_height);


        Space _space = new Space(activity);


        ll.addView(_space,LayoutParams.MATCH_PARENT, (int)(save_btn_height * 0.1));

        ll.addView(save_button_layout);

        save_dialog.setContentView(ll);
        save_dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        String state = Environment.getExternalStorageState();
      /*  if (Environment.MEDIA_MOUNTED.equals(state)) {
            refresh(Environment.getExternalStorageDirectory());
        }else{*/

         refresh(activity.getFilesDir());
        // }




    }

    public void SaveFile(String filename, String base64ContentStr){


        File sjr_file = new File(currentPath, filename);

        byte[] content = Base64.decode(base64ContentStr, Base64.NO_WRAP);


        try {
            FileOutputStream out = new FileOutputStream(sjr_file);

            try {

                out.write(content);


            } catch (FileNotFoundException e) {


            } catch (IOException e) {


            } finally {
                out.close();
            }
        } catch(FileNotFoundException e){



        }
        catch (IOException e) {


        }




    }

    public void showDialog(){

        Log.i(TAG,"showDialog()");

        save_dialog.show();
    }

    /**
     * Sort, filter and display the files for the given path.
     */
    private void refresh(File path){
        this.currentPath = path;
        if(path.exists()){
            File[] dirs = path.listFiles(new FileFilter(){
                @Override
                public boolean accept(File file){
                    return (file.isDirectory() && file.canRead());
                }
            });
            File[] files = path.listFiles(new FileFilter(){
                @Override
                public boolean accept(File file){
                    if(!file.isDirectory()){
                        if(!file.canRead()){
                            return false;
                        }
                        else if(extension == null){
                            return true;
                        }
                        else{
                            return file.getName().toLowerCase().endsWith(extension);
                        }
                    }
                    else{
                        return false;
                    }
                }
            });

// convert to an array
            int i = 0;
            String[] fileList;
            if(path.getParentFile() == null){
                fileList = new String[dirs.length + files.length];
            }
            else{
                fileList = new String[dirs.length + files.length + 1];
                fileList[i++] = PARENT_DIR;
            }
            Arrays.sort(dirs);
            Arrays.sort(files);
            for(File dir : dirs){
                fileList[i++] = dir.getName();
            }
            for(File file : files){
                fileList[i++] = file.getName();
            }

// refresh the user interface
            save_dialog.setTitle(currentPath.getPath());
            save_list.setAdapter(new ArrayAdapter(activity, android.R.layout.simple_list_item_1, fileList){
                @Override
                public View getView(int pos, View view, ViewGroup parent){
                    view = super.getView(pos, view, parent);
                    ((TextView) view).setSingleLine(true);
                    return view;
                }
            });
        }
    }

    /**
     * Convert a relative filename into an actual File object.
     */
    private File getChosenFile(String fileChosen){
        if(fileChosen.equals(PARENT_DIR)){
            return currentPath.getParentFile();
        }
        else{
            return new File(currentPath, fileChosen);
        }
    }
}

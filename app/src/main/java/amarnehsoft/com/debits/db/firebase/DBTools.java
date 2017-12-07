package amarnehsoft.com.debits.db.firebase;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import amarnehsoft.com.debits.db.sqlite.DBHelper;
import amarnehsoft.com.debits.interfaces.ITiger;
import amarnehsoft.com.debits.utils.Alerts;
import amarnehsoft.com.debits.utils.DateUtils;

/**
 * Created by alaam on 10/2/2017.
 */

public class DBTools {
    public static final int MODE_SILENCE = 0;
    public static final int MODE_PUBLIC = 1;
    private int mMODE;
    private String mUID;
    private DatabaseReference mDatabaseReference;
    private static boolean busy = false;

    private  ProgressDialog progress;
    private  String outFileNameDB = Environment.getExternalStorageDirectory()+"/database.db";
    private Context mContext;
    private ITiger<Boolean> mTriger = null;
    public DBTools(Context context,String uid,int MODE) {
        mUID = uid;
        this.mMODE = MODE;
        mContext = context;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(mUID);
    }
    public DBTools(Context context, String uid, int MODE, ITiger<Boolean> triger) {
        this(context,uid,MODE);
        mTriger = triger;
    }


    @SuppressWarnings("VisibleForTests")
    public  void ExportDB(){
        if(busy)
            return;

        busy = true;
        if(mMODE == MODE_PUBLIC) {
            progress = new ProgressDialog(mContext);
            progress.setTitle("Exporting..");
            progress.show();
        }
        Uri file = Uri.fromFile(mContext.getDatabasePath(DBHelper.DATABASE_NAME));
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        String key = "database";
        String location = mUID + "/dbs/" + key + ".db";
        StorageReference riversRef = storageRef.child(location);

         riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Log.d("Amarneh","Exported!");

                        Map<String,Object> map = new HashMap<>();
                        map.put("version", DBHelper.VERSION+"");
                        map.put("id",taskSnapshot.getMetadata().getName());
                        map.put("date", ServerValue.TIMESTAMP);


                        AddFireDB(map);
                        UpdateMainDB(map);

                        if(progress != null)
                            progress.dismiss();

                        if(mMODE == MODE_PUBLIC)
                            Alerts.MyAlert(mContext,"Done","Exporting succeed").show();

                        MakeTiger(true);
                        busy = false;
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
                        editor.putString("export_key","Last exported: " + DateUtils.formatDate(new Date()));
                        editor.apply();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        if(progress != null)
                            progress.dismiss();
                        busy  = false;
                        MakeTiger(false);
                    }
                });

    }

    private void MakeTiger(boolean b) {
        if(mTriger != null){
            mTriger.triger(b);
        }
    }

    private  void AddFireDB(Map<String,Object> map){
        DatabaseReference reference = mDatabaseReference.child("dbs");
        DatabaseReference dbRef = reference.push();
        dbRef.setValue(map);
    }
    private  void UpdateMainDB(Map<String,Object> map) {
        DatabaseReference reference = mDatabaseReference.child("db");
        reference.setValue(map);
    }
    public  void ImportDB(){
        if(busy)
            return;
        busy = true;
        if(mMODE == MODE_PUBLIC) {
            progress = new ProgressDialog(mContext);
            progress.setTitle("Importing..");
            progress.show();
        }
        DatabaseReference reference = mDatabaseReference.child("db").child("id");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    String id = dataSnapshot.getValue().toString();
                    Log.d("Amarneh","id: " + id);
                    DownloadDB(id);
                }catch (Exception e){
                    if(progress != null)
                        progress.dismiss();
                    busy = false;
                    e.printStackTrace();
                    MakeTiger(false);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(progress != null)
                    progress.dismiss();
                busy = false;
                MakeTiger(false);
            }
        });
    }
    private  void DownloadDB(String id){
        File localFile = new File(outFileNameDB);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(mUID+"/dbs/"+id);
        storageReference.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("Amarneh","Download done!");
                        try {
                        MoveDB();
                        }catch (Exception e){e.printStackTrace();
                            if(progress != null)
                                progress.dismiss();
                            busy = false;
                            MakeTiger(false);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if(progress != null)
                    progress.dismiss();
                busy = false;
                MakeTiger(false);
            }
        });
    }
    public void Copy(Context context){
        Log.d("Amarneh","Copy");
//        final String inFileName = "/data/data/<amarnehsoft.com.debits>/databases/foo.db";
        File dbFile = context.getDatabasePath("debits.db");

        try{
            FileInputStream fis = new FileInputStream(dbFile);

            String outFileName = Environment.getExternalStorageDirectory()+"/database_copy.db";

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer))>0){
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();
        }catch (Exception e){
            Log.d("Amarneh","Error ...................................");
            e.printStackTrace();}


    }
    private  void MoveDB() throws IOException {
        copy(new File(outFileNameDB),mContext.getDatabasePath(DBHelper.DATABASE_NAME));
    }
    private  void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst,false);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.flush();
                if(mMODE == MODE_PUBLIC)
                Alerts.MyAlert(mContext,"Done","Importing Succeed!").show();
                MakeTiger(true);
            } finally {
                out.close();
            }
        } finally {
            if(progress != null)
                progress.dismiss();
            busy = false;
            in.close();
            MakeTiger(false);
        }
    }
}

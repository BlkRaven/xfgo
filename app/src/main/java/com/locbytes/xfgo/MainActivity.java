package com.locbytes.xfgo;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

        editText=(EditText)findViewById(R.id.serverAddress);
        Button button=(Button)findViewById(R.id.submitButton);

        init();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String res = FileUtil.saveFileToSDcard("serverAddress",editText.getText().toString());
                if(res.equals("true")){
                    Toast.makeText(getApplicationContext(),"应用成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"应用失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {
        File dir = getExternalFilesDir(null);
        if (!dir.exists()){
            dir.mkdir();
        }
        String serverAddress = FileUtil.getFileDataFromSdcard("serverAddress");
        editText.setText(serverAddress);
    }

    // 安卓6.0及以上动态申请权限
    public static void verifyStoragePermissions(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

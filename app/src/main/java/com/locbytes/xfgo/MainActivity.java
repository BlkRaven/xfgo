package com.locbytes.xfgo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.security.KeyChain;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };

    private EditText serverAddressEdit;
    private EditText uHpEdit;
    private EditText uAtkEdit;
    private EditText eHpEdit;
    private EditText eAtkEdit;

    private Switch tdLvSwitch;
    private Switch skillLvSwitch;
    private Switch battleCancelSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

        init();

        Button submitButton=(Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String serverAddress = serverAddressEdit.getText().toString();
                final JSONObject newOptions = new JSONObject();
                try {
                    newOptions.put("serverAddress",serverAddress);
                    newOptions.put("uHp",uHpEdit.getText().toString());
                    newOptions.put("uAtk",uAtkEdit.getText().toString());
                    newOptions.put("eHp",eHpEdit.getText().toString());
                    newOptions.put("eAtk",eAtkEdit.getText().toString());
                    newOptions.put("tdLv",bool2string(tdLvSwitch.isChecked()));
                    newOptions.put("skillLv",bool2string(skillLvSwitch.isChecked()));
                    newOptions.put("battleCancel",bool2string(battleCancelSwitch.isChecked()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String res = FileUtil.saveFileToSDcard("options",newOptions.toString());
                HttpUtil.post(serverAddress, newOptions.toString());;

                if(res.equals("true")){
                    Toast.makeText(getApplicationContext(),"应用成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"应用失败",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button getRootCAButton=(Button)findViewById(R.id.getRootCAButton);
        getRootCAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String certContext = HttpUtil.get(serverAddressEdit.getText()+"?getRootCA");
                        try {
                            installCert(certContext);
                        } catch (CertificateException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }

    private void init() {

        File dir = getExternalFilesDir(null);
        if (!dir.exists()){
            dir.mkdir();
        }

        serverAddressEdit=(EditText)findViewById(R.id.serverAddressEdit);
        uHpEdit=(EditText)findViewById(R.id.uHpEdit);
        uAtkEdit=(EditText)findViewById(R.id.uAtkEdit);
        eHpEdit=(EditText)findViewById(R.id.eHpEdit);
        eAtkEdit=(EditText)findViewById(R.id.eAtkEdit);

        tdLvSwitch=(Switch)findViewById(R.id.tdlvSwitch);
        skillLvSwitch=(Switch)findViewById(R.id.skilllvSwitch);
        battleCancelSwitch=(Switch)findViewById(R.id.battleCancelSwitch);

        String oldOptionsStr = FileUtil.getFileDataFromSdcard("options");
        if(oldOptionsStr!=null){
            try {
                JSONObject oldOptions = new JSONObject(oldOptionsStr);
                serverAddressEdit.setText(oldOptions.getString("serverAddress"));
                uHpEdit.setText(oldOptions.getString("uHp"));
                uAtkEdit.setText(oldOptions.getString("uAtk"));
                eHpEdit.setText(oldOptions.getString("eHp"));
                eAtkEdit.setText(oldOptions.getString("eAtk"));
                tdLvSwitch.setChecked(string2bool(oldOptions.getString("tdLv")));
                skillLvSwitch.setChecked(string2bool(oldOptions.getString("skillLv")));
                battleCancelSwitch.setChecked(string2bool(oldOptions.getString("battleCancel")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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

    private void installCert(String certContext) throws CertificateException {
        byte [] cert = certContext.getBytes();
        X509Certificate x509 = X509Certificate.getInstance(cert);
        Intent intent = KeyChain.createInstallIntent();
        intent.putExtra(KeyChain.EXTRA_CERTIFICATE, x509.getEncoded());
        intent.putExtra(KeyChain.EXTRA_NAME, "xfgo");
        startActivity(intent);
    }

    private String bool2string(boolean bool){
        if(bool){
            return "true";
        }else{
            return "false";
        }
    }

    private Boolean string2bool(String string){
        return string.equals("true");
    }

}
package io.github.jianloong.calllogtocal;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Jian on 10/05/2016.
 */
public class PermissionActivity extends Activity implements EasyPermissions.PermissionCallbacks {

    private Button grantPermissionButton;
    private Button denyPermissionButton;
    private final int REQUEST_PERMISSIONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_activity);

        grantPermissionButton = (Button) findViewById(R.id.grantPermission);
        denyPermissionButton = (Button) findViewById(R.id.denyPermission);

        if(checkPermissionReadCallLog() && checkPermissionReadSms() && checkPermissionReadContact()){
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        }

        grantPermissionClicked();
        denyPermissionClicked();
    }

    private boolean checkPermissionReadContact(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        return true;
    }

    private boolean checkPermissionReadSms(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        return true;
    }

    private boolean checkPermissionReadCallLog(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        return true;
    }

    private void grantPermissionClicked(){

        grantPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActionBar();
            }
        });
    }

    private void denyPermissionClicked(){

        denyPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });
    }

    @AfterPermissionGranted(REQUEST_PERMISSIONS)
    private void setActionBar(){

        String[] perms = {Manifest.permission.READ_SMS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_CALL_LOG};

        if (EasyPermissions.hasPermissions(
                this, perms
        )) {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs access to your logs.",
                    REQUEST_PERMISSIONS,
                    perms
            );
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        System.exit(0);
    }
}

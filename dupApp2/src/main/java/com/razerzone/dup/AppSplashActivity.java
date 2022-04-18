package com.razerzone.dup;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRadioButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.facebook.common.util.UriUtil;

import com.google.android.material.textfield.TextInputEditText;
import com.razerzone.android.ui.UiCore;
import com.razerzone.android.ui.base.IntentFactory;
import com.razerzone.android.ui.base.UiConfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class AppSplashActivity extends Activity {


    AppCompatRadioButton bgmp4, bgSlides;
    AppCompatCheckBox setLanding, guestUsage, isDarkTheme, goInDirectly, multipleAccount;
    AppCompatRadioButton nativeLogin, webviewLogin;
    private CheckBox parallax;
    private TextInputEditText trasitiontime;
    private Button launch;
    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            switch (buttonView.getId()) {
                case R.id.bgmp4:
                    bgSlides.setChecked(!isChecked);
                    break;
                case R.id.bgSlides:
                    bgmp4.setChecked(!isChecked);
                    break;
            }
            parallax.setEnabled(bgmp4.isChecked());
            trasitiontime.setEnabled(bgSlides.isChecked());

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.app_splash);
        parallax = findViewById(R.id.parallax);
        bgmp4 = findViewById(R.id.bgmp4);
        bgSlides = findViewById(R.id.bgSlides);
        setLanding = findViewById(R.id.setLanding);
        goInDirectly = findViewById(R.id.goInDirectly);
        guestUsage = findViewById(R.id.guestUsage);
        nativeLogin = findViewById(R.id.radioNative);

        webviewLogin = findViewById(R.id.radioWebview);


        guestUsage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == false) {
                    goInDirectly.setChecked(false);
                    goInDirectly.setEnabled(false);
                } else {
                    goInDirectly.setEnabled(true);
                }
            }
        });
        isDarkTheme = findViewById(R.id.isDarkTheme);

        bgmp4.setOnCheckedChangeListener(checkedChangeListener);
        bgSlides.setOnCheckedChangeListener(checkedChangeListener);
        trasitiontime = findViewById(R.id.trasitiontime);
        trasitiontime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    trasitiontime.setText("1000");
                }
            }
        });
        launch = findViewById(R.id.launch);
        launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMobilekit(0);
            }
        });
        multipleAccount = findViewById(R.id.multipleAccount);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("mobileKit", "activity" + this.getLocalClassName() + "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("mobileKit", "activity" + this.getLocalClassName() + "onPause");
    }

    @Override
    public void finish() {
        overridePendingTransition(0, 0);
        super.finish();
    }


    private void gotoMobilekit(int mode) {

        long transition = 1000;
        try {
            transition = Long.parseLong(trasitiontime.getText().toString());
        } catch (Exception e) {
            trasitiontime.setText("1000");
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(null);
        }

        UiConfig.Builder builder = new UiConfig.Builder(this)
                .setAppInfo(R.string.cux_app_name, R.mipmap.ic_launcher);
        builder.setDarkTheme(true);
        builder.setSplashInfo(R.drawable.razer_logo2, 1000);

        //AssetFileDescriptor afd = getApplicationContext().getResources().openRawResourceFd(R.raw.nature2);
        File f = new File(getCacheDir(), "bg" + "video");
        try {
            if (f.exists() == false) {
                BufferedInputStream VideoReader = new BufferedInputStream(getResources().openRawResource(R.raw.nature2));
                BufferedOutputStream bufEcrivain = new BufferedOutputStream((new FileOutputStream(f)));
                byte[] buff = new byte[32 * 1024];
                int len;
                while ((len = VideoReader.read(buff)) > 0) {
                    bufEcrivain.write(buff, 0, len);
                }
                bufEcrivain.flush();
                bufEcrivain.close();
            }

        } catch (IOException e) {
            f.delete();
        }

        String localUrivideo = Uri.fromFile(f).toString(); //"android.resource://" + getPackageName() + "/raw/" + "gow"; //use file name instead of id.
        if (bgmp4.isChecked()) {
            //builder.setBgVideoRawResource(R.raw.nature2,"1.0",parallax.isChecked());
            builder.setBgVideoRawResource(localUrivideo, parallax.isChecked());
        } else {
            if (bgSlides.isChecked()) {
                String fromLocal = UriUtil.getUriForResourceId(R.drawable.s_3).toString();
                String fromLocal2 = UriUtil.getUriForResourceId(R.drawable.s_4).toString();
                String fromLocal3 = UriUtil.getUriForResourceId(R.drawable.s_5).toString();

                String fromWeb
                        = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/10/Nuclear_Dawn_-_Clocktower_FPS_02.png/800px-Nuclear_Dawn_"
                        + "-_Clocktower_FPS_02.png";
                String fromWeb2
                        = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/Nuclear_Dawn_-_Downtown_FPS_01.png/800px-Nuclear_Dawn_-_Downtown_FPS_01.png";
                builder.setBgSlides(new String[]{fromLocal, fromLocal2, fromLocal3,
                        fromWeb, fromWeb2}, transition);
            }

        }

        builder.setWebViewLogin(webviewLogin.isChecked());
        builder.setEnableMultipleAccount(multipleAccount.isChecked());
        builder.setEnableGuest(guestUsage.isChecked());
        builder.setGoInDirectlyWhenGuest(goInDirectly.isChecked());
        builder.setBclearTaskAfterLogin(false);
        builder.setDarkTheme(isDarkTheme.isChecked());
        if (setLanding.isChecked()) {
            builder.setLandingPage(new Intent(this, MainActivity.class));
        }

        UiConfig confi = builder.build();

        UiCore.setConfig(this, confi);
        Intent intent = IntentFactory.createAuth(this);
        startActivityForResult(intent,2);
        //finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

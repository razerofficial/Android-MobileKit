package com.razerzone.dup;

import android.accounts.Account;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.hardware.fingerprint.FingerprintManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatTextView;

import android.widget.Toast;


import com.razerzone.android.auth.certificate.CertAuthenticationModel;
import com.razerzone.android.auth.model.ModelCache;
import com.razerzone.android.auth.model.RazerRemoteConfiguration;
import com.razerzone.android.auth.model.SynapseAuthenticationModel;
import com.razerzone.android.core.IRazerUser;
import com.razerzone.android.core.UnauthorizedException;
import com.razerzone.android.core.UsageException;
import com.razerzone.android.core.WSException;
import com.razerzone.android.core.cop.Token;
import com.razerzone.android.ui.activity.ActivityForceUpdateV2;
import com.razerzone.android.ui.activity.ForgotPasswordActivity;
import com.razerzone.android.ui.activity.WebLogin;
import com.razerzone.android.ui.activity.base.BaseActivityNavBar;
import com.razerzone.android.ui.base.IntentFactory;
import com.razerzone.android.ui.dialogs.DeclineAgreement;
import com.razerzone.android.ui.finger.FingerprintAuthenticationDialogFragment;
import com.razerzone.android.ui.fragment.FragmentGuestProfileUpgrade;
import com.razerzone.android.ui.fragment.FragmentVerifyEmailDialog;
import com.razerzone.android.ui.presenter.Presenter;
import com.razerzone.android.ui.utils.FingerKeystoreHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivityNavBar implements DeclineAgreement.DeclineDialogListener, FragmentGuestProfileUpgrade.GuestUpgradeInterface {
    private static final int REQUEST_CODE_APP_INITIATED_UPGRADE = 72;
    private static final int REQUEST_CODE_PROFILE_INITATED_UPGRADE = 3;
    private static final int REQUEST_CODE_OTP_CODE = 10;
    @BindView(R.id.tv_login)
    AppCompatTextView loginTextView;
    @BindView(R.id.btn_prof_info)
    Button ssoButton;
    @BindView(R.id.btn_about_flow)
    Button aboutButton;
    @BindView(R.id.btn_logout)
    Button logoutButton;
    @BindView(R.id.token)
    AppCompatTextView token;
    @BindView(R.id.verify)
    Button verify;
    SynapseAuthenticationModel mAuthModel;
    SharedPreferences prefs;
    @BindView(R.id.featuresCount)
    EditText featuresCount;
    @BindView(R.id.passDelete)
    EditText passDelete;
    @BindView(R.id.scrollParent)
    View parent;

    @BindView(R.id.btn_app_migrate)
    Button btnAppMigrate;

    @Override
    public Resources.Theme getTheme() {
        return super.getTheme();
    }

    public MainActivity() {
        //IntentFactory.setAppInfo(R.string.flavored_app_name, R.drawable.splash_icon, BuildConfig.DEBUG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        parent.requestFocus();
        mAuthModel = ModelCache.getInstance(this).getAuthenticationModel();
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        //disableSearch();

            try {
                FingerKeystoreHelper.createInstance(this);
                if (FingerKeystoreHelper.getinstance().checkForFinger(this) && false) {
                    FingerprintAuthenticationDialogFragment diag = new FingerprintAuthenticationDialogFragment();
                    diag.setFingerListener(new FingerprintAuthenticationDialogFragment.FingerListener() {
                        @Override
                        public void onAuthenticated(FingerprintManager.AuthenticationResult result) {
                            Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(MainActivity.this, "errror", Toast.LENGTH_SHORT).show();
                        }
                    });
                    diag.show(getFragmentManager(), "fin");
                }
            }
            catch (Exception e){
                //no fingerprint exception
            }


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (CertAuthenticationModel.getInstance().isLoggedIn() == false && preferences.getBoolean("first_time2", false) == false) {
            new FragmentGuestProfileUpgrade().show(getSupportFragmentManager(), "");
            preferences.edit().putBoolean("first_time2", true).commit();
        }

        new AsyncTask<String,String,String>(){

            @Override
            protected String doInBackground(String... strings) {
                try {
                    String value = CertAuthenticationModel.getInstance().getLoggedInJWTToken(true);
                    String deviceid =  CertAuthenticationModel.getInstance().getHashedDeviceId();
                    HashMap<String,String> map = new HashMap<>();
                    map.put("yo","bro");
                    String withEndorsmentjwt = CertAuthenticationModel.getInstance().getLoggedInJWTToken(true,map);
                    System.out.println("");

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (UsageException e) {
                    e.printStackTrace();
                } catch (UnauthorizedException e) {
                    e.printStackTrace();
                } catch (WSException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
/*
        try {
            loginTextView.setText(CertAuthenticationModel.getInstance().getCachedLoggedInUserData().getFirstPrimaryEmail().Login);
        } catch (NotLoggedInException e) {
            e.printStackTrace();
        }
        logoutButton.setEnabled(CertAuthenticationModel.getInstance().isCertLoggedIn());

        StringBuffer buffer = new StringBuffer();

        buffer.append("cert Details:\n");
        if (CertAuthenticationModel.getInstance().isCertLoggedIn()) {
            buffer.append(CertAuthenticationModel.getInstance().signedInCertTificateInformation());

        }
        buffer.append("authenticationMethod:" + CertAuthenticationModel.getInstance().getAuthenticationMethod().name());
        token.setText(buffer.toString());
        */


    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void onLoggedOut() {
        // Toast.makeText(this, "You have been logged out - returning to start screen.", Toast.LENGTH_SHORT).show();
        startActivity(IntentFactory.createStartIntent(this));
        finish();
    }

    @Override
    public void onProfileClicked() {
        if (CertAuthenticationModel.getInstance().hasAuthenticatedCert()) {
            super.onProfileClicked();
        } else {
            startActivityForResult(IntentFactory.CreateProfileNavIntent(this), REQUEST_CODE_PROFILE_INITATED_UPGRADE);

            //startActivityForResult(IntentFactory.createAuthGuestSigninSignUpIntent(this), REQUEST_CODE_PROFILE_INITATED_UPGRADE);//without going through profile or upgrade screen
        }


    }

    @OnClick(R.id.upgradeScreen)
    public void upgrade() {
        startActivityForResult(IntentFactory.createUpgradeScreen(this), REQUEST_CODE_PROFILE_INITATED_UPGRADE);
    }

    @OnClick(R.id.tos)
    public void tosUpgrade() {
        startActivityForResult(IntentFactory.creatTermsOfServiceIntent(this, "https://support.razer.com/theme-store-user-tos"), 33);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_APP_INITIATED_UPGRADE || requestCode == REQUEST_CODE_PROFILE_INITATED_UPGRADE) {
            if (resultCode == RESULT_OK) {
                String uuid = "";

                if (data != null) {
                    uuid = data.getStringExtra("uuid");
                }

                Toast.makeText(this, "User has successfully authenticated with uuid." + uuid + "jwt" + data.getStringExtra("jwt"), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "User Cancelled the upgrade", Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == REQUEST_CODE_OTP_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this, "otp" + data.getStringExtra("otp"), Toast.LENGTH_LONG).show();

        } else {

            Toast.makeText(this, "result:" + resultCode + " ,requestCode:" + requestCode, Toast.LENGTH_SHORT).show();
        }


    }

    @OnClick(R.id.marketing)
    public void marketing() {
        startActivity(IntentFactory.createContactPermission(this));
    }

    @Override
    protected void onAccountRemoved(Account removedAccount) {
        Account currentAccount = mAuthModel.getOwnAccount();
        if (currentAccount != null) {
            if (currentAccount.name.equals(removedAccount.name)) {
                //Toast.makeText(this, "Account removed, Same account", Toast.LENGTH_SHORT).show();
            } else {
                baseGotoSsoScreen();
            }
        } else {
            onLoggedOut();
        }
    }

    @Override
    protected void onSSOLogin(Account ssoAccount) {
        super.onSSOLogin(ssoAccount);

        //Toast.makeText(this, "Razer Account - SSO Login: " + ssoAccount.name, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.getOtp)
    public void getotp() {
        startActivityForResult(IntentFactory.createOtpScreen(this), REQUEST_CODE_OTP_CODE);
    }

    @OnClick(R.id.webLoginPoc)
    public void webloginPoc() {
        startActivity(new Intent(this, WebLogin.class));

    }

    @OnClick(R.id.emailVerifyDialog)
    public void showEmailVerify() {
        if (featuresCount.getText().toString().equals("") || featuresCount.getText().toString().equals("0")) {
            Toast.makeText(this, "please set the number of features to generate", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            if (ModelCache.getInstance(MainActivity.this).getAuthenticationModel().getCachedUserData().GetPrimaryEmail().Verified) {
                String thisMail = ModelCache.getInstance(MainActivity.this).getAuthenticationModel().getCachedUserData().GetPrimaryEmail().Login;
                Toast.makeText(this, thisMail + " is already verified", Toast.LENGTH_SHORT).show();
                return;
            }

            int size = Integer.parseInt(featuresCount.getText().toString());
            FragmentVerifyEmailDialog.FeatureItem[] items = new FragmentVerifyEmailDialog.FeatureItem[size];
            Random random = new Random();

            for (int ctr = 0; ctr < size; ctr++) {
                items[ctr] = new FragmentVerifyEmailDialog.FeatureItem("Title" + random.nextInt(100),
                        "description " + random.nextInt(1000) + " " + random.nextLong() + " " + random.nextLong() + " " + random.nextLong());
            }
            String email = ModelCache.getInstance(MainActivity.this).getAuthenticationModel().getCachedUserData().GetPrimaryEmail().Login;
            FragmentVerifyEmailDialog dialog = FragmentVerifyEmailDialog.createInstance(items, email,
                    new FragmentVerifyEmailDialog.OnConfirmListener() {
                        @Override
                        public void onDismiss() {
                            Toast.makeText(MainActivity.this, "dialog dismissed", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onOkay(final DialogFragment dialogFragment) {
                            dialogFragment.dismiss();
                            Toast.makeText(MainActivity.this, "okay pressed", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onEmailVerified() {
                            Toast.makeText(MainActivity.this, "Email verified", Toast.LENGTH_SHORT).show();
                        }
                    });
            dialog.show(getFragmentManager(), "verify api");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Not Logged In", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.getRemoteConfig)
    public void onRemoteConfig() {
        RazerRemoteConfiguration razerRemoteConfiguration = new RazerRemoteConfiguration(this);
        razerRemoteConfiguration.asyncFetch(new RazerRemoteConfiguration.OnResultListener() {
            @Override
            public void onResult(final RazerRemoteConfiguration.Result result) {
                if (result.mException != null) {
                    Toast.makeText(MainActivity.this, result.mException.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, result.getResultMap().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnClick(R.id.finger)
    void fingeClick() {

        if (FingerKeystoreHelper.getinstance().checkForFinger(this) == false) {
            Toast.makeText(this, "No enrolled finger or hardware not supported", Toast.LENGTH_SHORT).show();
            return;
        }

        FingerprintAuthenticationDialogFragment fingerprintAuthenticationDialogFragment = new FingerprintAuthenticationDialogFragment();
        fingerprintAuthenticationDialogFragment.setFingerListener(new FingerprintAuthenticationDialogFragment.FingerListener() {
            @Override
            public void onAuthenticated(FingerprintManager.AuthenticationResult result) {
                Toast.makeText(MainActivity.this, "authenticated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(MainActivity.this, "errrrorr", Toast.LENGTH_SHORT).show();
            }
        });
        fingerprintAuthenticationDialogFragment.show(getFragmentManager(), "werwerwer");
    }


    @OnClick(R.id.verify)
    void verify() {

        if (ModelCache.getInstance(MainActivity.this).getAuthenticationModel().getCachedUserData() != null && ModelCache.getInstance(MainActivity.this)
                .getAuthenticationModel().getCachedUserData().GetPrimaryEmail().Verified) {
            String thisMail = ModelCache.getInstance(MainActivity.this).getAuthenticationModel().getCachedUserData().GetPrimaryEmail().Login;
            Toast.makeText(this, thisMail + " is already  verified", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(IntentFactory.CreateVerifyIntent(this, null, true, null,
                ModelCache.getInstance(MainActivity.this).getAuthenticationModel().getCachedUserData().GetPrimaryEmail().Login, null));
    }

    @OnClick(R.id.manualRefreshCop)
    void manulRefreshCop() {
        new AsyncTask<String, String, Object>() {
            @Override
            protected Object doInBackground(final String... strings) {
                try {
                    return "Cop:" + ModelCache.getInstance(MainActivity.this).getAuthenticationModel().refreshAndGetCopSessionToken();
                } catch (Exception e) {
                    e.printStackTrace();
                    return e;
                }
            }

            @Override
            protected void onPostExecute(final Object o) {
                super.onPostExecute(o);
                if (o instanceof Exception) {
                    Toast.makeText(MainActivity.this, "problem  with refreshing", Toast.LENGTH_LONG).show();
                } else {
                    token.setText((CharSequence) o);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @OnClick(R.id.manualRefresh)
    void manualRefresh() {
        new AsyncTask<String, String, Object>() {
            @Override
            protected Object doInBackground(final String... strings) {
                try {
                    ModelCache.getInstance(MainActivity.this).getAuthenticationModel().refreshAndGetAccessToken();
                    IRazerUser user = ModelCache.getInstance(MainActivity.this).getAuthenticationModel().getRazerUser();
                    Token token = user.GetOAuthToken();
                    String jwt = CertAuthenticationModel.getInstance().getLoggedInJWTToken();
                    return "jwt:" + jwt + "\n"
                            + "ACCESS:" + token.accessToken + "\n" + "REFRESH:" + token.refreshToken + "\nCOP:" + user
                            .GetToken();
                } catch (Exception e) {
                    e.printStackTrace();
                    return e;
                }
            }

            @Override
            protected void onPostExecute(final Object o) {
                super.onPostExecute(o);
                if (o instanceof Exception) {
                    Toast.makeText(MainActivity.this, "problem  with refreshing", Toast.LENGTH_LONG).show();
                } else {
                    token.setText((CharSequence) o);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @OnClick(R.id.btn_prof_info)
    void onSSOLogin() {
        startActivity(IntentFactory.CreateProfileNavIntent(this));
    }

    @OnClick(R.id.btn_about_flow)
    void onAbout() {
        Intent landingPageIntent = new Intent(this, MainActivity.class);
        landingPageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(IntentFactory.CreateAboutIntent(this, landingPageIntent, null, "Version 1.0",
                "the quick brown fox jumped over the lazy dog near the bank of the river. the quick brown fox jumped over the lazy dog near the bank of the "
                        + "river. ",
                false, true, getString(R.string.app_license)));
    }

    @OnClick(R.id.btn_forgot_pssd)
    void onForgotPassword() {
        Intent landingPageIntent = new Intent(this, ForgotPasswordActivity.class);
        landingPageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(IntentFactory.CreateForgotPasswordIntent(this, landingPageIntent));
    }

    @OnClick(R.id.btn_logout)
    void onLogout() {/*
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Sign out");
        builder.setMessage(R.string.cux_dialog_logout_message);
        builder.setNegativeButton(R.string.account_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Remove all accounts", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                new AsyncTask<String, String, Boolean>() {
                    @Override
                    protected Boolean doInBackground(final String... strings) {
                        mAuthModel.logOut();
                        CertAuthenticationModel.getInstance().logout();
                        return true;
                    }

                    @Override
                    protected void onPostExecute(final Boolean aBoolean) {
                        super.onPostExecute(aBoolean);
                        onLoggedOut();
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        builder.setNeutralButton("logout from app", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                new AsyncTask<String, String, Boolean>() {
                    @Override
                    protected Boolean doInBackground(final String... strings) {
                        mAuthModel.setAllAccountsToInactive();
                        return true;
                    }

                    @Override
                    protected void onPostExecute(final Boolean aBoolean) {
                        super.onPostExecute(aBoolean);
                        //onLoggedOut();
                        Intent landingPage = new Intent(MainActivity.this, MainActivity.class);

                        startActivity(IntentFactory.createSwitchScreen(MainActivity.this, landingPage));
                        finish();
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        builder.show();
        */
    }

    private void updateLabels() {


    }

    @Override
    public void dimiss() {

    }

    @Override
    public void decline() {

    }

    @Override
    public void onUpgrade() {
        startActivityForResult(IntentFactory.createAuthGuestSigninSignUpIntent(this), REQUEST_CODE_APP_INITIATED_UPGRADE);
    }

    @Override
    public void onDimiss() {

    }


    @OnClick(R.id.btn_app_migrate)
    public void appMigrate() {
        startActivity(new Intent(this, ActivityForceUpdateV2.class));
    }

}

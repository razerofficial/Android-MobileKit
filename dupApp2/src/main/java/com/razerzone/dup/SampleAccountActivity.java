package com.razerzone.dup;

import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;


import com.razerzone.android.auth.model.SynapseAuthenticationModel;
import com.razerzone.android.core.LoginData;
import com.razerzone.android.core.UserDataV7;
import com.razerzone.android.ui.activity.account.AccountActivity;
import com.razerzone.android.ui.activity.account.AccountItemEditableListener;
import com.razerzone.android.ui.activity.account.databinding.AccountItem;
import com.razerzone.android.ui.activity.account.databinding.AccountItemEditable;
import com.razerzone.android.ui.activity.account.databinding.AccountItemSpacer;
import com.razerzone.android.ui.activity.account.databinding.AccountItemSubtitle;
import com.razerzone.android.ui.base.IntentFactory;
import com.razerzone.android.ui.fragment.FragmentConfirmDialog;
import com.razerzone.android.ui.loaders.RazerIDNicknameLoader;
import com.razerzone.android.ui.utils.UiUtils;

import java.text.DateFormat;


public class SampleAccountActivity extends AccountActivity {

    private static final String TAG = "SampleAccountActivity";

    private static final int LOADER_ID_NICKNAME = 700;

    private static final int LOADER_BIRTHDATE = 701;

    private static final int LOADER_GENDER = 702;

    private static final int LOADER_SET_USERDATA = 703;

    private static final String KEY_NICKNAME = "nickname";

    private static final String KEY_RAZER_ID = "razerId";

    private static final String KEY_BIRTHDATE = "birthdate";

    private static final String KEY_GENDER = "gender";

    private static final int ID_RAZERID = 0;

    private static final int ID_EMAIL = 123;

    private static final int ID_EMAIL2 = 125;

    private static final int ID_PASSWORD = 124;
    private static final int ID_GOTO_RAZER_ID = 126;
    //private static final int ID_ABOUT = 5;

    private final int WHAT_NO_NETWORK = 0;

    private final int WHAT_ERROR = 1;

    private UserDataV7 mUserDataToSet = null;

    private String currentRazerID = "";

    private Handler feedBackHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:

                    UiUtils.createNoNetworkSnackbarAndShow(SampleAccountActivity.this, findViewById(android.R.id.content));
                    break;
                case 1:
                    new FragmentConfirmDialog.Builder(SampleAccountActivity.this).setTitle(getString(R.string.account_error))
                            .setMessageGravity(Gravity.LEFT)
                            .setMessage((String) msg.obj).build().show(getFragmentManager(), "werewr");

                    break;
            }
        }
    };

    @Override
    protected void onLoggedOut() {
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoaderManager.initLoader(LOADER_ID_NICKNAME, null, this);
    }
    @Override
    public boolean requiresProfilePubsub() {
        return true;
    }

    @Override
    public boolean requiresRazerIdWebCookieInjection() {
        return true;
    }
    @Override
    protected void onCreateItems() {
        addItem(new AccountItemEditable(this,getString(R.string.account_razerid), "",  ID_RAZERID, new AccountItemEditableListener<String>() {
            @Override
            public void onEditRequested(int itemPosition) {
                editString(getString(R.string.account_razerid), "", "", currentRazerID, itemPosition);
            }

            @Override
            public void onEditSave(String newValue) {
                Log.i(TAG, newValue);
                Bundle args = new Bundle();
                args.putString(KEY_RAZER_ID, newValue);
                mLoaderManager.restartLoader(LOADER_ID_NICKNAME, args, SampleAccountActivity.this);
            }
        }));

        AccountItemSubtitle sub = new AccountItemSubtitle(SampleAccountActivity.this, R.string.cux_edit_your_account, R.string.cux_access_your_account_on_razer_id,
                ID_GOTO_RAZER_ID,  new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                openRazerId();
            }
        });
        addItem(sub);
        sub.setElevation(2f);

        addItem(new AccountItemSpacer(SampleAccountActivity.this,R.dimen.account_item_spacer_bottom));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AccountItemSubtitle sub = new AccountItemSubtitle(SampleAccountActivity.this,R.string.one_sec_later, R.string.subtext,
                        65435,  new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        Toast.makeText(SampleAccountActivity.this, "nothing", Toast.LENGTH_SHORT).show();
                    }
                });

                addItemAt(1, sub);
            }
        }, 1000);

        
    }

    protected void onUserDataLoaded(UserDataV7 userData) {

        AccountItem razerIdItem = getItemById(ID_RAZERID);

        currentRazerID = userData.GetRazerId();
        razerIdItem.setSubText(SpannableString.valueOf(currentRazerID));

        AccountItem emailItem = getItemById(ID_EMAIL);
        if (emailItem != null) {
            StringBuffer emailBuffer1 = new StringBuffer();

            String unverifiedString = getString(R.string.unverified);
            if (userData.GetEmailLoginCount() > 0) {

                LoginData loginData1 = userData.getFirstPrimaryEmail();
                LoginData loginData2 = userData.getSecondPrimaryEmail();

                emailBuffer1.append(loginData1.Login + " " + (loginData1.Verified == false ? "\n" + unverifiedString : ""));

                String firstSub = unverifiedString.substring(0, 2);
                String lastSub = unverifiedString.substring(unverifiedString.length() - 2, unverifiedString.length());
                int startIndex = emailBuffer1.indexOf(firstSub);
                int endIndex = emailBuffer1.indexOf(lastSub) + 2;

                int lastindex = emailBuffer1.lastIndexOf(firstSub);
                SpannableString emailSpan = new SpannableString(emailBuffer1);

                if (startIndex > -1) {
                    emailSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_harvard_crimson)), startIndex, endIndex, 0);
                    if (startIndex != lastindex) {
                        //two unverified email
                        int lastlast = emailBuffer1.lastIndexOf(lastSub) + 2;
                        emailSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_harvard_crimson)), lastindex, lastlast, 0);
                    }
                }
                emailItem.setSubText(emailSpan);
                findOrAddEmail2(loginData2);
            } else {
                removeItemById(ID_EMAIL2);
                removeItemById(ID_EMAIL);
            }

        }

        String genderString = userData.GetGender().toString();
        genderString = genderString.substring(0, 1).toUpperCase() + genderString.substring(1);
        // genderItem.subText.set(genderString);

        String birthString = getString(R.string.account_not_set);
        if (userData.GetBirthdate() != null) {
            birthString = DateFormat.getDateInstance(DateFormat.MEDIUM).format(userData.GetBirthdate());
        }
        // birthItem.subText.set(birthString);
        //aboutItem.mainText.set(userData.HasAboutMe() ? userData.GetAboutMe() : getString(R.string.account_about_me_hint));
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID_NICKNAME:
                String nickname = null;
                String razerId = null;
                if (args != null) {
                    nickname = args.getString(KEY_NICKNAME);
                    razerId = args.getString(KEY_RAZER_ID);
                }
                return new RazerIDNicknameLoader(this, razerId, nickname);

        }

        return super.onCreateLoader(id, args);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case LOADER_ID_NICKNAME:
                processLoaderStatus(data);
                break;
        }

        super.onLoadFinished(loader, data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        super.onLoaderReset(loader);
    }

    @Override
    protected void onEmailUpdated(final String newEmail) {

        //startActivity(IntentFactory.CreateVerifyIntent(this, null, false, null, newEmail, null));
        startActivity(IntentFactory.createConfirmEmailScreenIntent(this));
    }


    private void findOrAddEmail2(LoginData loginData2) {

        if (loginData2 == null) {
            removeItemById(ID_EMAIL2);
            return;
        }
        String unverifiedString = getString(R.string.unverified);
        AccountItem emailItem2 = getItemById(ID_EMAIL2);

        if (emailItem2 == null) {
            emailItem2 = new AccountItemEditable(this,getString(R.string.cux_v7_new_email), "",  ID_EMAIL2,
                    new AccountItemEditableListener<String>() {
                        @Override
                        public void onEditRequested(int itemPosition) {

                            launchEmailUpdateScreen();
                        }

                        @Override
                        public void onEditSave(String newValue) {
                /*Log.i(TAG, newValue);
                Bundle args = new Bundle();
                args.putString(KEY_RAZER_ID, newValue);
                mLoaderManager.restartLoader(LOADER_ID_NICKNAME, args, SampleAccountActivity.this);
                */
                        }
                    });
            addItemAt(emailItem2, 2);
        }
        StringBuffer emailBuffer2 = new StringBuffer();
        emailBuffer2.append(loginData2.Login + " " + (loginData2.Verified == false ? "\n" + unverifiedString : ""));

        String firstSub = unverifiedString.substring(0, 2);
        String lastSub = unverifiedString.substring(unverifiedString.length() - 2, unverifiedString.length());
        int startIndex = emailBuffer2.indexOf(firstSub);
        int endIndex = emailBuffer2.indexOf(lastSub) + 2;

        SpannableString emailSpan2 = new SpannableString(emailBuffer2);
        if (startIndex > -1) {
            emailSpan2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_harvard_crimson)), startIndex, endIndex, 0);
        }

        emailItem2.setSubText(emailSpan2);
    }

    private void processLoaderStatus(Object data) {
        if (data != null) {
            final SynapseAuthenticationModel.Status result = (SynapseAuthenticationModel.Status) data;
            if (result.code == SynapseAuthenticationModel.Code.OK) {
                mLoaderManager.restartLoader(LOADER_USER_DATA, null, this);
            } else {

                if (result.code == SynapseAuthenticationModel.Code.NO_NETWORK || (result.message != null && result.message
                        .contains("Could not connect to server"))) {
                    UiUtils.createNoNetworkSnackbarAndShow(this, findViewById(android.R.id.content));
                    feedBackHandler.obtainMessage(WHAT_NO_NETWORK).sendToTarget();
                } else {

                    feedBackHandler.obtainMessage(WHAT_ERROR, result.message).sendToTarget();
                }
            }
        }
    }


}


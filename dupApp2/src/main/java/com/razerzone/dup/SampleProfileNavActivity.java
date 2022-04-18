package com.razerzone.dup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import androidx.appcompat.widget.AppCompatTextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.razerzone.android.auth.certificate.CertAuthenticationModel;
import com.razerzone.android.core.UserDataV7;
import com.razerzone.android.ui.activity.SplashActivity;
import com.razerzone.android.ui.activity.WebLogin;
import com.razerzone.android.ui.activity.profilenav.ProfileNavActivity;
import com.razerzone.android.ui.activity.profilenav.databinding.ProfileNavItem;
import com.razerzone.android.ui.activity.profilenav.databinding.ProfileNavItemHeader;
import com.razerzone.android.ui.activity.profilenav.databinding.ProfileNavItemLeftIcon;
import com.razerzone.android.ui.activity.profilenav.databinding.ProfileNavItemRazerIdWebsite;
import com.razerzone.android.ui.activity.profilenav.databinding.ProfileNavItemRightIcon;
import com.razerzone.android.ui.activity.profilenav.databinding.ProfileNavItemSignout;
import com.razerzone.android.ui.activity.profilenav.databinding.ProfileNavItemSpacer;
import com.razerzone.android.ui.activity.profilenav.databinding.ProfileNavItemSubtitle;
import com.razerzone.android.ui.activity.profilenav.databinding.ProfileNavItemSubtitleRightText;
import com.razerzone.android.ui.activity.profilenav.databinding.ProfileNavItemSwitch;
import com.razerzone.android.ui.base.IntentFactory;


public class SampleProfileNavActivity extends ProfileNavActivity {

    private final static String TAG = "SampleProfileNav";

    private final static int ID_ACCOUNT = 0;

    private final static int ID_SPACER = 1;

    View.OnClickListener contactClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(TAG, "contactClick triggered");
        }
    };

    View.OnClickListener signoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(SampleProfileNavActivity.this);

            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(final DialogInterface dialog) {

                }
            });
            builder.setTitle(R.string.profile_sign_out);
            builder.setMessage(R.string.cux_dialog_logout_message);
            builder.setNegativeButton(R.string.account_dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    dialog.dismiss();
                }
            });
           /* builder.setNeutralButton(R.string.cux_v6_logout_from_app, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    new AsyncTask<String, String, Boolean>() {
                        @Override
                        protected Boolean doInBackground(final String... strings) {
                            ModelCache.getInstance(SampleProfileNavActivity.this).getAuthenticationModel().setAllAccountsToInactive();
                            return true;
                        }

                        @Override
                        protected void onPostExecute(final Boolean aBoolean) {
                            super.onPostExecute(aBoolean);
                            startActivity(new Intent(SampleProfileNavActivity.this, SplashFragment.class));
                            finish();
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            });
            */
            builder.setPositiveButton(R.string.cux_v6_remove_accounts, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    new AsyncTask<String, String, Boolean>() {
                        @Override
                        protected Boolean doInBackground(final String... strings) {
                            boolean state = CertAuthenticationModel.getInstance().logout();
                            return state;
                        }

                        @Override
                        protected void onPostExecute(final Boolean aBoolean) {
                            super.onPostExecute(aBoolean);

                            startActivity(new Intent(SampleProfileNavActivity.this, SplashActivity.class));
                            finish();
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            });
            builder.show();

            Log.i(TAG, "signoutClick triggered");
        }
    };

    View.OnClickListener accountClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*launchWcri();*/
            startActivity(new Intent(SampleProfileNavActivity.this, mAccountClass));
        }
    };

    View.OnClickListener feedbackClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(TAG, "feedbackClick triggered");

            Intent landingPageIntent = IntentFactory.getLandingPageIntent(SampleProfileNavActivity.this);
            startActivity(IntentFactory.CreateFeedbackIntent(SampleProfileNavActivity.this, landingPageIntent));
        }
    };

    View.OnClickListener faqClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(TAG, "faqClick triggered");
        }
    };
    View.OnClickListener customerServiceCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(IntentFactory.createCustomerSupportIntent(SampleProfileNavActivity.this));
        }
    };
    View.OnClickListener aboutClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            gotoAbout();
        }
    };

    private void gotoAbout() {
        Log.i(TAG, "aboutClick triggered");
        Intent landingPageIntent = new Intent(SampleProfileNavActivity.this, MainActivity.class);
        landingPageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
          String yourLicense=  "<h2>You license here</h2>\n" +
                    "<p>Apache License</p>\n" +
                    "<p>Version 2.0, January 2004</p>\n" +
                    "<p><a href=\"http://www.apache.org/licenses\">http://www.apache.org/licenses</a></p>\n" +
                    "<p>\n" +
                    "Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit...\"\n" +
                  "\"There is no one who loves pain itself, who seeks after it and wants to have it, simply because it is pain...</p>";

        startActivity(IntentFactory.CreateAboutIntent(SampleProfileNavActivity.this, null, null, "Version 1.0",
                "Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur"
                       ,true,true,yourLicense,true));
    }

    @Override
    public boolean requiresProfilePubsub() {
        return true;
    }

    @Override
    public boolean requiresRazerIdWebCookieInjection() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        SpannableString spannableString = new SpannableString("I am a fancy banner. you can customize me.");
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 5, 0);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 6, 9, 0);

        showBanner(spannableString, Color.GREEN, Color.RED, new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(SampleProfileNavActivity.this, "bannerCLick", Toast.LENGTH_SHORT).show();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(SampleProfileNavActivity.this, "BannerClose", Toast.LENGTH_SHORT).show();
                hideBanner(true);//TO HIDE THE BANNER
            }
        }, true);
    }

    @Override
    protected void createNavItems() {
        
        /*
        addItem(new ProfileNavItemHeader(this, R.string.profile_nav_header_friends, null));
        addItem(new ProfileNavItem(this, R.string.profile_nav_add, R.drawable.ic_user_add, null));
        addItem(new ProfileNavItem(this, R.string.profile_nav_contacts, R.drawable.ic_user_profile_fill, contactClick, true));
        addItem(new ProfileNavItem(this, R.string.profile_nav_blocked, R.drawable.ic_blocked_contacts, null));
        addItem(new ProfileNavItemSpacer());
        */
        ProfileNavItemHeader accountHeader = new ProfileNavItemHeader(this, R.string.profile_nav_header_account, null);

        addItem(accountHeader);
        addItem(new ProfileNavItemSubtitle(this, 0,
                R.drawable.profile_pic, 0, accountClick, ID_ACCOUNT));

        ProfileNavItemSwitch switchPush = new ProfileNavItemSwitch(this, com.razerzone.dup.R.string.account_push_notifications,
                0, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(TAG, "checkChanged to " + isChecked);
            }
        }, true);
        addItem(switchPush);

        View.OnClickListener gotoPoc = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SampleProfileNavActivity.this, WebLogin.class));
                finish();
            }
        };
        addItem(new ProfileNavItem(this, com.razerzone.dup.R.string.web_login_poc, 0, gotoPoc));

        addItem(new ProfileNavItemHeader(this, R.string.profile_nav_header_more, null));
        addItem(new ProfileNavItem(this, R.string.profile_nav_feedback, 0, feedbackClick));
        addItem(new ProfileNavItem(this, R.string.profile_nav_faq, 0, faqClick));
        ProfileNavItem about = new ProfileNavItem(this, R.string.profile_nav_about, 0, aboutClick);

        addItem(about);

        ProfileNavItem customerSupport = new ProfileNavItem(this, R.string.customer_support, 0, customerServiceCLick);

        addItem(customerSupport);


        ProfileNavItemHeader others = new ProfileNavItemHeader(SpannableString.valueOf("App Specific"), null);
        addItem(others);
        ProfileNavItemSwitch switchWithIcon = new ProfileNavItemSwitch(this, R.drawable.splash_icon, com.razerzone.dup.R.string.account_push_notifications,
                0, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(TAG, "checkChanged to " + isChecked);
            }
        }, true);
        addItem(switchWithIcon);

        final ProfileNavItemSwitch clickableSwitch = new ProfileNavItemSwitch(SpannableString.valueOf("remove_rowClickable Switch"), SpannableString.valueOf("Clickable Switch subtitle"), new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Toast.makeText(SampleProfileNavActivity.this, "switch row clicked. ", Toast.LENGTH_SHORT).show();
                removeItemById(4324);
                // ProfileNavItemSwitch thisSwitch = (ProfileNavItemSwitch) getItemById(4324);

                //thisSwitch.checkedDefault.set(!thisSwitch.checkedDefault.get());
            }
        }, true);

        clickableSwitch.setId(4324);
        addItem(clickableSwitch);
        clickableSwitch.setIsSubtitleVisible(true);
        ProfileNavItemRightIcon lastAbout = new ProfileNavItemRightIcon(this, R.string.profile_nav_about, R.drawable.splash_icon, new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(SampleProfileNavActivity.this, "dummy action", Toast.LENGTH_SHORT).show();
                getItemById(883).setIconDrawable(getDrawable(R.drawable.bg_toggle_showhide_password));
            }
        });
        lastAbout.setId(883);
        addItem(lastAbout);
        SpannableString titleSpan = new SpannableString("Region FRANCE");
        titleSpan.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 6, 0);
        titleSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#555555")), 7, 13, 0);
        titleSpan.setSpan(new RelativeSizeSpan(.5f), 7, 13, 0);

        ProfileNavItemLeftIcon smallIcon = new ProfileNavItemLeftIcon(titleSpan, getDrawable(R.drawable.splash_icon),
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Toast.makeText(SampleProfileNavActivity.this, "dummy action", Toast.LENGTH_SHORT).show();
                        getItemById(898).setTitle(SpannableString.valueOf("changed title"));
                        ;
                        getItemById(898).setIconDrawable(getDrawable(R.drawable.bg_toggle_showhide_password));
                    }
                });
        smallIcon.setId(898);

        addItem(smallIcon);

        SpannableString spannableString = new SpannableString("Stylable top text");
        spannableString.setSpan(new UnderlineSpan(), 0, 2, 0);
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 3, 5, 0);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 6, 8, 0);

        SpannableString spannableStringBottom = new SpannableString("Sub text...quick brown fox...");
        spannableStringBottom.setSpan(new UnderlineSpan(), 0, 5, 0);
        spannableStringBottom.setSpan(new ForegroundColorSpan(Color.RED), 0, 5, 0);

        addItem(new ProfileNavItemSubtitle(spannableString, spannableStringBottom, new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(SampleProfileNavActivity.this, "dummy action", Toast.LENGTH_SHORT).show();
            }
        }, 123));

        SpannableString imRed = new SpannableString("im red");
        imRed.setSpan(new ForegroundColorSpan(Color.RED), 0, 6, 0);
        ProfileNavItemSubtitle withIConRed = new ProfileNavItemSubtitle(getDrawable(R.drawable.splash_icon), imRed,
                SpannableString.valueOf(""),
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Toast.makeText(SampleProfileNavActivity.this, "dummy action", Toast.LENGTH_SHORT).show();
                    }
                }, false, 1333);

        addItem(withIConRed);

        SpannableString imBlue = new SpannableString("im red. Just kidding. BLUE");
        SpannableString greenSubtext = new SpannableString("im a green subtext");
        greenSubtext.setSpan(new ForegroundColorSpan(Color.GREEN), 0, 18, 0);
        imBlue.setSpan(new ForegroundColorSpan(Color.BLUE), 3, 6, 0);
        imBlue.setSpan(new ForegroundColorSpan(Color.RED), 21, 26, 0);
        ProfileNavItemSubtitle withIConBlue = new ProfileNavItemSubtitle(getDrawable(R.drawable.splash_icon), imBlue, greenSubtext
                ,
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Toast.makeText(SampleProfileNavActivity.this, "dummy action", Toast.LENGTH_SHORT).show();
                    }
                }, true, 13333);

        addItem(withIConBlue);


        SpannableString colorMeAnything = new SpannableString("anycolor will do");

        colorMeAnything.setSpan(new ForegroundColorSpan(Color.GREEN), 0, 1, 0);
        colorMeAnything.setSpan(new ForegroundColorSpan(Color.BLUE), 2, 3, 0);
        colorMeAnything.setSpan(new ForegroundColorSpan(Color.YELLOW), 4, 5, 0);
        SpannableString tittlespan = new SpannableString("title Spanable text");
        SpannableString subSpan = new SpannableString("sub text span");
        subSpan.setSpan(new ForegroundColorSpan(Color.GREEN), 0, 2, 0);
        tittlespan.setSpan(new ForegroundColorSpan(Color.BLUE), 3, 7, 0);
        ProfileNavItemSubtitleRightText subtitleRightText = new ProfileNavItemSubtitleRightText(imBlue, greenSubtext, colorMeAnything
                ,
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Toast.makeText(SampleProfileNavActivity.this, "dummy action", Toast.LENGTH_SHORT).show();
                    }
                });

        addItem(subtitleRightText);


        SpannableString underlineTitle = new SpannableString("i'm an underlined title");
        underlineTitle.setSpan(new UnderlineSpan(), 0, 17, 0);
        SpannableString cooltext = new SpannableString("cool");
        cooltext.setSpan(new ForegroundColorSpan(Color.RED), 0, 4, 0);
        ProfileNavItemSubtitle withIUnderline = new ProfileNavItemSubtitle(getDrawable(R.drawable.splash_icon), underlineTitle, cooltext
                ,
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Toast.makeText(SampleProfileNavActivity.this, "dummy action", Toast.LENGTH_SHORT).show();
                    }
                }, true, 133333);

        addItem(withIUnderline);

        ProfileNavItemSubtitle withIcon = new ProfileNavItemSubtitle(getDrawable(R.drawable.splash_icon), SpannableString.valueOf("biggerIcon"),
                SpannableString.valueOf(""),
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Toast.makeText(SampleProfileNavActivity.this, "dummy action", Toast.LENGTH_SHORT).show();
                    }
                }, false, 133);

        withIcon.setElevation(2f);
        addItem(withIcon);




        addItem(new ProfileNavItemSpacer(this, R.dimen.profile_nav_item_spacer_signout_height, ID_SPACER));
        ProfileNavItemSignout signOut = new ProfileNavItemSignout(signoutClick);

        addItem(signOut);

        addItem(new ProfileNavItemSpacer(this, R.dimen.profile_nav_item_spacer_signout_height, 3));


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SpannableString underlineTitle = new SpannableString("i am added  1sec later");
                ProfileNavItemSubtitle withIUnderline = new ProfileNavItemSubtitle(getDrawable(R.drawable.splash_icon), underlineTitle, null
                        ,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                Toast.makeText(SampleProfileNavActivity.this, "dummy action", Toast.LENGTH_SHORT).show();
                            }
                        }, false, 1353433);

                addItemAt(5, withIUnderline);
            }
        }, 1000);
    }

    @Override
    protected void onUserDataLoaded(UserDataV7 userData) {
        updateFromUserData(userData);
    }

    @Override
    protected void guestFaqClicked() {
        Toast.makeText(this, "guest faq clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void guestAboutClicked() {
        gotoAbout();
    }

    private void updateFromUserData(UserDataV7 userData) {
        RecyclerView.ViewHolder viewHolder = getViewHolderByItemId(ID_ACCOUNT);
        if (viewHolder != null) {
            SimpleDraweeView ivAccountPic = viewHolder.itemView.findViewById(R.id.profile_nav_item_icon);
            if (ivAccountPic != null) {
                ivAccountPic.setImageURI(userData.GetAvatarUrl());
            }

            AppCompatTextView tvId =  (AppCompatTextView) viewHolder.itemView.findViewById(R.id.profile_nav_item_title);
            AppCompatTextView tvEmail =  (AppCompatTextView) viewHolder.itemView.findViewById(R.id.profile_nav_item_subtitle);

            if (tvId != null && tvEmail != null) {
                tvEmail.setVisibility(View.VISIBLE);
                tvId.setVisibility(View.VISIBLE);
                String email = null;
                String razerID = userData.GetRazerId();
                tvId.setText(razerID);
                if (userData.GetPrimaryEmail() != null) {
                    email = userData.GetPrimaryEmail().Login;
                }
                if (TextUtils.isEmpty(email) == false) {
                    SpannableString emailSpannable;
                    if (userData.getFirstPrimaryEmail().Verified == false) {
                        String unverifiedText = getString(R.string.unverified);
                        String finalEmail = email + "\n" + unverifiedText;
                        int start = finalEmail.indexOf("(");
                        emailSpannable = new SpannableString(finalEmail);
                        int color;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            color = getColor(R.color.cux_v7_error_red);
                        } else {
                            color = getResources().getColor(R.color.cux_v7_error_red);
                        }

                        emailSpannable.setSpan(new ForegroundColorSpan(color), start, finalEmail.length(), 0);
                    } else {
                        emailSpannable = new SpannableString(email);
                    }

                    tvEmail.setText(emailSpannable);
                }
                //  Log.e("cache", "email:" + email + ", id" + razerID);
            } else {
                System.out.print("");
            }
        }
    }
}

# Overview

Mobile kit for Apps is a fast and convenient way to incorporate Razer id to your native application. It is available Native and Webview look in feel.

## Use Cases
Mobile kit is used in the provision of the following experiences:
## User Sign Up
Mobile kit is a quick and convenient way to allow your android application to onboard users to Razer Id ecosystem.
## SSO
Mobile kit is a quick and convenient way for your android application to share credentials with existing Razer Applications like Cortex Launcher, Cortex Deals, Themestore, Oobe, Sila,Raiju,Game pad.
Users will no longer have to input his/her password.
## Getting Started

### Integration

1. Add gradle dependencies

build.gradle
```
repositories {
    maven {
        url 'http://nexus-mobile.razerapi.com:8081/repository/thirdparty'
        //allowInsecureProtocol = true  //optional you may need this depending on your builtools version
        credentials {
            username 'thirdparty'
            password 'thirdparty_123'
        }
    }
}
 
dependencies {
   ...
       implementation "com.razer.android:ui:2.0.1"
   ...
}
```
2. Add meta data to your application manifest. Request these information from Razer ID Team
   contact: joseph.deguzman@razer.com, udhayakumar.nattamai@razer.com & zhou.shijun@razer.com

   a.) service code
   b.) project name
   c.) synapse/auth2 client id

```
<application android:name=".Application">
...
<meta-data
    android:name="synapse_service_code"
    android:value="XXXX" />
<meta-data
    android:name="synapse_project_name"
    android:value="XXXXXXXXX" />
<meta-data
    android:name="synapse_app_id"
    android:value="XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" />
<meta-data
    android:name="synapse_scopes"
    android:value="cop" />
...
```
3. Set Environment and Profile screens
   setIsStaging(false) = Production razerid
   setIsStaging(true)   = Staging razerid

```
public class Application extends android.app.Application {
   @Override
   public void onCreate() {
   super.onCreate();
      BaseAuthConfig authConfig = new BaseAuthConfig.Builder().
      setIsStaging(isStaging).
      setFacebookid(getString(R.string.flavored_fb_id)).
      setGoogleid(getString(R.string.flavored_google_id)).
      setTwitchid(getString(R.string.flavored_twitch_id)).
      build();
      AuthCore.init(this, authConfig);
      UiCore.init(this, SampleProfileNavActivity.class, SampleAccountActivity.class);
      ...
   }
}
```

if you do not intend to show User profile, pass null to Profile and Account class.

4. Getting JWT Token, From Mobile kit
```
   String thisToken = CertAuthenticationModel.getInstance().getLoggedInJWTToken();
```
5. Show Login/signup/sso/ssi  Screen
```
    startActivity(IntentFactory.createStartIntent(this));
```
6. To Check for the Authentication state of the user
```
    boolean isLoggedInAnonOrAuthenticated =CertAuthenticationModel.getInstance().isCertLoggedIn();
    boolean isGuestAccountLoggedIn=CertAuthenticationModel.getInstance().isCertLoggedInAnon();
    boolean isAuthenticated =CertAuthenticationModel.getInstance().isLoggedIn();// returns false if its guest user. true if the user authenticated with email password or ssi
```
7. Native or Webview Frontent
```
    // Razer ID webview front end
    UiConfig.Builder(context).setWebViewLogin(true);  
    
    // options available: Static images background, Video Background, Parallax Video Background
    UiConfig.Builder(context).setWebViewLogin(false);  
```
Native: Video Background With Parallax effect
![Alt Text](https://rz-s3-mobileapp-assets-prod.s3.ap-southeast-1.amazonaws.com/mkit/parallax.gif)
Webview:
![Alt Text](https://rz-s3-mobileapp-assets-prod.s3.ap-southeast-1.amazonaws.com/mkit/wcri.gif)
Refer  to AppSplashActivity.java for other ui configuration

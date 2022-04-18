-include proguard-rules.pro
-keepattributes SourceFile,LineNumberTable
-keepclasseswithmembernames class *{
    public <init>(java.lang.Class,android.support.test.internal.util.AndroidRunnerParams);
}
-keep class android.uiautomator.** { *;}
-keep public class junit.framework.** { *;}
-keep class android.support.** { *;}
-keep interface android.support.** { *;}

-keepclassmembers class android.uiautomator.** {
   public *;
}
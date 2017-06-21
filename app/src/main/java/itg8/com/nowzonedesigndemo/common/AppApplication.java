package itg8.com.nowzonedesigndemo.common;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;

import timber.log.BuildConfig;
import timber.log.Timber;

import static timber.log.Timber.DebugTree;
/**
 * Created by itg_Android on 6/7/2017.
 */

public class AppApplication extends Application {

//    private final String PROFILE_MODEL = getPackageName().concat("PROFILE_MODEL");
    private ProfileModel profileModel;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    public ProfileModel getProfileModel() {
//        if(profileModel==null){
//            profileModel=new Gson().toJson(SharePrefrancClass.getInstance(this).getPref(PROFILE_MODEL);
//        }
//        return profileModel;
        return null;
    }

    /** A tree which logs important information for crash reporting. */
    private static class CrashReportingTree extends Timber.Tree {
        @Override protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            FakeCrashLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(t);
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(t);
                }
            }
        }
    }
}

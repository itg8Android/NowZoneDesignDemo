package itg8.com.nowzonedesigndemo.common;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import timber.log.BuildConfig;
import timber.log.Timber;

import static timber.log.Timber.DebugTree;
/**
 * Created by itg_Android on 6/7/2017.
 */

public class AppApplication extends Application {

    private static final String PROFILE_MODEL = "PROFILE_MODEL";
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
        String mProfileModel=SharePrefrancClass.getInstance(this).getPref(PROFILE_MODEL);
        if(profileModel==null && mProfileModel!=null){
            profileModel=new Gson().fromJson(mProfileModel,new TypeToken<ProfileModel>(){}.getType());
        }
        return profileModel;
    }

    public void setProfileModel(ProfileModel model){
        if(model!=null)
            SharePrefrancClass.getInstance(this).savePref(PROFILE_MODEL,new Gson().toJson(model));
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

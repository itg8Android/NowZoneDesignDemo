package itg8.com.nowzonedesigndemo.utility;

/**
 * Created by itg_Android on 5/18/2017.
 */

 interface AccelVerifyListener {
    void onMotionStarts();

    void onMotionEnds();

    void onStep(int step);
}

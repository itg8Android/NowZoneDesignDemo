package itg8.com.nowzonedesigndemo.widget.maditation;

/**
 * Created by Android itg 8 on 7/7/2017.
 */

public class SpriteFactory {
    public static Sprite create(Style style) {
        Sprite sprite = null;
        switch (style) {

            case DOUBLE_BOUNCE:
                sprite = new DoubleBounce();
                break;

            default:
                break;
        }
        return sprite;
    }

}

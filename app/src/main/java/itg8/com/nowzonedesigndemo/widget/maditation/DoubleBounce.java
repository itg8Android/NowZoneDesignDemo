package itg8.com.nowzonedesigndemo.widget.maditation;

import android.animation.ValueAnimator;
import android.os.Build;

import itg8.com.nowzonedesigndemo.widget.maditation.animation.SpriteAnimatorBuilder;

/**
 * Created by Android itg 8 on 7/7/2017.
 */

public class DoubleBounce  extends  SpriteContainer {

    @Override
    public Sprite[] onCreateChild() {
        return new Sprite[]{
                new Bounce(), new Bounce()
        };
    }

    @Override
    public void onChildCreated(Sprite... sprites) {
        super.onChildCreated(sprites);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sprites[1].setAnimationDelay(1000);
        } else {
            sprites[1].setAnimationDelay(-1000);
        }
    }

    private class Bounce extends CircleSprite {

        Bounce() {
            setAlpha(153);
            setScale(0f);
        }

        @Override
        public ValueAnimator onCreateAnimation() {
            float fractions[] = new float[]{0f, 0f, 0.5f};
            return new SpriteAnimatorBuilder(this).scale(fractions, 1f, 0f, 1f).
                    duration(2000).
                    easeInOut(fractions)
                    .build();
        }
    }
}

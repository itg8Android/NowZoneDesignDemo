package itg8.com.nowzonedesigndemo.common;

import android.graphics.Typeface;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.Window;
import android.widget.TextView;



public abstract class BaseActivity extends AppCompatActivity {

    public Typeface typefaceDroidSans;
    public Typeface typefaceDroidSerifRegular;
    public Typeface typefaceDroidSansRegular;
    public Typeface typefaceOpenSansSemiBold;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }




    public void setFontDroidSans(FontType fontType, TextView... txt) {
        if (fontType == FontType.DROIDSANS) {
            typefaceDroidSans = CommonMethod.setFontDroidSans(this);
            for (TextView text : txt
                    ) {
                text.setTypeface(typefaceDroidSans);

            }
        }
    }

    public void setFontDroidSansRegular(FontType fontType, TextView... txt) {
        if (fontType == FontType.DROIDSANSREGULAR) {
            typefaceDroidSansRegular = CommonMethod.setFontOpenSansRegular(this);
            for (TextView text : txt
                    ) {
                text.setTypeface(typefaceDroidSansRegular);

            }
        }
    }

    public void setFontDroidSerifRegular(FontType fontType, TextView... txt) {
        if (fontType == FontType.DROIDSERIFREGULAR) {
            typefaceDroidSerifRegular = CommonMethod.setFontDroidSerifRegular(this);
            for (TextView text : txt
                    ) {
                text.setTypeface(typefaceDroidSerifRegular);

            }
        }
    }

    public void setFontOpenSansSemiBold(FontType fontType, TextView... txt) {
        if (fontType == FontType.JURAMEDIUM) {
            typefaceOpenSansSemiBold = CommonMethod.setFontOpenSansSemiBold(this);
            for (TextView text : txt
                    ) {
                text.setTypeface(typefaceOpenSansSemiBold);

            }

        }
    }

    public void setFontOxygenRegular(FontType fontType, TextView... txt) {
        if (fontType == FontType.ROBOTOlIGHT) {
            typefaceOpenSansSemiBold = CommonMethod.setFontRobotoLight(this);
            for (TextView text : txt
                    ) {
                text.setTypeface(typefaceOpenSansSemiBold);

            }

        }
    }


}




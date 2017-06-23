package itg8.com.nowzonedesigndemo.alarm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.List;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.alarm.model.AlarmDaysModel;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;

/**
 * Created by Android itg 8 on 6/22/2017.
 */

public class AlarmDaysAdapter extends BaseAdapter {

    private final List<AlarmDaysModel> days;
    private LayoutInflater inflater;

    private Context mContext;


    public AlarmDaysAdapter(Context mContext, List<AlarmDaysModel> days) {
        this.mContext = mContext;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.days = days;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
//        if(convertView != null) {
//            view = (View)convertView;
//        }else {
        view = inflater.inflate(R.layout.item_alarmdays, parent, false);

//            convertView.setTag(view);

//        }

        CustomFontTextView txt = (CustomFontTextView) view.findViewById(R.id.txt_days);
//        ViewGroup container = (ViewGroup) view.findViewById(R.id.ll_container);
        ImageView img = (ImageView) view.findViewById(R.id.img_dots);
        txt.setText(days.get(position).getDays());

        if (days.get(position).isChecked()) {
            img.setVisibility(View.VISIBLE);


        } else {
            img.setVisibility(View.GONE);
        }
        return view;
    }


}

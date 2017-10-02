package itg8.com.nowzonedesigndemo.breath_history.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.db.tbl.TblState;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;
import itg8.com.nowzonedesigndemo.utility.BreathState;
import itg8.com.nowzonedesigndemo.utility.Helper;

/**
 * Created by Android itg 8 on 9/20/2017.
 */

public class BreathsHistoryAdapter extends RecyclerView.Adapter<BreathsHistoryAdapter.BreathViewHolder> {



    private Context context;
    private BreathState type;
    private List<TblState> listState;
    private BitmapDrawable draw;

     public   interface OnItemHistoryClickedListener{
         void onItemClicked(View view, int position, TblState listState);
     }
      private OnItemHistoryClickedListener listener;


    public BreathsHistoryAdapter(Context mContext, List<TblState> listState, OnItemHistoryClickedListener listener) {
        context = mContext;
        this.listState = listState;
        this.listener = listener;
    }

    @Override
    public BreathViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_breath_history, parent, false);
        BreathViewHolder holder = new BreathViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BreathViewHolder holder, int position) {
        type = BreathState.valueOf(listState.get(position).getState());
        int imgRes = 0;
        int cardBgRes = 0;
        int cardBgColor = 0;
        switch (type) {
            case CALM:
               // cardBgRes = (R.drawable.back_gradient_compose);
                imgRes = R.drawable.calm_streak_icon;
                cardBgColor =R.color.color_composed;

                break;
            case FOCUSED:
//                holder.cardview.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_attentive_half));
                cardBgRes = (R.drawable.back_gradient_attentive);
                imgRes = R.drawable.focus_streak_icon;
                cardBgColor =R.color.color_attentive;


                break;
            case STRESS:
//                holder.cardview.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_stress_half));
                cardBgRes = (R.drawable.back_gradient_stress);
                imgRes = R.drawable.stress_streak_icon;
                cardBgColor =R.color.color_stress;


                break;
            case SEDENTARY:
//                holder.cardview.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_normal));
                cardBgRes = (R.drawable.back_gradient_compose);
                imgRes = R.drawable.sedentory_icon;

                cardBgColor =R.color.color_normal;

                break;
            case ACTIVITY:
//                holder.cardview.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_normal));
                imgRes = (R.drawable.activity_icon);
                cardBgColor =R.color.color_steps;
                break;
            case POSTURE:
//                holder.cardview.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_normal));
                imgRes = (R.drawable.posture_icon);
                cardBgColor =R.color.color_posture_half;
                break;
            default:
                imgRes = R.drawable.stress_streak_icon;
                cardBgRes = (R.drawable.back_gradient_compose);
                cardBgColor =R.color.color_normal;


        }

        holder.img.setImageResource(imgRes);
        //  holder.imgIcon.setImageResource(imgRes);
//        holder.cardview.setBackgroundResource(cardBgRes);
        //  holder.imgTimeLine.setBackground(draw);



        holder.imgIcon.setColorFilter(ContextCompat.getColor(context,cardBgColor));
        holder.imgMsg.setColorFilter(ContextCompat.getColor(context,cardBgColor));
        holder.view.setBackgroundColor(ContextCompat.getColor(context,cardBgColor));
        holder.lblType.setText(listState.get(position).getState());
        holder.lblHrValue.setText(Helper.calculateMinuteDiffFromTimestamp(listState.get(position).getTimestampStart(), listState.get(position).getTimestampEnd()));
        holder.lblTime.setText(Helper.getDateTimeFromMillies(listState.get(position).getTimestampStart()) + "\n-\n" + Helper.getDateTimeFromMillies(listState.get(position).getTimestampEnd()));

    }

    @Override
    public int getItemCount() {
        return listState.size();
    }

    public class BreathViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lbl_time)
        CustomFontTextView lblTime;
        @BindView(R.id.lbl_type)
        CustomFontTextView lblType;
        @BindView(R.id.lbl_hr_value)
        CustomFontTextView lblHrValue;
        @BindView(R.id.lbl_hr)
        CustomFontTextView lblHr;
        @BindView(R.id.cardview_comment)
        CardView cardview;
        //
        @BindView(R.id.img)
        ImageView img;

        @BindView(R.id.frameLayout)
        FrameLayout frameLayout;
        @BindView(R.id.img_time_line)
        ImageView imgTimeLine;

        @BindView(R.id.view)
        View view;
        @BindView(R.id.img_icon)
        ImageView imgIcon;
        @BindView(R.id.img_msg)
        ImageView imgMsg;

        public BreathViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     listener.onItemClicked(itemView, getAdapterPosition(), listState.get(getAdapterPosition()));
                 }
             });
        }
    }
}

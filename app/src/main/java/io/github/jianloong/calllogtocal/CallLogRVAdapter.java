package io.github.jianloong.calllogtocal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.util.DateTime;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.security.AccessController.getContext;

/**
 * Created by Jian on 6/05/2016.
 */
public class CallLogRVAdapter extends RecyclerView.Adapter<CallLogRVAdapter.PhoneLogViewHolder> {

    List<PhoneLog> phoneLogList;
    Activity activity;
    Context context;

    CallLogRVAdapter(List<PhoneLog> phoneLogs, Activity activity, Context c) {
        this.phoneLogList = phoneLogs;
        this.activity = activity;
        this.context = c;

    }

    @Override
    public CallLogRVAdapter.PhoneLogViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view,
                viewGroup,
                false);
        PhoneLogViewHolder pvh = new PhoneLogViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PhoneLogViewHolder phoneLogViewHolder, int i) {

        String name = phoneLogList.get(i).getName();
        String number = phoneLogList.get(i).getNumber();
        String callType = phoneLogList.get(i).getCallType();

        if (name.equals("Unknown Number"))
            phoneLogViewHolder.name.setText(number);
        else
            phoneLogViewHolder.name.setText(name);

        phoneLogViewHolder.date.setText(phoneLogList.get(i).getDate().toStringRfc3339());

        DateTime dt = phoneLogList.get(i).getDate();

        String formattedDate = DateUtils.getRelativeDateTimeString(context,
                dt.getValue(),
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS,
                0).toString();

        phoneLogViewHolder.date.setText(formattedDate);
        phoneLogViewHolder.type.setText(callType);
        phoneLogViewHolder.setOnClickListener(phoneLogList.get(i));
    }

    @Override
    public int getItemCount() {
        return phoneLogList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class PhoneLogViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView number;
        TextView date;
        TextView type;
        Button button;
        ImageView exportButton;

        PhoneLogViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            name = (TextView) itemView.findViewById(R.id.name);
            number = (TextView) itemView.findViewById(R.id.number);
            date = (TextView) itemView.findViewById(R.id.date);
            type = (TextView) itemView.findViewById(R.id.type);
            exportButton = (ImageView) itemView.findViewById(R.id.exportImage);
        }

        public void setOnClickListener(final PhoneLog phoneLog) {
            exportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, CalendarActivity.class);
                    //Using map because Gson had troubles serialising the Event object.
                    Map<String, String> map = new HashMap<>();
                    map.put("type", "Call");
                    map.put("summary", phoneLog.getName());
                    map.put("number", phoneLog.getNumber());
                    map.put("startDateTime", phoneLog.getDate().toStringRfc3339());
                    map.put("endDateTime", phoneLog.getDate().toStringRfc3339());
                    map.put("description", phoneLog.getDuration() + "");

                    intent.putExtra("event", new Gson().toJson(map));
                    activity.startActivity(intent);
                }
            });
        }
    }

}
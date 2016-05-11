package io.github.jianloong.calllogtocal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.client.util.DateTime;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jian on 6/05/2016.
 */
public class SmsLogRVAdapter extends RecyclerView.Adapter<SmsLogRVAdapter.SmsLogViewHolder>{

    List<Sms> smsList;
    Activity activity;
    Context context;

    SmsLogRVAdapter(List<Sms> smses, Activity activity, Context c){
        this.smsList = smses;
        this.activity = activity;
        this.context = c;
    }

    public class SmsLogViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        private TextView name;
        private TextView number;
        TextView date;
        TextView body;
        TextView type;
        Button button;
        ImageView image;
        ImageView exportButton;

        SmsLogViewHolder(View itemView) {
            super(itemView);

            cv = (CardView)itemView.findViewById(R.id.cv);
            name = (TextView)itemView.findViewById(R.id.name);
            number = (TextView)itemView.findViewById(R.id.number);
            date = (TextView)itemView.findViewById(R.id.date);
            body = (TextView) itemView.findViewById(R.id.body);
            type = (TextView) itemView.findViewById(R.id.type);
            image = (ImageView) itemView.findViewById(R.id.imageType);
            exportButton = (ImageView) itemView.findViewById(R.id.exportImage);

        }

        public void setOnClickListener(final Sms sms){
            exportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(activity, CalendarActivity.class);
                    //Using map because Gson had troubles serialising the Event object.
                    Map<String, String> map = new HashMap<>();

                    map.put("type", "Sms");
                    map.put("summary",sms.getPerson());
                    map.put("number", sms.getAddress());
                    map.put("startDateTime",sms.getDate().toStringRfc3339());
                    map.put("endDateTime" ,sms.getDate().toStringRfc3339());
                    map.put("description", sms.getBody() + "");

                    intent.putExtra("event", new Gson().toJson(map));
                    activity.startActivity(intent);
                }
            });
        }

        //TODO Open card if too much info
        public void setCardOnClickListener(final Sms sms){
            cv.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    //body.setText(sms.getBody());
                    //body.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public SmsLogRVAdapter.SmsLogViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sms_card, viewGroup, false);
        SmsLogViewHolder svh = new SmsLogViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(final SmsLogViewHolder smsLogViewHolder, int i) {
        DateTime dt = smsList.get(i).getDate();
        String person = smsList.get(i).getPerson();
        String address = smsList.get(i).getAddress();
        if(person.equals("Unknown")) {
            smsLogViewHolder.name.setText(address);

        }else {
            smsLogViewHolder.name.setText(person);
            smsLogViewHolder.number.setText(address);
        }

        smsLogViewHolder.date.setText(smsList.get(i).getDate().toStringRfc3339());
        String body = smsList.get(i).getBody();
        String type = smsList.get(i).getType();

        switch (type){
            case "1" :
                type = "Inbox";
                smsLogViewHolder.image.setImageResource(R.mipmap.incoming);
                break;
            case "2":
                type = "Sent";
                smsLogViewHolder.image.setImageResource(R.mipmap.outgoing);
                break;
            case "3":
                type = "Draft";
                break;
        }

        smsLogViewHolder.image.setVisibility(View.VISIBLE);
        smsLogViewHolder.body.setText(body);
        smsLogViewHolder.body.setVisibility(View.VISIBLE);

        String str = DateUtils.getRelativeDateTimeString(context, dt.getValue(), DateUtils
                .MINUTE_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS,0).toString();

        smsLogViewHolder.date.setText(str);
        smsLogViewHolder.type.setText(type);
        smsLogViewHolder.type.setVisibility(View.VISIBLE);
        smsLogViewHolder.setOnClickListener(smsList.get(i));
        smsLogViewHolder.setCardOnClickListener(smsList.get(i));
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
package io.github.jianloong.calllogtocal;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class SmsLogFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;
    private ProgressBarCircular progressBarCircular;
    private RecyclerView rv;

    public static SmsLogFragment newInstance(int position) {
        SmsLogFragment f = new SmsLogFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        position = getArguments().getInt(ARG_POSITION);
        View rootView = inflater.inflate(R.layout.page, container, false);

        progressBarCircular = (ProgressBarCircular) rootView.findViewById(R.id.progress);

        this.rv = (RecyclerView) rootView.findViewById(R.id.rv);
        this.rv.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        this.rv.setLayoutManager(layoutManager);

        new RequestSmsLog(progressBarCircular, rv).execute();

        return rootView;
    }

    /**
     *
     */
    private class RequestSmsLog extends AsyncTask<String, Void, List<Sms>> {

        private ProgressBarCircular progressBarCircular;
        private List<Sms> smses;
        private RecyclerView rv;

        public RequestSmsLog(ProgressBarCircular progressBarCircular,
                             RecyclerView recyclerView) {
            super();

            this.progressBarCircular = progressBarCircular;
            this.smses = new ArrayList<>();
            this.rv = recyclerView;
        }

        @Override
        protected List<Sms> doInBackground(String... params) {
            smses = getAllSms();

            return smses;
        }

        @Override
        protected void onPostExecute(List<Sms> phoneLogs) {
            progressBarCircular.setVisibility(View.INVISIBLE);
            SmsLogRVAdapter adapter = new SmsLogRVAdapter(phoneLogs, getActivity(), getContext());
            rv.setAdapter(adapter);

            // Needed to prevent RecycleView canScrollHorizontally issue. Do not remove
            rv.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPreExecute() {
            progressBarCircular.setVisibility(View.VISIBLE);
        }

        private List<Sms> getAllSms() {

            HashMap<String, String> map = new HashMap<>();
            List<Sms> smses = new ArrayList<>();
            Uri allMessages = Uri.parse("content://sms/");
            Cursor c = getActivity().getBaseContext().getContentResolver().query(allMessages, null,
                    null, null, Telephony.Sms.DATE + " DESC");
            while (c.moveToNext()) {
                Sms sms = new Sms();
                String number = c.getString(c.getColumnIndex(Telephony.Sms.ADDRESS));
                String body = c.getString(c.getColumnIndex(Telephony.Sms.BODY));
                String type = c.getString(c.getColumnIndex(Telephony.Sms.TYPE));
                long date = c.getLong(c.getColumnIndex(Telephony.Sms.DATE));

                sms.setAddress(number);
                sms.setBody(body);
                sms.setDate(new DateTime(date));
                sms.setType(type);


                if (map.containsKey(number))
                    sms.setPerson(map.get(number));
                else {
                    String name = getContactName(number);
                    map.put(number, name);
                    sms.setPerson(name);
                }



                smses.add(sms);
            }
            c.close();

            return smses;
        }


        public String getContactName(String phoneNumber) {

            ContentResolver cr = getActivity().getContentResolver();
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
            if (cursor == null) {
                return "Unknown";
            }
            String contactName = "Unknown";
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            }

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

            return contactName;
        }

        /**
         * http://stackoverflow.com/questions/3712112/search-contact-by-phone-number?rq=1
         * @param number
         * @return
         */
        public String getContactDisplayNameByNumber(String number) {
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
            String name = "?";
            ContentResolver contentResolver = getActivity().getContentResolver();
            Cursor contactLookup = contentResolver.query(uri, new String[] {BaseColumns._ID,
                    ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);
            try {
                if (contactLookup != null && contactLookup.getCount() > 0) {
                    contactLookup.moveToNext();
                    name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                }
            } finally {
                if (contactLookup != null) {
                    contactLookup.close();
                }
            }

            return name;
        }


    }
}
package io.github.jianloong.calllogtocal;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class CallLogFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;
    private ProgressBarCircular progressBarCircular;
    private RecyclerView rv;


    public static CallLogFragment newInstance(int position) {
        CallLogFragment f = new CallLogFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        position = getArguments().getInt(ARG_POSITION);
        View rootView = inflater.inflate(R.layout.page, container, false);
        progressBarCircular = (ProgressBarCircular) rootView.findViewById(R.id.progress);

        this.rv = (RecyclerView) rootView.findViewById(R.id.rv);
        this.rv.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        this.rv.setLayoutManager(layoutManager);

        new RequestCallLog(progressBarCircular, rv).execute();

        return rootView;
    }

    private class RequestCallLog extends AsyncTask<String, Void, List<PhoneLog>> {

        private ProgressBarCircular progressBarCircular;
        private List<PhoneLog> phoneLogs;
        private RecyclerView rv;


        public RequestCallLog(ProgressBarCircular progressBarCircular, RecyclerView rv) {
            super();
            this.progressBarCircular = progressBarCircular;
            this.phoneLogs = new ArrayList<>();
            this.rv = rv;

        }

        @Override
        protected List<PhoneLog> doInBackground(String... params) {
            phoneLogs = getPhoneLogs();
            return phoneLogs;
        }

        @Override
        protected void onPostExecute(List<PhoneLog> phoneLogs) {

            progressBarCircular.setVisibility(View.INVISIBLE);
            CallLogRVAdapter adapter = new CallLogRVAdapter(phoneLogs, getActivity(), getContext());
            rv.setAdapter(adapter);
            // Needed to prevent RecycleView canScrollHorizontally issue. Do not remove
            rv.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPreExecute() {
            progressBarCircular.setVisibility(View.VISIBLE);
        }

        private List<PhoneLog> getPhoneLogs() {
            List<PhoneLog> phoneLogs = new ArrayList<>();
            ContentResolver cr = getActivity().getContentResolver();

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission
                    .READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {

            }
            Cursor c = cr.query(CallLog.Calls.CONTENT_URI, null, null,
                    null, CallLog.Calls.DATE + " DESC");

            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    PhoneLog phoneLog = new PhoneLog();

                    String callerNumber = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
                    String name = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    long callDate = c.getLong(c.getColumnIndex(CallLog.Calls.DATE));
                    long callDuration = c.getLong(c.getColumnIndex(CallLog.Calls.DURATION));
                    int callType = c.getInt(c.getColumnIndex(CallLog.Calls.TYPE));

                    phoneLog.setNumber(callerNumber);
                    name = name == null ? "Unknown Number" : name;
                    phoneLog.setName(name);
                    DateTime d = new DateTime(callDate);
                    phoneLog.setDate(d);
                    phoneLog.setDuration(callDuration);

                    switch (callType) {
                        case CallLog.Calls.INCOMING_TYPE:
                            phoneLog.setCallType("Incoming");
                            break;
                        case CallLog.Calls.OUTGOING_TYPE:
                            phoneLog.setCallType("Outgoing");
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            phoneLog.setCallType("Missed");
                            break;
                    }
                    phoneLogs.add(phoneLog);
                } while (c.moveToNext());
            }
            c.close();
            return phoneLogs;
        }
    }
}
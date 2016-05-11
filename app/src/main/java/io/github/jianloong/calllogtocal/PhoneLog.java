package io.github.jianloong.calllogtocal;

import com.google.api.client.util.DateTime;

import java.util.Comparator;

/**
 * Created by Jian on 24/04/2016.
 */
public class PhoneLog implements Comparator {
    private String name;
    private DateTime date;
    private String number;
    private long duration;
    private String callType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getCallType() {
        return this.callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    @Override
    public String toString() {
        return "Name : " + name + "\n" +
                "Date : " + date + "\n" +
                "No : " + number + "\n" +
                "Call Type : " + callType + "\n" +
                "Duration : " + duration;
    }

    @Override
    public int compare(Object lhs, Object rhs) {
        return 0;
    }
}

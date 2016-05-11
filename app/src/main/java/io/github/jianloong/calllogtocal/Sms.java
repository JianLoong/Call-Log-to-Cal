package io.github.jianloong.calllogtocal;

import com.google.api.client.util.DateTime;

import java.util.Comparator;


/**
 * Created by Jian on 25/04/2016.
 */
public class Sms{
    private String id;
    private String address;
    private String threadId;
    private String person;
    private DateTime date;
    private DateTime dateSend;
    private String protocol;
    private boolean read;
    private int status;
    private String type;
    private String subject;
    private String body;
    private String serviceCenter;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public DateTime getDateSend() {
        return dateSend;
    }

    public void setDateSend(DateTime dateSend) {
        this.dateSend = dateSend;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getServiceCenter() {
        return serviceCenter;
    }

    public void setServiceCenter(String serviceCenter) {
        this.serviceCenter = serviceCenter;
    }

    public String toString(){

        if(person.equals("Unknown")){
            person = address;
        }

        return "Name : " + person + "\n"
                + "Date : " + date + "\n\n"
                + "" + body + "\n";
    }

}
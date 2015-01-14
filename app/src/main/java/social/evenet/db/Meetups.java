package social.evenet.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import  social.evenet.model.ChatModel;

/**
 * Created by Alexander on 10.09.2014.
 */

@DatabaseTable(tableName = "meetupstable")
public class Meetups implements Parcelable {

    public static final class COL {
        public static final String ID = "Id";
        public static final String CHAT = "chat";


    }

    @DatabaseField(columnName = COL.ID, generatedId = true)
    private int meet_id;

    public int getMeet_id() {
        return meet_id;
    }

    public void setMeet_id(int meet_id) {
        this.meet_id = meet_id;
    }

    @DatabaseField(foreign = true, columnName = "chat", canBeNull = false, foreignAutoRefresh = true)
    private ChatModel chatModel;

    public ChatModel getChatModel() {
        return chatModel;
    }

    public void setChatModel(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public int getMeetup_id() {
        return meetup_id;
    }

    public void setMeetup_id(int meetup_id) {
        this.meetup_id = meetup_id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getMeetup_name() {
        return meetup_name;
    }

    public void setMeetup_name(String meetup_name) {
        this.meetup_name = meetup_name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHas_unread_messages() {
        return has_unread_messages;
    }

    public void setHas_unread_messages(int has_unread_messages) {
        this.has_unread_messages = has_unread_messages;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(meetup_id);
        parcel.writeInt(time);
        parcel.writeInt(start);
        parcel.writeInt(end);
        parcel.writeString(meetup_name);
        parcel.writeInt(status);
        parcel.writeInt(has_unread_messages);
        parcel.writeParcelable(event, i);
        parcel.writeInt(point);
        parcel.writeParcelable(chatModel,i);
        parcel.writeParcelable(place,i);
        parcel.writeInt(users_count);
        parcel.writeInt(going_count);
    }

    public Meetups() {
    }

    private int meetup_id;
    private int time;
    private int start;
    private int end;
    private String meetup_name;
    private int status;
    private int has_unread_messages;
    private Event event;
    private int point;
    private int going_count;

    public int getGoing_count() {
        return going_count;
    }

    public void setGoing_count(int going_count) {
        this.going_count = going_count;
    }

    private Place place;
    private int users_count;

    public int getUsers_count() {
        return users_count;
    }

    public void setUsers_count(int users_count) {
        this.users_count = users_count;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    private void readFromParcel(Parcel in) {

        meetup_id = in.readInt();
        time = in.readInt();
        start = in.readInt();
        end = in.readInt();
        meetup_name = in.readString();
        status = in.readInt();
        has_unread_messages = in.readInt();
        event = (Event) in.readParcelable(Meetups.class.getClassLoader());
        point = in.readInt();
        chatModel=(ChatModel) in.readParcelable(Meetups.class.getClassLoader());
        place=(Place)in.readParcelable(Meetups.class.getClassLoader());
        users_count=in.readInt();
        going_count=in.readInt();
    }


    public static final Creator<Meetups> CREATOR = new Creator<Meetups>() {
        public Meetups createFromParcel(Parcel in) {
            return new Meetups(in);
        }

        public Meetups[] newArray(int size) {
            return new Meetups[size];
        }
    };

    private Meetups(Parcel in) {
        readFromParcel(in);
    }

}

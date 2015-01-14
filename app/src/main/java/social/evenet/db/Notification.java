package social.evenet.db;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alexander on 22.09.2014.
 */
public class Notification implements Parcelable {

    private int meetup_id;
    private String chat_name;
    private int time;
    private UserInfo userInfo;
    private String notification_type;
    private Place place;
    private Event event;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public int getMeetup_id() {
        return meetup_id;
    }

    public void setMeetup_id(int meetup_id) {
        this.meetup_id = meetup_id;
    }

    public String getChat_name() {
        return chat_name;
    }

    public void setChat_name(String chat_name) {
        this.chat_name = chat_name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Notification() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(meetup_id);
        dest.writeString(chat_name);
        dest.writeInt(time);
        dest.writeParcelable(userInfo, flags);
        dest.writeString(notification_type);
        dest.writeParcelable(meetups,flags);
        dest.writeParcelable(place,flags);
        dest.writeParcelable(event,flags);
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };


    private Meetups meetups;

    public Meetups getMeetups() {
        return meetups;
    }

    public void setMeetups(Meetups meetups) {
        this.meetups = meetups;
    }

    private void readFromParcel(Parcel in) {


        meetup_id = in.readInt();
        chat_name = in.readString();
        time = in.readInt();
        userInfo = (UserInfo) in.readParcelable(Notification.class.getClassLoader());
        notification_type=in.readString();
        meetups=(Meetups) in.readParcelable(Notification.class.getClassLoader());
        place=(Place)in.readParcelable(Notification.class.getClassLoader());
        event=(Event)in.readParcelable(Notification.class.getClassLoader());
    }

    private Notification(Parcel in) {
        readFromParcel(in);
    }

    public static Creator<Notification> getCreator() {
        return CREATOR;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}

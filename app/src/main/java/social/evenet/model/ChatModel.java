package social.evenet.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import social.evenet.db.Event;
import social.evenet.db.MainAttachment;
import social.evenet.db.Place;

/**
 * Created by Александр on 13.09.2014.
 */

@DatabaseTable(tableName = "chattable")
public class ChatModel implements Parcelable {



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(message);
        parcel.writeString(name);
        parcel.writeString(icon);
        parcel.writeString(expired);
        parcel.writeInt(image.length);
        parcel.writeByteArray(image);

        parcel.writeInt(image_second.length);
        parcel.writeByteArray(image_second);

        parcel.writeInt(image_third.length);
        parcel.writeByteArray(image_third);

        parcel.writeInt(image_four.length);
        parcel.writeByteArray(image_four);

        parcel.writeInt(image_five.length);
        parcel.writeByteArray(image_five);

        parcel.writeString(time);
        parcel.writeByte((byte) (send ? 1 : 0));

        parcel.writeInt(meetup_id);

        parcel.writeInt(event_id);

        parcel.writeInt(meetup_id);

        parcel.writeInt(user_id);

        parcel.writeInt(is_new);

        parcel.writeInt(is_read);

        parcel.writeInt(iknow);

        parcel.writeParcelable(attachment,i);


    }

    @DatabaseField(columnName = COL.MEET_ID)
    private int meetup_id;
    @DatabaseField(columnName = COL.EVENT_ID)
    private int event_id;

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public int getMeetup_id() {
        return meetup_id;
    }

    public void setMeetup_id(int meetup_id) {
        this.meetup_id = meetup_id;
    }

    @DatabaseField(dataType = DataType.BYTE_ARRAY,columnName = COL.UPLOAD_FIRST)
    private byte[] image;

    @DatabaseField(dataType = DataType.BYTE_ARRAY,columnName = COL.UPLOAD_SECOND)
    private byte[] image_second;

    @DatabaseField(dataType = DataType.BYTE_ARRAY,columnName = COL.UPLOAD_THIRD)
    private byte[] image_third;

    @DatabaseField(dataType = DataType.BYTE_ARRAY,columnName = COL.UPLOAD_FOUR)
    private byte[] image_four;

    @DatabaseField(dataType = DataType.BYTE_ARRAY,columnName = COL.UPLOAD_FIVE)
    private byte[] image_five;

    public byte[] getImage_second() {
        return image_second;
    }

    public void setImage_second(byte[] image_second) {
        this.image_second = image_second;
    }

    public byte[] getImage_third() {
        return image_third;
    }

    public void setImage_third(byte[] image_third) {
        this.image_third = image_third;
    }

    public byte[] getImage_five() {
        return image_five;
    }

    public void setImage_five(byte[] image_five) {
        this.image_five = image_five;
    }

    public byte[] getImage_four() {
        return image_four;
    }

    public void setImage_four(byte[] image_four) {
        this.image_four = image_four;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    private void readFromParcel(Parcel in) {

        id = in.readInt();

        message = in.readString();

        name = in.readString();

        icon = in.readString();

        expired = in.readString();

        image = new byte[in.readInt()];

        in.readByteArray(image);

        image_second = new byte[in.readInt()];

        in.readByteArray(image_second);

        image_third = new byte[in.readInt()];

        in.readByteArray(image_third);

        image_four = new byte[in.readInt()];

        in.readByteArray(image_four);

        image_five = new byte[in.readInt()];

        in.readByteArray(image_five);

        time = in.readString();

        send = in.readByte() != 0;

        meetup_id=in.readInt();

        event_id=in.readInt();

        meetup_id=in.readInt();

        user_id=in.readInt();

        is_new=in.readInt();

        is_read=in.readInt();

        iknow=in.readInt();

        attachment=(MainAttachment)in.readParcelable(ChatModel.class.getClassLoader());
    }

    public static final class COL {
        public static final String ID = "Id";
        public static final String MESSAGE = "message";
        public static final String NAME = "name";
        public static final String TIME = "time";
        public static final String ICON_USER = "user_url";
        public static final String UPLOAD_FIRST = "upload_first";
        public static final String UPLOAD_SECOND = "upload_second";
        public static final String UPLOAD_THIRD = "upload_third";
        public static final String UPLOAD_FOUR = "upload_four";
        public static final String UPLOAD_FIVE = "upload_five";
        public static final String EXPIRED = "day";
        public static final String SEND = "send";
        public static final String MEET_ID = "meet_id";
        public static final String EVENT_ID = "event_id";
        public static final String MESSAGE_ID = "message_id";
        public static final String USER_ID = "user_id";


    }

    @DatabaseField(columnName = COL.ID, generatedId = true)
    private int id;

    private Place place;

    private Event event;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    @DatabaseField(columnName = COL.MESSAGE)
    private String message;

    @DatabaseField(columnName = COL.NAME)
    private String name;
    @DatabaseField(columnName = COL.ICON_USER)
    private String icon;

    @DatabaseField(columnName = COL.EXPIRED)
    private String expired;



    @DatabaseField(columnName = COL.TIME)
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public static final Creator<ChatModel> CREATOR = new Creator<ChatModel>() {
        public ChatModel createFromParcel(Parcel in) {
            return new ChatModel(in);
        }

        public ChatModel[] newArray(int size) {
            return new ChatModel[size];
        }
    };

    private ChatModel(Parcel in) {
        readFromParcel(in);
    }

    public ChatModel() {
    }


    @DatabaseField(columnName = COL.SEND)
    private Boolean send;

    public Boolean getSend() {
        return send;
    }

    public void setSend(Boolean send) {
        this.send = send;
    }

    @DatabaseField(columnName = COL.MESSAGE_ID)
    private int message_id;

    @DatabaseField(columnName = COL.USER_ID)
    private int user_id;

    private int is_new;

    private int is_read;

    private int iknow;

    private MainAttachment attachment;

    public MainAttachment getAttachment() {
        return attachment;
    }

    public void setAttachment(MainAttachment attachment) {
        this.attachment = attachment;
    }

    public int getIknow() {
        return iknow;
    }

    public void setIknow(int iknow) {
        this.iknow = iknow;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public int getIs_new() {
        return is_new;
    }

    public void setIs_new(int is_new) {
        this.is_new = is_new;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }
}



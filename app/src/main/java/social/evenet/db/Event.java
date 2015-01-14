package social.evenet.db;

import android.os.Parcel;
import android.os.Parcelable;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import social.evenet.model.ChatModel;

/**
 * Created by Александр on 07.09.2014.
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = "event")
public class Event implements Parcelable {

    public static final class COL {
        public static final String id = "id";
        public static final String ID = "event_id";
        public static final String EVENT_TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String BEGINS = "begins";
        public static final String ENDS = "event_end";
        public static final String CREATOR = "event_creator";

    }


    @DatabaseField(columnName = COL.id, generatedId = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private UserInfo creator;

    public UserInfo getCreator() {
        return creator;
    }

    public void setCreator(UserInfo creator) {
        this.creator = creator;
    }

    @DatabaseField(columnName = COL.ID)
    private int event_id;
    @DatabaseField(columnName = COL.EVENT_TITLE)
    private String event_title;
    @DatabaseField(columnName = COL.DESCRIPTION)
    private String event_description;

    private int lang_id;

    @DatabaseField(columnName = COL.BEGINS)
    private String begins;
    @DatabaseField(columnName = COL.ENDS)
    private String ends;

    @DatabaseField(foreign = true, columnName = "mainAttachment", canBeNull = false,foreignAutoCreate = true)
    private MainAttachment mainAttachment;

    @DatabaseField(foreign = true, columnName = "company", canBeNull = false,foreignAutoCreate = true)
    private Company company;

    @DatabaseField(foreign = true, columnName = "place", canBeNull = false,foreignAutoCreate = true)
    private Place place;

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public int getLang_id() {
        return lang_id;
    }

    public void setLang_id(int lang_id) {
        this.lang_id = lang_id;
    }

    public String getBegins() {
        return begins;
    }

    public void setBegins(String begins) {
        this.begins = begins;
    }

    public String getEnds() {
        return ends;
    }

    public void setEnds(String ends) {
        this.ends = ends;
    }

    public MainAttachment getMainAttachment() {
        return mainAttachment;
    }

    public void setMainAttachment(MainAttachment mainAttachment) {
        this.mainAttachment = mainAttachment;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    private ChatModel message;

    public ChatModel getMessage() {
        return message;
    }

    public void setMessage(ChatModel message) {
        this.message = message;
    }

    private Event(Parcel in) {
        readFromParcel(in);
    }


    public Event(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {


        parcel.writeInt(event_id);
        parcel.writeString(event_title);
        parcel.writeString(event_description);
        parcel.writeInt(lang_id);
        parcel.writeString(begins);
        parcel.writeString(ends);
        parcel.writeParcelable(mainAttachment,i);
        parcel.writeParcelable(company,i);
        parcel.writeParcelable(place,i);
        parcel.writeParcelable(creator,i);
        parcel.writeParcelable(message,i);

    }

    private void readFromParcel(Parcel in) {

        event_id = in.readInt();
        event_title=in.readString();
        event_description=in.readString();
        lang_id=in.readInt();
        begins=in.readString();
        ends=in.readString();
        mainAttachment = (MainAttachment)in.readParcelable(Event.class.getClassLoader());
        company = (Company)in.readParcelable(Event.class.getClassLoader());
        place=(Place)in.readParcelable(Event.class.getClassLoader());
        creator=(UserInfo)in.readParcelable(Event.class.getClassLoader());
        message=(ChatModel)in.readParcelable(Event.class.getClassLoader());
    }




    public static final Creator<Event> CREATOR = new Creator<Event>() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

}

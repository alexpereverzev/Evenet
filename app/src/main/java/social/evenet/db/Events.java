package social.evenet.db;

import android.os.Parcel;
import android.os.Parcelable;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Александр on 07.09.2014.
 */

//@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = "events")
public class Events implements Parcelable {

    public static final class COL {
        public static final String ID = "Id";
        public static final String TEXT = "message";
        public static final String LIKES_COUNT = "likes";
        public static final String SURNAME = "surname";
        public static final String LIKES = "likes";
        public static final String LATITUDE = "latitude_current";
        public static final String LONGTITUDE = "longtitude_current";


    }


    @DatabaseField(columnName = COL.ID, generatedId = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @DatabaseField(foreign = true, columnName = "event", canBeNull = false, foreignAutoRefresh = true)
    private Event event;

    @DatabaseField(columnName = COL.LIKES_COUNT)
    private int likes_count;

    private int dislikes_count;

    private int reposts_count;

    private int comments_count;

    private int liked_by_you;

    private int respost_by_you;

    private int checkins_count;

    public int getCheckins_count() {
        return checkins_count;
    }

    public void setCheckins_count(int checkins_count) {
        this.checkins_count = checkins_count;
    }

    private int disliked_by_you;

    @DatabaseField(columnName = COL.LONGTITUDE)
    private double current_coordinat_longutide;

    @DatabaseField(columnName = COL.LATITUDE)
    private double current_coordinat_latitude;

    public double getCurrent_coordinat_longutide() {
        return current_coordinat_longutide;
    }

    public void setCurrent_coordinat_longutide(double current_coordinat_longutide) {
        this.current_coordinat_longutide = current_coordinat_longutide;
    }

    public double getCurrent_coordinat_latitude() {
        return current_coordinat_latitude;
    }

    public void setCurrent_coordinat_latitude(double current_coordinat_latitude) {
        this.current_coordinat_latitude = current_coordinat_latitude;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public int getDislikes_count() {
        return dislikes_count;
    }

    public void setDislikes_count(int dislikes_count) {
        this.dislikes_count = dislikes_count;
    }

    public int getReposts_count() {
        return reposts_count;
    }

    public void setReposts_count(int reposts_count) {
        this.reposts_count = reposts_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getLiked_by_you() {
        return liked_by_you;
    }

    public void setLiked_by_you(int liked_by_you) {
        this.liked_by_you = liked_by_you;
    }

    public int getRespost_by_you() {
        return respost_by_you;
    }

    public void setRespost_by_you(int respost_by_you) {
        this.respost_by_you = respost_by_you;
    }

    public int getDisliked_by_you() {
        return disliked_by_you;
    }

    public void setDisliked_by_you(int disliked_by_you) {
        this.disliked_by_you = disliked_by_you;
    }

    public static final Creator<Events> CREATOR = new Creator<Events>() {
        public Events createFromParcel(Parcel in) {
            return new Events(in);
        }

        public Events[] newArray(int size) {
            return new Events[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(event, i);
        parcel.writeInt(likes_count);
        parcel.writeInt(dislikes_count);
        parcel.writeInt(comments_count);
        parcel.writeInt(liked_by_you);
        parcel.writeInt(reposts_count);
        parcel.writeInt(disliked_by_you);
        parcel.writeDouble(current_coordinat_latitude);
        parcel.writeDouble(current_coordinat_longutide);
        parcel.writeInt(checkins_count);
    }

    public Events() {

    }

    private Events(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        event = (Event) in.readParcelable(Events.class.getClassLoader());
        likes_count = in.readInt();
        dislikes_count = in.readInt();
        comments_count = in.readInt();
        liked_by_you = in.readInt();
        reposts_count = in.readInt();
        disliked_by_you = in.readInt();
        current_coordinat_latitude = in.readDouble();
        current_coordinat_longutide = in.readDouble();
        checkins_count = in.readInt();

    }

}

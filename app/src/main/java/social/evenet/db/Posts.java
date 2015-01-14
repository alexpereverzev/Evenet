package social.evenet.db;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alexander on 10.09.2014.
 */
public class Posts implements Parcelable {


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(checkin_id);
        parcel.writeInt(time);
        parcel.writeString(message);
        parcel.writeInt(likes_count);
        parcel.writeInt(dislikes_count);
        parcel.writeInt(reposts_count);
        parcel.writeInt(comments_count);
        parcel.writeInt(liked_by_you);
        parcel.writeInt(reposted_by_you);
        parcel.writeInt(disliked_by_you);
        parcel.writeParcelable(event,i);
        parcel.writeInt(date);
        parcel.writeString(type);
    }


    private int date;

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    private int checkin_id;
    private int time;
    private String message;
    private int likes_count;
    private int dislikes_count;
    private int reposts_count;
    private int comments_count;
    private int liked_by_you;
    private int reposted_by_you;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private int disliked_by_you;
    private Event event;


    public int getDisliked_by_you() {
        return disliked_by_you;
    }

    public void setDisliked_by_you(int disliked_by_you) {
        this.disliked_by_you = disliked_by_you;
    }

    public int getRepost_id() {
        return checkin_id;
    }

    public void setRepost_id(int repost_id) {
        this.checkin_id = repost_id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public int getReposted_by_you() {
        return reposted_by_you;
    }

    public void setReposted_by_you(int reposted_by_you) {
        this.reposted_by_you = reposted_by_you;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }




    private void readFromParcel(Parcel in) {

        checkin_id = in.readInt();
        time = in.readInt();
        message = in.readString();
        likes_count = in.readInt();
        dislikes_count = in.readInt();
        reposts_count = in.readInt();
        comments_count = in.readInt();
        liked_by_you = in.readInt();
        reposted_by_you = in.readInt();
        disliked_by_you = in.readInt();
        event = (Event)in.readParcelable(Posts.class.getClassLoader());
        date=in.readInt();
        type=in.readString();

    }

    public Posts(){

    }

    public static final Creator<Posts> CREATOR = new Creator<Posts>() {
        public Posts createFromParcel(Parcel in) {
            return new Posts(in);
        }

        public Posts[] newArray(int size) {
            return new Posts[size];
        }
    };

    private Posts(Parcel in) {
        readFromParcel(in);
    }

    private String name_user;
    private String url_user;

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getUrl_user() {
        return url_user;
    }

    public void setUrl_user(String url_user) {
        this.url_user = url_user;
    }
}

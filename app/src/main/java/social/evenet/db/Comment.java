package social.evenet.db;

import android.os.Parcel;
import android.os.Parcelable;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Alexander on 15.09.2014.
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment implements Parcelable {

    private String name;
    private String icon;
    private String comment;
    private int time;
    private int comment_id;
    private int likes_count;
    private int liked_by_you;
    private UserInfo user;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public int getLiked_by_you() {
        return liked_by_you;
    }

    public void setLiked_by_you(int liked_by_you) {
        this.liked_by_you = liked_by_you;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int describeContents() {
        return 0;
    }



    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(comment_id);
        parcel.writeString(comment);
        parcel.writeInt(time);
        parcel.writeInt(likes_count);
        parcel.writeInt(liked_by_you);
        parcel.writeParcelable(user, flags);
    }


    private void readFromParcel(Parcel in) {

        comment_id = in.readInt();

        comment = in.readString();

        time = in.readInt();

        likes_count = in.readInt();
        liked_by_you = in.readInt();
        user=(UserInfo)in.readParcelable(Comment.class.getClassLoader());

    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    private Comment(Parcel in) {
        readFromParcel(in);
    }

    public Comment(){}
}

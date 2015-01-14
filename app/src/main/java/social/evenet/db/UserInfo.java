package social.evenet.db;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Александр on 06.09.2014.
 */
public class UserInfo implements Parcelable {

    private String name;
    private String surname;

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getFollowing_count() {
        return following_count;
    }

    public void setFollowing_count(int following_count) {
        this.following_count = following_count;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public int getFeedback_notification_count() {
        return feedback_notification_count;
    }

    public void setFeedback_notification_count(int feedback_notification_count) {
        this.feedback_notification_count = feedback_notification_count;
    }

    public String getPhoto_small() {
        return photo_small;
    }

    public void setPhoto_small(String photo_small) {
        this.photo_small = photo_small;
    }

    public String getB_day() {
        return b_day;
    }

    public void setB_day(String b_day) {
        this.b_day = b_day;
    }

    public String getTags_in_text() {
        return tags_in_text;
    }

    public void setTags_in_text(String tags_in_text) {
        this.tags_in_text = tags_in_text;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public int getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(int phone_number) {
        this.phone_number = phone_number;
    }

    public String getNative_lang() {
        return native_lang;
    }

    public void setNative_lang(String native_lang) {
        this.native_lang = native_lang;
    }

    public String getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(String last_seen) {
        this.last_seen = last_seen;
    }

    public int getEvent_created() {
        return event_created;
    }

    public void setEvent_created(int event_created) {
        this.event_created = event_created;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHome_city() {
        return home_city;
    }

    public void setHome_city(String home_city) {
        this.home_city = home_city;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEducation_level_id() {
        return education_level_id;
    }

    public void setEducation_level_id(String education_level_id) {
        this.education_level_id = education_level_id;
    }

    public String getPhoto_mid() {
        return photo_mid;
    }

    public void setPhoto_mid(String photo_mid) {
        this.photo_mid = photo_mid;
    }

    public String getPhoto_big() {
        return photo_big;
    }

    public void setPhoto_big(String photo_big) {
        this.photo_big = photo_big;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    private int user_id;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    private String nikname;

    public String getNikname() {
        return nikname;
    }

    public void setNikname(String nikname) {
        this.nikname = nikname;
    }

    private int followers_count;
    private int following_count;
    private int friends_count;
    private int feedback_notification_count;
    private String photo_small;
    private String b_day;
    private String tags_in_text;
    private int reputation;
    private int phone_number;
    private String native_lang;
    private String last_seen;
    private int event_created;
    private String gender;
    private String home_city;
    private String bio;
    private String education_level_id;
    private String photo_mid;
    private String photo_big;
    private int relation;

    private String email;

    private int common_meetups;
    private int places_in_lists;
    private int reviews_count;

    public int getPlaces_in_lists() {
        return places_in_lists;
    }

    public void setPlaces_in_lists(int places_in_lists) {
        this.places_in_lists = places_in_lists;
    }

    public int getReviews_count() {
        return reviews_count;
    }

    public void setReviews_count(int reviews_count) {
        this.reviews_count = reviews_count;
    }

    public int getCommon_meetups() {
        return common_meetups;
    }

    public void setCommon_meetups(int common_meetups) {
        this.common_meetups = common_meetups;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(followers_count);
        parcel.writeInt(following_count);
        parcel.writeInt(friends_count);
        parcel.writeInt(feedback_notification_count);
        parcel.writeString(photo_small);
        parcel.writeString(b_day);
        parcel.writeString(tags_in_text);
        parcel.writeInt(reputation);
        parcel.writeInt(phone_number);
        parcel.writeString(native_lang);
        parcel.writeString(last_seen);
        parcel.writeInt(event_created);
        parcel.writeString(gender);
        parcel.writeString(home_city);
        parcel.writeString(bio);
        parcel.writeString(education_level_id);
        parcel.writeString(photo_mid);
        parcel.writeString(photo_big);
        parcel.writeInt(relation);
        parcel.writeString(name);
        parcel.writeString(surname);
        parcel.writeInt(user_id);
        parcel.writeString(email);
        parcel.writeInt(status);
        parcel.writeInt(common_meetups);
        parcel.writeInt(places_in_lists);
        parcel.writeInt(reviews_count);
        parcel.writeString(nikname);

    }


    private void readFromParcel(Parcel in) {

        followers_count = in.readInt();
        following_count = in.readInt();
        friends_count = in.readInt();
        feedback_notification_count = in.readInt();
        photo_small = in.readString();
        b_day = in.readString();
        tags_in_text = in.readString();
        reputation = in.readInt();
        phone_number = in.readInt();
        native_lang = in.readString();
        last_seen = in.readString();
        event_created = in.readInt();
        gender = in.readString();
        home_city = in.readString();
        bio = in.readString();
        education_level_id = in.readString();
        photo_mid = in.readString();
        photo_big = in.readString();
        relation = in.readInt();
        name = in.readString();
        surname = in.readString();
        user_id=in.readInt();
        email=in.readString();
        status=in.readInt();
        common_meetups=in.readInt();
        places_in_lists=in.readInt();
        reviews_count=in.readInt();
        nikname=in.readString();

    }

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserInfo() {

    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        public UserInfo[] newArray(int size) {

            return new UserInfo[size];
        }
    };

    private UserInfo(Parcel in) {
        readFromParcel(in);
    }


    private boolean check;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}

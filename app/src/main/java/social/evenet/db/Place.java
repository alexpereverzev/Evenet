package social.evenet.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Александр on 07.09.2014.
 */
@DatabaseTable(tableName = "placetable")
public class Place implements Parcelable {

    public static final class COL {
        public static final String ID = "Id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String LANG_ID = "land_id";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String ADDRESS = "address";

    }
    @DatabaseField(columnName = COL.ID, generatedId = true)
    private int place_id;
    @DatabaseField(columnName = COL.TITLE)
    private String place_title;

    private String place_category;

    private int category_id;

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getPlace_category() {
        return place_category;
    }

    public void setPlace_category(String place_category) {
        this.place_category = place_category;
    }

    @DatabaseField(columnName = COL.DESCRIPTION)
    private String place_description;
    @DatabaseField(columnName = COL.LANG_ID)
    private int lang_id;
    @DatabaseField(columnName = COL.LATITUDE)
    private double latitude;
    @DatabaseField(columnName = COL.LONGITUDE)
    private double longitude;
    @DatabaseField(columnName = COL.ADDRESS)
    private String place_address;

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    public String getPlace_title() {
        return place_title;
    }

    public void setPlace_title(String place_title) {
        this.place_title = place_title;
    }

    public String getPlace_description() {
        return place_description;
    }

    public void setPlace_description(String place_description) {
        this.place_description = place_description;
    }

    public int getLang_id() {
        return lang_id;
    }

    public void setLang_id(int lang_id) {
        this.lang_id = lang_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPlace_address() {
        return place_address;
    }

    public void setPlace_address(String place_address) {
        this.place_address = place_address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(place_id);
        parcel.writeString(place_title);
        parcel.writeString(place_description);
        parcel.writeInt(lang_id);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(place_address);
        parcel.writeString(place_category);
        parcel.writeInt(category_id);
        parcel.writeInt(friends_count);
        parcel.writeInt(place_count);
    }

    private void readFromParcel(Parcel in) {

        place_id = in.readInt();
        place_title = in.readString();
        place_description = in.readString();
        lang_id = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        place_address = in.readString();
        place_category=in.readString();
        category_id=in.readInt();
        friends_count=in.readInt();
        place_count=in.readInt();
    }

    private int friends_count;
    private int place_count;

    public int getFriends_count() {
        return friends_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public int getPlace_count() {
        return place_count;
    }

    public void setPlace_count(int place_count) {
        this.place_count = place_count;
    }

    public Place(){

    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    private Place(Parcel in) {
        readFromParcel(in);
    }


    @Override
    public String toString() {
        return place_address;
    }
}

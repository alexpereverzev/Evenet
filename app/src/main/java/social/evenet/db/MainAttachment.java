package social.evenet.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Александр on 07.09.2014.
 */
@DatabaseTable(tableName = "mainAttachmenttable")
public class MainAttachment implements Parcelable {

    public static final class COL {
        public static final String ID = "Id";
        public static final String TYPE = "type";
        public static final String URL = "url";

    }
    @DatabaseField(columnName = COL.ID, generatedId = true)
    private int id;
    @DatabaseField(columnName = COL.TYPE)
    private String type;
    @DatabaseField(columnName = COL.URL)
    private String url;

    private String small_photo;

    private String medium_photo;

    public String getMedium_photo() {
        return medium_photo;
    }

    public void setMedium_photo(String medium_photo) {
        this.medium_photo = medium_photo;
    }

    public String getSmall_photo() {
        return small_photo;
    }

    public void setSmall_photo(String small_photo) {
        this.small_photo = small_photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(type);
        parcel.writeString(url);
        parcel.writeString(small_photo);
        parcel.writeString(medium_photo);
    }

    public MainAttachment(){

    }

    private void readFromParcel(Parcel in) {

        id = in.readInt();
        type = in.readString();
        url = in.readString();
        small_photo=in.readString();
        medium_photo=in.readString();
    }


    public static final Creator<MainAttachment> CREATOR = new Creator<MainAttachment>() {
        public MainAttachment createFromParcel(Parcel in) {
            return new MainAttachment(in);
        }

        public MainAttachment[] newArray(int size) {
            return new MainAttachment[size];
        }
    };

    private MainAttachment(Parcel in) {
        readFromParcel(in);
    }
}

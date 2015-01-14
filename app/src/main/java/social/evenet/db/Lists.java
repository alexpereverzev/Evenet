package social.evenet.db;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alexander on 10.09.2014.
 */
public class Lists implements Parcelable {

    private int list_id;
    private String name;
    private String url;

    public int getList_id() {
        return list_id;
    }

    public void setList_id(int list_id) {
        this.list_id = list_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    private Lists(Parcel in) {
        readFromParcel(in);
    }


    public Lists() {
    }



    @Override
    public void writeToParcel(Parcel parcel, int i) {


        parcel.writeInt(list_id);
        parcel.writeString(name);
        parcel.writeString(url);

    }

    private void readFromParcel(Parcel in) {

        list_id = in.readInt();
        name = in.readString();
        url = in.readString();

    }


    public static final Creator<Lists> CREATOR = new Creator<Lists>() {
        public Lists createFromParcel(Parcel in) {
            return new Lists(in);
        }

        public Lists[] newArray(int size) {
            return new Lists[size];
        }
    };


}

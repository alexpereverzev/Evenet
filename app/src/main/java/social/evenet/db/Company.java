package social.evenet.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Александр on 07.09.2014.
 */

@DatabaseTable(tableName = "companytable")
public class Company implements Parcelable {


    public static final class COL {
        public static final String ID = "Id";
        public static final String COMPANY_ID = "company_id";
        public static final String NAME = "name";
        public static final String LANG_ID = "lang_id";

    }

    @DatabaseField(columnName = COL.NAME)
    private String company_name;

    @DatabaseField(columnName = COL.COMPANY_ID)
    private int company_id;

    @DatabaseField(columnName = COL.ID, generatedId = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @DatabaseField(columnName = COL.LANG_ID)
    private int lang_id;

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getLang_id() {
        return lang_id;
    }

    public void setLang_id(int lang_id) {
        this.lang_id = lang_id;
    }

    public Company(){

    }

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(company_name);
        parcel.writeInt(company_id);
        parcel.writeInt(lang_id);
        parcel.writeString(logo_small);
        parcel.writeString(website);
        parcel.writeString(logo_mid);
        parcel.writeString(social_network_FB);
        parcel.writeInt(places_count);

        parcel.writeString(social_network_VK);
        parcel.writeString(company_description);
        parcel.writeString(social_network_twitter);
        parcel.writeString(logo_big_orig);
        parcel.writeString(logo_big);
        parcel.writeString(logo_small_orig);


        parcel.writeInt(events_count);
        parcel.writeInt(rating);

        parcel.writeString(company_title);
        parcel.writeString(social_network_INSTA);
        parcel.writeString(logo_mid_orig);

    }


    private void readFromParcel(Parcel in) {

        company_name = in.readString();
        company_id = in.readInt();
        lang_id = in.readInt();
        logo_small=in.readString();
        website=in.readString();
        logo_mid=in.readString();
        social_network_FB=in.readString();
        places_count=in.readInt();
        social_network_VK=in.readString();
        company_description=in.readString();
        social_network_twitter=in.readString();
        logo_big_orig=in.readString();
        logo_big=in.readString();
        logo_small_orig=in.readString();
        events_count=in.readInt();
        rating=in.readInt();
        company_title=in.readString();
        social_network_INSTA=in.readString();
        logo_mid_orig=in.readString();

    }

    private String logo_small;
    private String website;
    private String logo_mid;
    private String social_network_FB;

    public String getLogo_small() {
        return logo_small;
    }

    public void setLogo_small(String logo_small) {
        this.logo_small = logo_small;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLogo_mid() {
        return logo_mid;
    }

    public void setLogo_mid(String logo_mid) {
        this.logo_mid = logo_mid;
    }

    public String getSocial_network_FB() {
        return social_network_FB;
    }

    public void setSocial_network_FB(String social_network_FB) {
        this.social_network_FB = social_network_FB;
    }

    public int getPlaces_count() {
        return places_count;
    }

    public void setPlaces_count(int places_count) {
        this.places_count = places_count;
    }

    public String getSocial_network_VK() {
        return social_network_VK;
    }

    public void setSocial_network_VK(String social_network_VK) {
        this.social_network_VK = social_network_VK;
    }

    public String getCompany_description() {
        return company_description;
    }

    public void setCompany_description(String company_description) {
        this.company_description = company_description;
    }

    public String getSocial_network_twitter() {
        return social_network_twitter;
    }

    public void setSocial_network_twitter(String social_network_twitter) {
        this.social_network_twitter = social_network_twitter;
    }

    public String getLogo_big_orig() {
        return logo_big_orig;
    }

    public void setLogo_big_orig(String logo_big_orig) {
        this.logo_big_orig = logo_big_orig;
    }

    public String getLogo_big() {
        return logo_big;
    }

    public void setLogo_big(String logo_big) {
        this.logo_big = logo_big;
    }

    public String getLogo_small_orig() {
        return logo_small_orig;
    }

    public void setLogo_small_orig(String logo_small_orig) {
        this.logo_small_orig = logo_small_orig;
    }

    public int getEvents_count() {
        return events_count;
    }

    public void setEvents_count(int events_count) {
        this.events_count = events_count;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getCompany_title() {
        return company_title;
    }

    public void setCompany_title(String company_title) {
        this.company_title = company_title;
    }

    public String getSocial_network_INSTA() {
        return social_network_INSTA;
    }

    public void setSocial_network_INSTA(String social_network_INSTA) {
        this.social_network_INSTA = social_network_INSTA;
    }

    public String getLogo_mid_orig() {
        return logo_mid_orig;
    }

    public void setLogo_mid_orig(String logo_mid_orig) {
        this.logo_mid_orig = logo_mid_orig;
    }

    public static Creator<Company> getCreator() {
        return CREATOR;
    }

    private int places_count;
    private String social_network_VK;
    private String company_description;
    private String social_network_twitter;
    private String logo_big_orig;
    private String logo_big;
    private String logo_small_orig;
    private int events_count;
    private int rating;
    private String company_title;
    private String social_network_INSTA;
    private String logo_mid_orig;


    public static final Creator<Company> CREATOR = new Creator<Company>() {
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        public Company[] newArray(int size) {
            return new Company[size];
        }
    };

    private Company(Parcel in) {
        readFromParcel(in);
    }

}

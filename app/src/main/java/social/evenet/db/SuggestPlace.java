package social.evenet.db;

/**
 * Created by Alexander on 01.12.2014.
 */
public class SuggestPlace {

    private String title;
    private int icon;

    public SuggestPlace(String title, int icon){
        this.title=title;
        this.icon=icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}

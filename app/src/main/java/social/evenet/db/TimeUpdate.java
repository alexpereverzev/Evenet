package social.evenet.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Александр on 20.09.2014.
 */
@DatabaseTable(tableName = "update")
public class TimeUpdate {

    public static final class COL {
        public static final String ID = "id";
        public static final String CURRENT_DATE="current_date";



    }

    @DatabaseField(columnName = COL.ID)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @DatabaseField(columnName = COL.CURRENT_DATE)
    private String current_date;

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
        this.current_date = current_date;
    }
}

package social.evenet.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import  social.evenet.db.Company;
import  social.evenet.db.Event;
import  social.evenet.db.Events;
import  social.evenet.db.MainAttachment;
import  social.evenet.db.Place;
import  social.evenet.db.TimeUpdate;

/**
 * Created by Александр on 13.09.2014.
 */

public class EventsDBHelper extends OrmLiteSqliteOpenHelper {


    private static final String DATABASE_NAME = "EventsDB.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    public EventsDBHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // the DAO object we use to access the SimpleData table
    private Dao<Events, Integer> EventsModelDao = null;

    @Override
    public void onCreate(final SQLiteDatabase db, final ConnectionSource connectionSource) {
        try {

            TableUtils.createTable(connectionSource, Company.class);
            TableUtils.createTable(connectionSource, MainAttachment.class);
            TableUtils.createTable(connectionSource, Events.class);
            TableUtils.createTable(connectionSource, Event.class);
            TableUtils.createTable(connectionSource, TimeUpdate.class);
            TableUtils.createTable(connectionSource, Place.class);

        }
        catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust the various data to
     * match the new version number.
     */
    @Override
    public void onUpgrade(final SQLiteDatabase db, final ConnectionSource connectionSource, final int oldVersion, final int newVersion) {
        try {


            TableUtils.dropTable(connectionSource, Events.class, true);
            TableUtils.dropTable(connectionSource, Event.class, true);
            TableUtils.dropTable(connectionSource, Company.class, true);
            TableUtils.dropTable(connectionSource, MainAttachment.class, true);
            TableUtils.dropTable(connectionSource, Place.class, true);
            TableUtils.dropTable(connectionSource, TimeUpdate.class, true);

            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        }
        catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Events, Integer> getDateModelDao() throws java.sql.SQLException {
        if (this.EventsModelDao == null) {
            this.EventsModelDao = getDao(Events.class);
        }
        return this.EventsModelDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        this.EventsModelDao = null;

    }
}

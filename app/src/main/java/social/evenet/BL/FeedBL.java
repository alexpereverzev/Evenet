package social.evenet.BL;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import social.evenet.db.Company;
import social.evenet.db.Event;
import social.evenet.db.Events;
import social.evenet.db.MainAttachment;
import social.evenet.db.Place;
import social.evenet.db.TimeUpdate;
import social.evenet.helper.Util;
import social.evenet.model.EventsDBHelper;

//import org.joda.time.DateTime;

/**
 * Created by Alexander on 18.09.2014.
 */
public class FeedBL {

    private Context mContext;

    private EventsDBHelper databaseHelper;

    private Events events;

    public FeedBL(Context context) {
        mContext = context;
        databaseHelper=new EventsDBHelper(mContext);

    }

    public void saveEvents(Events e){

        databaseHelper=new EventsDBHelper(mContext);


        try {
            Dao<Events, Integer> dao = databaseHelper.getDao(Events.class);
            Dao<Event, Integer> dao_event = databaseHelper.getDao(Event.class);
            Dao<Company, Integer> dao_company = databaseHelper.getDao(Company.class);
            Dao<Place, Integer> dao_place = databaseHelper.getDao(Place.class);
            Dao<MainAttachment, Integer> dao_mainattachemnt = databaseHelper.getDao(MainAttachment.class);

            Event event=e.getEvent();
            Place place=event.getPlace();
            Company company=event.getCompany();
            MainAttachment mainAttachment=event.getMainAttachment();
            dao_company.create(company);
            dao_mainattachemnt.create(mainAttachment);
            dao_place.create(place);
            dao_event.create(event);

            dao.create(e);
            databaseHelper.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public void setTime(){
        databaseHelper=new EventsDBHelper(mContext);
        try {
            Dao<TimeUpdate, Integer> dao = databaseHelper.getDao(TimeUpdate.class);
            TimeUpdate t=new TimeUpdate();
            t.setId(1);



            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            System.out.println(dateFormat.format(cal.getTime()));


            String s= dateFormat.format(cal.getTime()).toString();
            t.setCurrent_date(s);
            dao.create(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void clearTable(){
        databaseHelper=new EventsDBHelper(mContext);
        try {
            TableUtils.clearTable(databaseHelper.getConnectionSource(), Events.class);
            TableUtils.clearTable(databaseHelper.getConnectionSource(), Event.class);
            TableUtils.clearTable(databaseHelper.getConnectionSource(), Company.class);
            TableUtils.clearTable(databaseHelper.getConnectionSource(), Place.class);
            TableUtils.clearTable(databaseHelper.getConnectionSource(), MainAttachment.class);
            TableUtils.clearTable(databaseHelper.getConnectionSource(), TimeUpdate.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean checkUpdate(){
        databaseHelper=new EventsDBHelper(mContext);
        try {
            Dao<TimeUpdate, Integer> dao = databaseHelper.getDao(TimeUpdate.class);
            TimeUpdate t=dao.queryForAll().get(0);
          String result=  Util.checkCacheDB(t.getCurrent_date().toString());
            if(result.contains("23 hours"));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean   checkId(Event search) {
        databaseHelper = new EventsDBHelper(mContext);
        Dao<Event, Integer> dao = null;
        Where<Events, Integer> where = null;
        try {
            dao=databaseHelper.getDao(Event.class);
            QueryBuilder<Event, Integer> qb = dao.queryBuilder();
            qb.where().eq("event_id", search.getEvent_id());

            PreparedQuery<Event> pq = qb.prepare();
            List<Event> li= dao.query(pq);
            if(li.size()==0){
                return true;
            }
            else return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
            return false;
    }

    public boolean checkSave(Events e){
        databaseHelper=new EventsDBHelper(mContext);
        String request="SELECT * FROM news ORDER BY id DESC";
        try {
            Dao<Event, Integer> dao_event = databaseHelper.getDao(Event.class);
        List<Event> list=dao_event.queryForAll();
        if(list.size()==0){
            return false;
        }

         if(e.getEvent().getEvent_id()==list.get(list.size()-1).getEvent_id()){
           databaseHelper.close();
            return true;
        }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        databaseHelper.close();
        return false;
    }


    public void saveListEvents(List<Events> e){
        databaseHelper = new EventsDBHelper(mContext);

        try {
            Dao<Events, Integer> dao = databaseHelper.getDao(Events.class);
            Dao<Event, Integer> dao_event = databaseHelper.getDao(Event.class);
            Dao<Company, Integer> dao_company = databaseHelper.getDao(Company.class);
            Dao<Place, Integer> dao_place = databaseHelper.getDao(Place.class);
            Dao<MainAttachment, Integer> dao_mainattachemnt = databaseHelper.getDao(MainAttachment.class);
            Iterator<Events> iterator=e.iterator();
            while (iterator.hasNext()){
                Events events=iterator.next();
                Event event=events.getEvent();
                Place place=event.getPlace();
                Company company=event.getCompany();
                MainAttachment mainAttachment=event.getMainAttachment();
                dao_company.create(company);
                dao_mainattachemnt.create(mainAttachment);
                dao_place.create(place);
                dao_event.create(event);
                dao.create(events);

            }
            databaseHelper.close();

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public List<Event>   searchItem(String search) {
       databaseHelper = new EventsDBHelper(mContext);
        Dao<Event, Integer> dao = null;
        Where<Events, Integer> where = null;
        try {
            dao=databaseHelper.getDao(Event.class);
            QueryBuilder<Event, Integer> qb = dao.queryBuilder();
            qb.where().like("title", "%" + search+ "%");

            PreparedQuery<Event> pq = qb.prepare();
            return dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Event getChat(int meet_id) {
        databaseHelper = new EventsDBHelper(mContext);

        Event result = new Event();
        try {
            Dao<Event, Integer> dao = databaseHelper.getDao(Event.class);
            QueryBuilder<Event, Integer> qb = dao.queryBuilder();
            qb.where().eq("event_id", meet_id);

            PreparedQuery<Event> pq = qb.prepare();
            return dao.query(pq).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        databaseHelper.close();
        return result;
    }

    public List<Events>   searchItemEvents(String search) {
        databaseHelper = new EventsDBHelper(mContext);
        Dao<Events, Integer> dao = null;
        Dao<Event, Integer> dao_event = null;
        Dao<Company, Integer> dao_company = null;


        Where<Events, Integer> where = null;
        try {
            dao=databaseHelper.getDao(Events.class);
            dao_event=databaseHelper.getDao(Event.class);
            dao_company=databaseHelper.getDao(Company.class);

            QueryBuilder<Event, Integer> phoneQb = dao_event.queryBuilder();
            QueryBuilder<Company, Integer> companydb = dao_company.queryBuilder();
            companydb.where().like("name", "%" + search + "%");
            phoneQb.where().like("title", "%" + search + "%").or().like("description", "%" + search + "%");

            QueryBuilder<Events, Integer> qb = dao.queryBuilder();
           // qb.where().like("title", "%" + search+ "%");
          //  phoneQb.join(companydb);
         //   QueryBuilder<Event, Integer> eventQb = dao_event.queryBuilder();
          //  eventQb.leftJoin((companydb));

            PreparedQuery<Events> pq = qb.leftJoin(phoneQb).prepare();

            return dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Events> getEvents() {
        databaseHelper=new EventsDBHelper(mContext);
        List<Events> result = new ArrayList<Events>();
        try {
            EventsDBHelper databaseHelper =new  EventsDBHelper(mContext);
            return databaseHelper.getDao(Events.class).queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        databaseHelper.close();
        return result;
    }

    public List<Event> getEvent() {
        databaseHelper=new EventsDBHelper(mContext);

        List<Event> result = new ArrayList<Event>();
        try {
            EventsDBHelper databaseHelper =new  EventsDBHelper(mContext);
            return databaseHelper.getDao(Event.class).queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        databaseHelper.close();
        return result;
    }

    public void deleteItem(){
        databaseHelper=new EventsDBHelper(mContext);

        try {
            Dao<Events, Integer> dao = databaseHelper.getDao(Events.class);
            List list = dao.queryForAll();
            dao.delete(list);
            databaseHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }






}

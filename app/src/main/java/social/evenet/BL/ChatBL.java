package social.evenet.BL;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import social.evenet.db.Company;
import social.evenet.model.ChatDBHelper;
import social.evenet.model.ChatModel;

/**
 * Created by Александр on 27.09.2014.
 */
public class ChatBL {

    private Context mContext;

    private ChatDBHelper databaseHelper;

    public ChatBL(Context context) {
        mContext = context;
        databaseHelper = new ChatDBHelper(mContext);
    }

    public List<ChatModel> getChat() {
        databaseHelper = new ChatDBHelper(mContext);

        List<ChatModel> result = new ArrayList<ChatModel>();
        try {

            return databaseHelper.getDao(ChatModel.class).queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        databaseHelper.close();
        return result;
    }

    public List<ChatModel> getChat(int meet_id) {
        databaseHelper = new ChatDBHelper(mContext);

        List<ChatModel> result = new ArrayList<ChatModel>();
        try {
            Dao<ChatModel, Integer> dao = databaseHelper.getDateModelDao();
            QueryBuilder<ChatModel, Integer> qb = dao.queryBuilder();
            qb.where().eq("meet_id", meet_id);

            PreparedQuery<ChatModel> pq = qb.prepare();
            return dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        databaseHelper.close();
        return result;
    }


    public void deleteItem() {
        databaseHelper = new ChatDBHelper(mContext);

        try {
            Dao<ChatModel, Integer> dao = databaseHelper.getDao(ChatModel.class);
            List list = dao.queryForAll();
            dao.delete(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteItem(int meet_id) {
        databaseHelper = new ChatDBHelper(mContext);

        try {
            Dao<ChatModel, Integer> dao = databaseHelper.getDao(ChatModel.class);
            List list = null;

            QueryBuilder<ChatModel, Integer> qb = dao.queryBuilder();
            qb.where().eq("meet_id", meet_id);

            PreparedQuery<ChatModel> pq = qb.prepare();
            list = dao.query(pq);

            dao.delete(list);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void saveChatItem(ChatModel model) {
        databaseHelper = new ChatDBHelper(mContext);
        try {
            Dao<ChatModel, Integer> dao = databaseHelper.getDateModelDao();
            dao.create(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

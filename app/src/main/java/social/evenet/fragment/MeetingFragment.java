package social.evenet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commonsware.cwac.merge.MergeAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import social.evenet.R;
import social.evenet.activity.App;
import social.evenet.activity.MeetupDetailActivity;
import social.evenet.adapter.MeetAdapter;
import social.evenet.db.Event;
import social.evenet.db.MainAttachment;
import social.evenet.db.Meetups;
import social.evenet.db.Place;
import social.evenet.db.UserInfo;
import social.evenet.helper.BaseSwipeListViewListener;
import social.evenet.helper.SwipeListView;
import social.evenet.model.ChatModel;


public class MeetingFragment extends RefreshableFragment {


    private ArrayList<Meetups> item;
    private ArrayList<Meetups> item_past;
    public static MergeAdapter mergeAdapter;
    private MeetAdapter adapter;
    private MeetAdapter past_adapter;
    private String[] param;
    public static SwipeListView mSwipeList;
    private static int openItem = -1;
    private static int lastOpenedItem = -1;
    private int lastClosedItem = -1;

    private Intent intent;

    public static MeetingFragment newInstance(String param1, String param2) {
        MeetingFragment fragment = new MeetingFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public MeetingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        intent = new Intent(getActivity(), MeetupDetailActivity.class);
        View v = inflater.inflate(R.layout.fragment_meeting, container, false);

        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("page_name ", "meetings");
        // ParseAnalytics.trackEvent("Page View", dimensions);
        mSwipeList = (SwipeListView) v.findViewById(R.id.list_meetup);
        mergeAdapter = new MergeAdapter();
        // list_meeting = (ListView) v.findViewById(R.id.list_meeting);

        mSwipeList.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
                lastOpenedItem = position;
                if (openItem > -1 && lastOpenedItem != lastClosedItem) {
                    mSwipeList.closeAnimate(openItem);
                }

                openItem = position;

            }

            @Override
            public void onClosed(int position, boolean fromRight) {
            }

            @Override
            public void onListChanged() {
            }

            @Override
            public void onMove(int position, float x) {

            }

            @Override
            public void onStartOpen(int position, int action, boolean right) {
                //Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));

            }

            @Override
            public void onStartClose(int position, boolean right) {
                //Log.d("swipe", String.format("onStartClose %d", position));
                lastClosedItem = position;
            }

            @Override
            public void onClickFrontView(int position) {
                //Log.d("swipe", String.format("onClickFrontView %d", position));
                if (openItem > -1 && lastOpenedItem != lastClosedItem) {
                    mSwipeList.closeAnimate(openItem);
                }
                if (position - 1 < item.size())
                    intent.putExtra("meetup_id", item.get(position - 1).getMeetup_id());
                else
                    intent.putExtra("meetup_id", item_past.get(position - item.size() - 2).getMeetup_id());
                startActivity(intent);
            }

            @Override
            public void onClickBackView(int position) {
                //Log.d("swipe", String.format("onClickBackView %d", position));
                // mListView.closeAnimate(position);

            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {

                mergeAdapter.notifyDataSetChanged();
            }

        });

        param = new String[4];
        param[0] = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE).getString("token", "");
        //param[1] = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE).getString("user_id", "");
        param[1] = "" + 0;
        param[2] = "" + 50;
        param[3] = "" + 0;

        App.getApi().userMeetup(putOption(param), new Callback<JSONArray>() {
            @Override
            public void success(JSONArray jsonArray, Response response) {
                item = parse(jsonArray);
                adapter = new MeetAdapter(getActivity(), item);
                param[1] = "1";
                App.getApi().userMeetup(putOption(param), callback);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


        return v;
    }


    private View buildLabel(String s) {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View rootView = li.inflate(R.layout.header_meetup, null);
        TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText(s);

        return rootView;
    }


    public Map<String, String> putOption(String[] param) {
        Map<String, String> options = new LinkedHashMap<>();
        options.put("access_token", param[0]);
        options.put("type", param[1]);
        options.put("count", param[2]);
        options.put("offset", param[3]);
        return options;
    }


    public ArrayList<Meetups> parse(JSONArray jsonArray) {
        ArrayList<Meetups> massive = new ArrayList<>();
        jsonArray = jsonArray.optJSONObject(0).optJSONArray("meetups");

        if (jsonArray.length() != 0) {


            for (int i = 0; i < jsonArray.length(); i++) {
                Meetups e = new Meetups();
                Place place = new Place();
                Event event = new Event();
                ChatModel chatModel = new ChatModel();
                MainAttachment attachment = new MainAttachment();
                UserInfo userInfo = new UserInfo();
                JSONObject jsonobject = jsonArray.optJSONObject(i);
                e.setMeetup_id(jsonobject.optInt("meetup_id"));
                e.setTime(jsonobject.optInt("date"));
                if (!jsonobject.isNull("start")) e.setStart(jsonobject.optInt("start"));
                if (!jsonobject.isNull("end")) e.setEnd(jsonobject.optInt("end"));
                e.setMeetup_name(jsonobject.optString("meetup_name"));
                e.setUsers_count(jsonobject.optInt("going_count"));
                e.setStatus(jsonobject.optInt("status"));
                e.setHas_unread_messages(jsonobject.optInt("has_unread_messages"));
                if (!jsonobject.isNull("point")) {
                    if (!jsonobject.optJSONObject("point").isNull("place")) {
                        place.setPlace_id(jsonobject.optJSONObject("point").optJSONObject("place").optInt("place_id"));
                        place.setPlace_title(jsonobject.optJSONObject("point").optJSONObject("place").optString("place_title"));
                        place.setPlace_address(jsonobject.optJSONObject("point").optJSONObject("place").optString("place_address"));
                    } else if (!jsonobject.optJSONObject("point").isNull("address")) {
                        place.setPlace_address(jsonobject.optJSONObject("point").optString("address"));
                    }
                }

                if (!jsonobject.isNull("message")) {
                    chatModel.setMeetup_id(jsonobject.optJSONObject("message").optInt("message_id"));
                    chatModel.setMessage(jsonobject.optJSONObject("message").optString("message"));
                    userInfo.setUser_id(jsonobject.optJSONObject("message").optInt("user_id"));
                    userInfo.setName(jsonobject.optJSONObject("message").optString("name"));
                    userInfo.setSurname(jsonobject.optJSONObject("message").optString("surname"));
                    userInfo.setPhoto_small(jsonobject.optJSONObject("message").optString("photo_small"));
                    chatModel.setTime(jsonobject.optJSONObject("message").optString("date"));

                    if (!jsonobject.optJSONObject("message").isNull("attachments")) {

                    }

                    event.setCreator(userInfo);
                    event.setMessage(chatModel);
                    e.setEvent(event);
                }
                if (!jsonobject.isNull("event")) {
                    event.setEvent_id(jsonobject.optJSONObject("event").optInt("event_id"));
                    event.setEvent_title(jsonobject.optJSONObject("event").optString("event_title"));
                    if (!jsonobject.optJSONObject("event").isNull("main_attachment")) {
                        attachment.setSmall_photo(jsonobject.optJSONObject("event").optJSONObject("main_attachment").optString("photo_small"));
                        attachment.setUrl(jsonobject.optJSONObject("event").optJSONObject("main_attachment").optString("photo_big"));
                    }
                    event.setEvent_description(jsonobject.optJSONObject("event").optString("event_description"));
                    if (!jsonobject.optJSONObject("event").isNull("event_starts")) {
                        event.setBegins(jsonobject.optJSONObject("event").optString("event_starts"));
                    }
                    if (!jsonobject.optJSONObject("event").isNull("event_ends")) {
                        event.setBegins(jsonobject.optJSONObject("event").optString("event_ends"));
                    }
                    event.setMainAttachment(attachment);
                    e.setEvent(event);
                }

                e.setPlace(place);
                massive.add(e);

            }

        }
        return massive;
    }


    private Callback<JSONArray> callback = new Callback<JSONArray>() {
        @Override
        public void success(JSONArray jsonArray, Response response) {
            item_past = parse(jsonArray);
            if (getActivity() != null) {
                past_adapter = new MeetAdapter(getActivity(), item_past);
                mergeAdapter.addView(buildLabel("Upcoming"), true);
                mergeAdapter.addAdapter(adapter);
                mergeAdapter.addView(buildLabel("Past"));
                mergeAdapter.addAdapter(past_adapter);
                mSwipeList.setAdapter(mergeAdapter);
            }
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

}

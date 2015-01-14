package social.evenet.fragment;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import social.evenet.BL.FeedBL;
import social.evenet.R;
import social.evenet.activity.App;
import social.evenet.activity.MenuNewActivity;
import social.evenet.adapter.FeedAdapterNew;
import social.evenet.db.Company;
import social.evenet.db.Event;
import social.evenet.db.Events;
import social.evenet.db.MainAttachment;
import social.evenet.db.Place;
import social.evenet.db.UserInfo;
import social.evenet.helper.Connectivity;
import social.evenet.widget.PullToRefreshListView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class FeedFragmentNew extends RefreshableFragment {


    private Handler handler;
    private List<Events> item;


    private String[] param;
    private FeedBL feedBL;
    public static FeedAdapterNew adapter;

    private MenuItem searchItem;

    private FeedBL bl;

    private Context mContext;

    public static PullToRefreshListView listView;

    private List<Events> list;

    public FeedFragmentNew() {
        // Required empty public constructor
    }

    private static int count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        location = getLocation();
        int number = getArguments().getInt("feed");
        switch (number) {
            case 0:

                break;
            case 1:
                param = new String[5];
                param[0] = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE).getString("token", "");
                if (location != null) {
                    param[1] = "" + location.getLatitude();
                    param[2] = "" + location.getLongitude();
                } else {
                    param[1] = "" + 55.72;
                    param[2] = "" + 37.61;
                }
                if (Connectivity.isConnectedFast(getActivity())) {
                    param[3] = "30";
                    param[4] = "0";
                } else {
                    param[3] = "20";
                    param[4] = "0";
                }

                App.getApi().feed(putOption(param),feedCallBack);

                break;
            case 2:
                param = new String[5];
                param[0] = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE).getString("token", "");
                if (location != null) {

                    param[1] = "" + location.getLatitude();
                    param[2] = "" + location.getLongitude();
                } else {
                    param[1] = "" + 55.72;
                    param[2] = "" + 37.61;

                }
                if (Connectivity.isConnectedFast(getActivity())) {
                    param[3] = "50";
                    param[4] = "0";
                } else {
                    param[3] = "20";
                    param[4] = "0";
                }
                App.getApi().listEvent(putOption(param),feedCallBack);

                break;
            default:
        }
    }

    public Map<String,String> putOption(String [] param){
      Map<String ,String>  options=new LinkedHashMap<>();
        options.put("access_token",param[0]);
        options.put("latitude",param[1]);
        options.put("longitude",param[2]);
        options.put("count",param[3]);
        options.put("offset",param[4]);
        return options;
    }

    public static FeedFragmentNew getFeedFragment(int a) {
        Bundle bundle = new Bundle(1);

        switch (a) {
            case 0:
                bundle.putInt("feed", 0);
                break;
            case 1:
                bundle.putInt("feed", 1);
                break;
            case 2:
                bundle.putInt("feed", 2);
                break;
            default:
        }


        FeedFragmentNew fragment = new FeedFragmentNew();

        fragment.setArguments(bundle);
        return fragment;
    }


    public static FeedFragmentNew getFeedFragment(String[] a) {
        Bundle bundle = new Bundle(1);
        bundle.putInt("feed", 2);
        bundle.putStringArray("param", a);
        //bundle.putSerializable("ekg", value);
        FeedFragmentNew fragment = new FeedFragmentNew();
        fragment.setArguments(bundle);
        return fragment;
    }

    private boolean flag = false;
    private List<Events> te;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        feedBL = new FeedBL(getActivity());
        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("page_name ", "Events Near Me Feed");

        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        listView = (PullToRefreshListView) v.findViewById(R.id.list_feed);


        listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                location = getLocation();
                // count=count+20;
                param = new String[5];
                param[0] = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE).getString("token", "");
                if (location != null) {

                    param[1] = "" + location.getLatitude();
                    param[2] = "" + location.getLongitude();
                } else {
                    param[1] = "" + 55.72;
                    param[2] = "" + 37.61;

                }
                if (Connectivity.isConnectedFast(getActivity())) {
                    param[3] = "30";
                    param[4] = "" + 0;
                } else {
                    param[3] = "20";
                    param[4] = "" + 0;
                }
                App.getApi().feed(putOption(param),feedCallBack);


            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();

                b.putParcelable("events", item.get(position - 1));
                b.putString("count", "" + 1);
                  MenuNewActivity aa = (MenuNewActivity) getActivity();
                aa.hide();
                aa.showFragment(4, FeedDetailFragment.getFeedFragment(b));
            }
        });
        boolean pauseOnScroll = true; // or true
        boolean pauseOnFling = true; // or false
        PauseOnScrollListener listener = new PauseOnScrollListener(ImageLoader.getInstance(), pauseOnScroll, pauseOnFling);
        listView.setOnScrollListener(listener);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
              List<Events> mas=  (List<Events>) msg.obj;
                if (mas.size() > 0) {
                    item=  (List<Events>) msg.obj;
                    adapter = new FeedAdapterNew(getActivity(), item);
                    listView.setAdapter(adapter);
                    listView.setSelection(1);
                    flag = false;
                }

            }
        };
        return v;
    }

    private Callback<JSONArray> feedCallBack=new Callback<JSONArray>() {
        @Override
        public void success(JSONArray jsonArray, Response response) {

            item = parseFeed(jsonArray);
            FeedBL bl = new FeedBL(getActivity());
            if (item == null) {
                count = 20;
                List<Events> it = bl.getEvents();
                item = it.subList(0, 20);
            }
            listView.onRefreshComplete();
            Iterator<Events> iterator = item.iterator();
            while (iterator.hasNext()) {
                Events e = iterator.next();
                if (bl.checkId(e.getEvent())) {
                    bl.saveEvents(e);
                }
            }
            flag = false;
            adapter = new FeedAdapterNew(getActivity(), item);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {
                    switch (i) {

                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                            Log.i("", "here scrolling is finished");

                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                            Log.i("", "its scrolling....");
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, final int totalItemCount) {
                    if ((totalItemCount > 0) && (!flag)) {
                        int lastInScreen = firstVisibleItem + visibleItemCount;
                        if (lastInScreen == totalItemCount) {
                            flag = true;
                            count = count + 30;
                            param[4]=""+count;
                           App.getApi().feed(putOption(param),new Callback<JSONArray>() {
                               @Override
                               public void success(JSONArray jsonArray, Response response) {

                                   if (jsonArray.optJSONObject(0).optJSONArray("events").length()!=0) {
                                       Message msg = new Message();
                                       msg.obj = parseFeed(jsonArray);
                                       handler.sendMessage(msg);
                                   } else {
                                       count = 0;
                                       param[4]=""+count;
                                       flag=false;
                                   }
                               }

                               @Override
                               public void failure(RetrofitError error) {

                               }
                           });
                        }
                    }
                }
            });

        }

        @Override
        public void failure(RetrofitError error) {

        }
    };


    public Location location;



    public List<Events> parseFeed(JSONArray jsonArray){
        ArrayList<Events> massive = new ArrayList<Events>();
        try {
            jsonArray = jsonArray.optJSONObject(0).optJSONArray("events");
            if(jsonArray.length()!=1){
                for (int i = 0; i < jsonArray.length(); i++) {
                    Events e = new Events();
                    e.setCurrent_coordinat_latitude(Double.parseDouble(param[1]));
                    e.setCurrent_coordinat_longutide(Double.parseDouble(param[2]));
                    Event event=new Event();
                    MainAttachment mainAttachment=new MainAttachment();
                    Company company=new Company();
                    UserInfo creator=new UserInfo();
                    Place place=new Place();
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    e.setLikes_count(jsonobject.getInt("likes_count"));
                    e.setComments_count(jsonobject.getInt("comments_count"));
                    e.setLiked_by_you(jsonobject.getInt("liked_by_you"));
                    e.setCheckins_count(jsonobject.getInt("checkins_count"));
                    JSONObject jsonevent = jsonobject.getJSONObject("event");
                    event.setEvent_id(jsonevent.getInt("event_id"));
                    event.setEvent_title(jsonevent.getString("event_title"));
                    event.setEvent_description(jsonevent.getString("event_description"));
                    event.setLang_id(jsonevent.getInt("lang_id"));
                    event.setBegins(jsonevent.getString("begins"));
                    event.setEnds(jsonevent.getString("ends"));
                    if(!jsonevent.isNull("main_attachment")){
                        JSONObject jsonmain_attachment = jsonevent.getJSONObject("main_attachment");
                        mainAttachment.setId(jsonmain_attachment.getInt("id"));
                        mainAttachment.setType(jsonmain_attachment.getString("type"));
                        mainAttachment.setUrl(jsonmain_attachment.getJSONObject("photo_big").getString("url"));
                        mainAttachment.setSmall_photo(jsonmain_attachment.getJSONObject("photo_small").getString("url"));
                        mainAttachment.setMedium_photo(jsonmain_attachment.getJSONObject("photo_medium").getString("url"));
                        if(!jsonevent.isNull("company")){
                            JSONObject jsonCompany = jsonevent.getJSONObject("company");
                            company.setCompany_name(jsonCompany.getString("company_name"));
                            if(jsonCompany.optInt("company_id")!=0){
                                company.setCompany_id(jsonCompany.getInt("company_id"));
                                company.setLang_id(jsonCompany.getInt("lang_id"));
                            }
                        }
                        if(!jsonevent.isNull("creator")){
                            creator.setUser_id(jsonevent.getJSONObject("creator").getInt("user_id"));
                            creator.setName(jsonevent.getJSONObject("creator").getString("name"));
                            creator.setSurname(jsonevent.getJSONObject("creator").getString("surname"));
                            if(!jsonevent.getJSONObject("creator").isNull("photo_small"))
                                creator.setPhoto_small(jsonevent.getJSONObject("creator").getString("photo_small"));
                        }
                    }
                    JSONObject jsonPlace = jsonevent.getJSONObject("place");
                    place.setPlace_id(jsonPlace.getInt("place_id"));
                    place.setPlace_title(jsonPlace.getString("place_title"));
                    place.setPlace_description(jsonPlace.getString("place_description"));
                    place.setLang_id(jsonPlace.getInt("lang_id"));
                    place.setLatitude(jsonPlace.getDouble("latitude"));
                    place.setLongitude(jsonPlace.getDouble("longitude"));
                    place.setPlace_address(jsonPlace.getString("place_address"));
                    place.setPlace_category(jsonPlace.getString("category_name"));
                    place.setCategory_id(jsonPlace.getInt("category_id"));
                    event.setMainAttachment(mainAttachment);
                    event.setCompany(company);
                    event.setPlace(place);
                    e.setEvent(event);
                    massive.add(e);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return massive;
    }

}

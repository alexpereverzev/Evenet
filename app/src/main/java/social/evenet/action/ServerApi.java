package social.evenet.action;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.Map;

import retrofit.Callback;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import retrofit.mime.TypedFile;


/**
 * Created by Александр on 20.12.2014.
 */

public interface ServerApi {

    Converter DATA_CONVERTER = new GsonConverter(new Gson());
   static final String SERVICE_ENDPOINT = "http://evenet.me";

    @POST("/user.login.php")
    void login(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/user.getFriends.php")
    void userGetFriends(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @GET("/user.getInfo.php")
    void userInfo(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @GET("/exp.php")
    void feed(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @GET("/user.getLists.php")
    void userList(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/user.registerDevice.php")
    void registerDevice(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/user.add.php")
    void registerUser(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/meetup.setInfo.php")
    void meetupSet(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/user.follow.php")
    void userFollow(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/user.unfollow.php")
    void userUnfollow(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/user.getMeetups.php")
    void userMeetup(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/user.setInfo.php")
    void userChangeInfo(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/meetup.setStatus.php")
    void meetupChangeStatus(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @GET("/user.getWall.php")
    void userWall(@QueryMap Map<String, String> options, Callback<JSONArray> callback);


    @POST("/place.getEvents.php")
    void placeEvent(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/user.getFollowing.php")
    void userFollowing(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/user.getFollowers.php")
    void userFollower(@QueryMap Map<String, String> options, Callback<JSONArray> callback);


    @POST("/event.getComments.php")
    void eventComment(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/comment.like.php")
    void commentLike(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/event.like.php")
    void eventLike(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/event.unlike.php")
    void eventUnLike(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/user.addComment.php")
    void userAddComment(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @GET("/notifications.get.php")
    void userNotification(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/place.getInfo.php")
    void placeInfo(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/place.getComments.php")
    void placeComment(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/user.createMeetup.php")
    void createMeetup(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/user.addtoMeetup.php")
    void addMeetup(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/event.getSchedule.php")
    void eventSheldure(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/chat.showmessages.php")
    void chatMessage(@QueryMap Map<String, String> options,Callback<JSONArray> callback);

    @POST("/chat.addmessage.php")
    void chatAddMessage(@QueryMap Map<String, String> options,Callback<JSONArray> callback);

    @Multipart
    @POST("/photos.upload.php")
    void chatAddUpload(@QueryMap Map<String, String> options,@Part("file") TypedFile photo,Callback<JSONArray> callback);

    @POST("/event.send.php")
    void sendEventFriend(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @GET("/meetup.getInfo.php")
    void meetupInfo(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/meetup.setInfo.php")
    void meetupChange(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/lists.getEvents.php")
    void listEvent(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/list.getRecommendedPlaces.php")
    void placeRecomended(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/list.addPlaces.php")
    void listPlace(@QueryMap Map<String, String> options, Callback<JSONArray> callback);

    @POST("/list.add.php")
    void listAdd(@QueryMap Map<String, String> options);



}

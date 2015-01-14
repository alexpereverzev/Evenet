package social.evenet.helper;

import com.vk.sdk.VKScope;

/**
 * Created by Александр on 06.09.2014.
 */
public class SupportInfo {

    public static String url = "http://evenet.me/";

    public static String add = "user.add.php?name=";
    public static String user_follow = "user.follow.php?access_token=";
    public static String un_user_follow = "user.unfollow.php?access_token=";
    public static String meetup_set_status = "meetup.setStatus.php?access_token=";
    public static String comment_like = "comment.like.php?access_token=";
    public static String chat_url="ws://188.225.25.18:80/ws/";

    public static final String[] sMyScope = new String[] {
            VKScope.FRIENDS,
            VKScope.DIRECT,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.PAGES,
            VKScope.NOHTTPS
    };


}

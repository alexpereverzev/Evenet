package social.evenet.twitter;

import android.app.Activity;
import android.content.Context;

import java.io.File;

import social.evenet.activity.T4JTwitterLoginActivity;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Alexander on 13.10.2014.
 */
public class T4JTwitterFunctions {

    public T4JTwitterFunctions() {
        // TODO Auto-generated constructor stub
    }

    public static void postToTwitter(Context c, final Activity callingActivity, final String consumerKey, final String consumerSecret, final String message, final TwitterPostResponse postResponse){
        if(!T4JTwitterLoginActivity.isConnected(c)){
            postResponse.OnResult(false);
            return;
        }

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(consumerKey);
        configurationBuilder.setOAuthConsumerSecret(consumerSecret);
        configurationBuilder.setOAuthAccessToken(T4JTwitterLoginActivity.getAccessToken((c)));
        configurationBuilder.setOAuthAccessTokenSecret(T4JTwitterLoginActivity.getAccessTokenSecret(c));
        Configuration configuration = configurationBuilder.build();
        final Twitter twitter = new TwitterFactory(configuration).getInstance();

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = true;
                try {
                    twitter.updateStatus(message);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    success = false;
                }

                final boolean finalSuccess = success;

                callingActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        postResponse.OnResult(finalSuccess);
                    }
                });

            }
        }).start();
    }

    public static void postToTwitterImage(Context c, final Activity callingActivity,File file, final String consumerKey, final String consumerSecret, final String message, final TwitterPostResponse postResponse){
        if(!T4JTwitterLoginActivity.isConnected(c)){
            postResponse.OnResult(false);
            return;
        }

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(consumerKey);
        configurationBuilder.setOAuthConsumerSecret(consumerSecret);
        configurationBuilder.setOAuthAccessToken(T4JTwitterLoginActivity.getAccessToken((c)));
        configurationBuilder.setOAuthAccessTokenSecret(T4JTwitterLoginActivity.getAccessTokenSecret(c));
        Configuration configuration = configurationBuilder.build();
        final StatusUpdate status = new StatusUpdate(message);
      //  status.setMedia(file);
        final Twitter twitter = new TwitterFactory(configuration).getInstance();

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = true;
                try {
                    twitter.updateStatus(status);

                } catch (TwitterException e) {
                    e.printStackTrace();
                    success = false;
                }

                final boolean finalSuccess = success;

                callingActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        postResponse.OnResult(finalSuccess);
                    }
                });

            }
        }).start();
    }

    public static abstract class TwitterPostResponse{
        public abstract void OnResult(Boolean success);
    }
}

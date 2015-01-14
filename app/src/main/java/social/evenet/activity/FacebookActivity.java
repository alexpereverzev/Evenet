package social.evenet.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import social.evenet.fb.Permissi;
import social.evenet.fb.Properties;
import social.evenet.fb.SimpleFacebook;
import social.evenet.fb.SimpleFacebookConfiguration;
import social.evenet.fb.entities.Feed;
import social.evenet.fb.entities.Profile;
import social.evenet.fb.utils.Attributes;
import social.evenet.fb.utils.PictureAttributes;
import social.evenet.fb.utils.Utils;
import social.evenet.R;
import social.evenet.db.UserInfo;

public class FacebookActivity extends HashActivity {


    private SimpleFacebook mSimpleFacebook;

    private ProgressDialog mProgress;
    private Button mButtonLogin;
    private Button mButtonLogout;
    private TextView mTextStatus;
    private Button mButtonPublishFeed;
    private Button mButtonPublishStory;
    private Button mButtonPublishPhoto;
    private Button mButtonInviteAll;
    private Button mButtonInviteSuggested;
    private Button mButtonInviteOne;
    private Button mButtonGetProfile;
    private Button mButtonGetProfileProperties;
    private Button mButtonGetFriends;
    private Button mButtonFragments;

    private UserInfo user;
    private Intent intent;

    public SimpleFacebook.OnPublishListener onPublishListener = new SimpleFacebook.OnPublishListener() {

        @Override
        public void onFail(String reason) {
            hideDialog();
            // insure that you are logged in before publishing

        }

        @Override
        public void onException(Throwable throwable) {
            hideDialog();

        }

        @Override
        public void onThinking() {
            // show progress bar or something to the user while publishing
            showDialog();
        }

        @Override
        public void onComplete(String postId) {
            hideDialog();
            toast("Published successfully. The new post id = " + postId);
        }
    };

    // Login listener
    public SimpleFacebook.OnLoginListener mOnLoginListener = new SimpleFacebook.OnLoginListener() {

        @Override
        public void onFail(String reason) {
            //   mTextStatus.setText(reason);

        }

        @Override
        public void onException(Throwable throwable) {
            //  mTextStatus.setText("Exception: " + throwable.getMessage());

        }

        @Override
        public void onThinking() {
        }

        @Override
        public void onLogin() {


            Feed feed = new Feed.Builder()
                    .setMessage("Clone it out...")
                    .setName("Evenet Network for Android")
                    .setCaption("Code less, do the same.")
                    .setDescription("The Simple publish feeds on facebook from android.")
                    .setPicture("https://raw.github.com/sromku/android-simple-facebook/master/Refs/android_facebook_sdk_logo.png")
                    .setLink("https://github.com/sromku/android-simple-facebook")

                    .build();

            //  mSimpleFacebook.publish(feed, onPublishListener);

            toast("You are logged in");
        }

        @Override
        public void onNotAcceptingPermissions() {
            toast("You didn't accept read permissions");
        }
    };

    public SimpleFacebook.OnProfileRequestListener onProfileRequestListener = new SimpleFacebook.OnProfileRequestListener() {

        @Override
        public void onFail(String reason) {
            hideDialog();

        }

        @Override
        public void onException(Throwable throwable) {
            hideDialog();

        }

        @Override
        public void onThinking() {
            showDialog();


        }

        @SuppressWarnings("unchecked")
        @Override
        public void onComplete(Profile profile) {
            hideDialog();
            String id = profile.getId();
            String firstName = profile.getFirstName();
            String coverUrl = profile.getCover();
            String pictureUrl = profile.getPicture();

            String lastname = profile.getLastName();
            String bir = profile.getBirthday();

            user=new UserInfo();
            user.setName(firstName);
            user.setSurname(lastname);
            user.setEmail(profile.getEmail());
            user.setB_day(profile.getBirthday());
            user.setPhoto_small(pictureUrl);
            intent.putExtra("user",user);
            startActivity(intent);
            finish();

            AlertDialog dialog = Utils.buildProfileResultDialog(FacebookActivity.this,
                    new Pair<String, String>(Properties.ID, id),
                    new Pair<String, String>(Properties.FIRST_NAME, firstName),
                    new Pair<String, String>(Properties.LAST_NAME, lastname),
                    new Pair<String, String>(Properties.BIRTHDAY, bir),
                    new Pair<String, String>(Properties.COVER, coverUrl),
                    new Pair<String, String>(Properties.PICTURE, pictureUrl));
          //  dialog.show();
        }
    };


    private SimpleFacebook.OnLogoutListener mOnLogoutListener = new SimpleFacebook.OnLogoutListener() {

        @Override
        public void onFail(String reason) {

        }

        @Override
        public void onException(Throwable throwable) {

        }

        @Override
        public void onThinking() {
            // show progress bar or something to the user while login is happening

        }

        @Override
        public void onLogout() {
            // change the state of the button or do whatever you want

            toast("You are logged out");
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   //     setContentView(R.layout.activity_facebook);
        intent=new Intent(this, Registration.class);
        mSimpleFacebook = SimpleFacebook.getInstance(this);
        if (!mSimpleFacebook.isLogin()) {
            mSimpleFacebook.login(mOnLoginListener);
        } else {

            PictureAttributes pictureAttributes = Attributes.createPictureAttributes();
            pictureAttributes.setHeight(500);
            pictureAttributes.setWidth(500);
            pictureAttributes.setType(PictureAttributes.PictureType.SQUARE);

            // prepare the properties that we need
            Properties properties = new Properties.Builder()
                    .add(Properties.ID)
                    .add(Properties.FIRST_NAME)
                    .add(Properties.LAST_NAME)
                    .add(Properties.BIRTHDAY)
                    .add(Properties.EDUCATION)
                    .add(Properties.EMAIL)
                    .add(Properties.COVER)
                    .add(Properties.PICTURE, pictureAttributes)
                    .build();

            // do the get profile action
            mSimpleFacebook.getProfile(properties, onProfileRequestListener);
        }
        //  hideDialog();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);
        //  setUIState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Login example.
     */
    private void loginExample() {
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mSimpleFacebook.login(mOnLoginListener);
            }
        });
    }

    /**
     * Logout example
     */
    private void logoutExample() {
        mButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mSimpleFacebook.logout(mOnLogoutListener);
            }
        });
    }

    /**
     * Publish feed example. <br>
     */
    private void publishFeedExample() {
        // listener for publishing action
        final SimpleFacebook.OnPublishListener onPublishListener = new SimpleFacebook.OnPublishListener() {

            @Override
            public void onFail(String reason) {
                hideDialog();
                // insure that you are logged in before publishing

            }

            @Override
            public void onException(Throwable throwable) {
                hideDialog();

            }

            @Override
            public void onThinking() {
                // show progress bar or something to the user while publishing
                showDialog();
            }

            @Override
            public void onComplete(String postId) {
                hideDialog();
                toast("Published successfully. The new post id = " + postId);
            }
        };

        // feed builder
        final Feed feed = new Feed.Builder()
                .setMessage("Clone it out...")
                .setName("Simple Facebook SDK for Android")
                .setCaption("Code less, do the same.")
                .setDescription("The Simple Facebook library project makes the life much easier by coding less code for being able to login, publish feeds and open graph stories, invite friends and more.")
                .setPicture("https://raw.github.com/sromku/android-simple-facebook/master/Refs/android_facebook_sdk_logo.png")
                .setLink("https://github.com/sromku/android-simple-facebook")
                .build();

        // click on button and publish
        mButtonPublishFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSimpleFacebook.publish(feed, onPublishListener);
            }
        });
    }


    private void inviteExamples() {
        // listener for invite action
        final SimpleFacebook.OnInviteListener onInviteListener = new SimpleFacebook.OnInviteListener() {

            @Override
            public void onFail(String reason) {
                // insure that you are logged in before inviting

            }

            @Override
            public void onException(Throwable throwable) {

            }

            @Override
            public void onComplete(List<String> invitedFriends, String requestId) {
                toast("Invitation was sent to " + invitedFriends.size() + " users, invite request=" + requestId);
            }

            @Override
            public void onCancel() {
                toast("Canceled the dialog");
            }

        };

        // invite all
        mButtonInviteAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // will open dialog with all my friends
                mSimpleFacebook.invite("I invite you to use this app", onInviteListener);
            }
        });

        // invite suggested
        mButtonInviteSuggested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] friends = new String[]
                        {
                                "630243197",
                                "584419361",
                                "1456233371",
                                "100000490891462"
                        };
                mSimpleFacebook.invite(friends, "I invite you to use this app", onInviteListener);
            }
        });

        // invite one
        mButtonInviteOne.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String friend = "630243197";
                mSimpleFacebook.invite(friend, "I invite you to use this app", onInviteListener);
            }
        });
    }

    /**
     * Get my profile example
     */
    private void getProfileExample() {
        // listener for profile request
        final SimpleFacebook.OnProfileRequestListener onProfileRequestListener = new SimpleFacebook.OnProfileRequestListener() {

            @Override
            public void onFail(String reason) {
                hideDialog();
                // insure that you are logged in before getting the profile

            }

            @Override
            public void onException(Throwable throwable) {
                hideDialog();

            }

            @Override
            public void onThinking() {
                showDialog();
                // show progress bar or something to the user while fetching profile

            }

            @Override
            public void onComplete(Profile profile) {
                hideDialog();

                String name = profile.getName();
                String email = profile.getEmail();

                toast("name = " + name + ", email = " + String.valueOf(email));
            }
        };
    }

    /**
     * Get my profile with properties example
     */
    private void getProfileWithPropertiesExample() {
        // listener for profile request
        final SimpleFacebook.OnProfileRequestListener onProfileRequestListener = new SimpleFacebook.OnProfileRequestListener() {

            @Override
            public void onFail(String reason) {
                hideDialog();
                // insure that you are logged in before getting the profile
                //   Log.w(TAG, reason);
            }

            @Override
            public void onException(Throwable throwable) {
                hideDialog();
                // Log.e(TAG, "Bad thing happened", throwable);
            }

            @Override
            public void onThinking() {
                showDialog();
                // show progress bar or something to the user while fetching profile

            }

            @SuppressWarnings("unchecked")
            @Override
            public void onComplete(Profile profile) {
                hideDialog();
                String id = profile.getId();
                String firstName = profile.getFirstName();
                String coverUrl = profile.getCover();
                String pictureUrl = profile.getPicture();

                String lastname = profile.getLastName();
                String bir = profile.getBirthday();

                // this is just to show the results
                AlertDialog dialog = Utils.buildProfileResultDialog(FacebookActivity.this,
                        new Pair<String, String>(Properties.ID, id),
                        new Pair<String, String>(Properties.FIRST_NAME, firstName),
                        new Pair<String, String>(Properties.COVER, coverUrl),
                        new Pair<String, String>(Properties.PICTURE, pictureUrl));
                dialog.show();
            }
        };

        // set on click button
        mButtonGetProfileProperties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prepare specific picture that we need
                PictureAttributes pictureAttributes = Attributes.createPictureAttributes();
                pictureAttributes.setHeight(500);
                pictureAttributes.setWidth(500);
                pictureAttributes.setType(PictureAttributes.PictureType.SQUARE);

                // prepare the properties that we need
                Properties properties = new Properties.Builder()
                        .add(Properties.ID)
                        .add(Properties.FIRST_NAME)
                        .add(Properties.LAST_NAME)
                        .add(Properties.BIRTHDAY)
                        .add(Properties.EDUCATION)
                        .add(Properties.EMAIL)
                        .add(Properties.COVER)
                        .add(Properties.PICTURE, pictureAttributes)
                        .build();

                // do the get profile action
                mSimpleFacebook.getProfile(properties, onProfileRequestListener);
            }
        });

    }

    /**
     * Get friends example
     */
    private void getFriendsExample() {
        // listener for friends request
        final SimpleFacebook.OnFriendsRequestListener onFriendsRequestListener = new SimpleFacebook.OnFriendsRequestListener() {

            @Override
            public void onFail(String reason) {
                hideDialog();
                // insure that you are logged in before getting the friends
                //   Log.w(TAG, reason);
            }

            @Override
            public void onException(Throwable throwable) {
                hideDialog();
                //      Log.e(TAG, "Bad thing happened", throwable);
            }

            @Override
            public void onThinking() {
                showDialog();
                // show progress bar or something to the user while fetching profile

            }

            @Override
            public void onComplete(List<Profile> friends) {
                hideDialog();
                // Log.i(TAG, "Number of friends = " + friends.size());
                toast("Number of friends = " + friends.size());
            }

        };

        // set button
        mButtonGetFriends.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSimpleFacebook.getFriends(onFriendsRequestListener);
            }
        });

    }


    private void toast(String message) {

    }


    private void showDialog() {
        mProgress = ProgressDialog.show(this, "Thinking",
                "Waiting for Facebook", true);
    }

    private void hideDialog() {
        mProgress.hide();
    }

    public class OnProfileRequestAdapter implements SimpleFacebook.OnProfileRequestListener {

        @Override
        public void onThinking() {
        }

        @Override
        public void onException(Throwable throwable) {
        }

        @Override
        public void onFail(String reason) {
        }

        @Override
        public void onComplete(Profile profile) {
        }

    }

}

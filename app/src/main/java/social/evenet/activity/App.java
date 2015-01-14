package social.evenet.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.parse.Parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dalvik.system.DexClassLoader;
import retrofit.RestAdapter;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;
import social.evenet.R;
import social.evenet.action.ServerApi;
import social.evenet.fb.Permissi;
import social.evenet.fb.SimpleFacebook;
import social.evenet.fb.SimpleFacebookConfiguration;
import social.evenet.fb.utils.Utils;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * Created by Александр on 07.09.2014.
 */
public class App extends MultiDexApplication {

    private ImageLoaderConfiguration mConfiguration;
    private static App ourInstance;
    private static Context mContext;


    private static ServerApi api;

    public static ServerApi getApi() {
        return api;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "vFJzOozn0ZC6afu4WiqCI8BzVGWNHaurNy7Ndjcd", "AP7CjC8ikEMRS9eVjLkZ3u5ffsE2QeeWxg9uqOoc");

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerApi.SERVICE_ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new StringConverter())
                .build();

        api = restAdapter.create(ServerApi.class);

        ourInstance = this;
        mContext = getApplicationContext();


        mConfiguration = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPoolSize(10)
                .memoryCacheExtraOptions(250, 250)
                .diskCacheExtraOptions(250, 250, null)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .threadPriority(Thread.NORM_PRIORITY)
                        //  .memoryCache(new LruMemoryCache(1000 * 1024 * 1024))
                .diskCacheSize(2000 * 1024 * 1024)
                        //  .diskCacheSize(100 * 1024 * 1024) // 50 Mb
                        //  .memoryCache(new WeakMemoryCache())
                .memoryCache(new LruMemoryCache(1000 * 1024 * 1024))
                .writeDebugLogs()
                .build();

        //restore();
        ImageLoader.getInstance().init(mConfiguration);
        //initImageLoader(mContext);
        setSSLFactory();

        Permissi[] permissions = new Permissi[]{
                Permissi.USER_PHOTOS,
                Permissi.EMAIL,
                Permissi.USER_BIRTHDAY,
                Permissi.PUBLISH_ACTION,
                Permissi.BASIC_INFO,
                Permissi.USER_ABOUT_ME
        };
        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId(getResources().getString(R.string.facebook_app))
                .setNamespace("evenetapp")
                .setPermissions(permissions)
                .build();

        SimpleFacebook.setConfiguration(configuration);
        Utils.updateLanguage(getApplicationContext(), "en");
        Utils.printHashKey(getApplicationContext());


    }


    private void setSSLFactory() {
        try {
            X509TrustManager easyTrustManager = new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{easyTrustManager};
            SSLContext context;
            context = SSLContext.getInstance("TLS");
            context.init(null, trustAllCerts, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static App getInstance() {
        return ourInstance;
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static App getApplication(Context context) {
        if (context instanceof App) {
            return (App) context;
        }
        return (App) context.getApplicationContext();
    }


    class StringConverter implements Converter {

        @Override
        public Object fromBody(TypedInput typedInput, Type type) throws ConversionException {
            String text = null;
            JSONArray result = null;
            try {
                text = fromStream(typedInput.in());
                result = new JSONArray(text);
            } catch (IOException ignored) {
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("Andrey", result.toString());
            return result;
        }

        @Override
        public TypedOutput toBody(Object o) {
            return null;
        }

        public String fromStream(InputStream in) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder out = new StringBuilder();
            String newLine = System.getProperty("line.separator");
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
                out.append(newLine);
            }
            return out.toString();
        }
    }

}

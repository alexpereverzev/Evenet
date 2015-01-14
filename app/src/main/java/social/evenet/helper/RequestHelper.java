package social.evenet.helper;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class RequestHelper {
	
	public static enum Method{
		GET,
		POST
	}
	
	public interface RequestCompleteListener {
		public void onComplete(String response, Object state);
	}
	
	static final String TAG = "RequestHelper";
	public static String mLastRedirectUrl = "";
	

    public void request(final String apiMethod, final Method method, final List<BasicNameValuePair> parameters, final String url, final RequestCompleteListener mRequestCompleteListener) {
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				String result = "";
				try {
					result = getResult(method, parameters, url, null);

				} catch (ClientProtocolException e) {
					result = "ClientProtocolException: " + e.getMessage();
				} catch (IOException e) {
					result = "IOException: " + e.getMessage();
				} catch (Exception e) {
					result = "Exception: " + e.getMessage();
				}				

				if (mRequestCompleteListener != null){
					mRequestCompleteListener.onComplete(result, apiMethod);
				}


			}
		}).start();
	}
	
	public String request(Method method, List<BasicNameValuePair> parameters, String url, List<BasicNameValuePair> headers) throws ClientProtocolException, IOException{
		return getResult(method, parameters, url, headers);
	}
	
	private String getResult(Method method, List<BasicNameValuePair> parameters, String url, List<BasicNameValuePair> headers) throws ClientProtocolException, IOException {
		String result = "";
		DefaultHttpClient client = getNewHttpClient();
		String requestUrl = url;

		HttpResponse response = null;
		
		switch (method) {
		case GET:
			if (!requestUrl.endsWith("?")){
				requestUrl += "?";
			}
			if (parameters != null){
				String paramString = URLEncodedUtils.format(parameters, HTTP.UTF_8);
				requestUrl += paramString;
			}
			HttpGet get = new HttpGet(requestUrl);
			if(headers != null){
				for(BasicNameValuePair value : headers){
					get.addHeader(value.getName(), value.getValue());
				}
			}		
			response = client.execute(get);
			break;
			
		case POST:
			HttpPost post = new HttpPost(requestUrl);
			if (parameters != null){
				post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
			}
			if(headers != null){
				for(BasicNameValuePair value : headers){
					post.addHeader(value.getName(), value.getValue());
				}
			}

			response = client.execute(post);
			break;
		}
		

		if(response.getEntity() != null){
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + System.getProperty("line.separator"));
			}
			result = sb.toString();
		} else {
			result = "";
		}
		
		return result;
	}
	
	public DefaultHttpClient getNewHttpClient() {
	    try {
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);

	        SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	        HttpParams params = new BasicHttpParams();
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

	        SchemeRegistry registry = new SchemeRegistry();
	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        registry.register(new Scheme("https", sf, 443));

	        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

	        DefaultHttpClient httpclient = new DefaultHttpClient(ccm, params);
			int portOfProxy = android.net.Proxy.getDefaultPort();
			if (portOfProxy > 0) {
				HttpHost proxy = new HttpHost(android.net.Proxy.getDefaultHost(), portOfProxy);
				httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}


	        return httpclient;
	    } catch (Exception e) {
	        return new DefaultHttpClient();
	    }
	}
	
	class MySSLSocketFactory extends SSLSocketFactory {
	    SSLContext sslContext = SSLContext.getInstance("TLS");

	    public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
	        super(truststore);

	        TrustManager tm = new X509TrustManager() {
	            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	            }

	            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	            }

	            public X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }
	        };

	        sslContext.init(null, new TrustManager[] { tm }, null);
	    }

	    @Override
	    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
	        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
	    }

	    @Override
	    public Socket createSocket() throws IOException {
	        return sslContext.getSocketFactory().createSocket();
	    }
	}

}
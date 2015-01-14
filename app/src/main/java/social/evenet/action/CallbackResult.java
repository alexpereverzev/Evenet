package social.evenet.action;

import android.os.Bundle;

public interface CallbackResult {
	
	void onCallback(int requestId, int resultCode, String method, Bundle data);

}
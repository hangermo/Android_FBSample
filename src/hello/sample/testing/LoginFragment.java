package hello.sample.testing;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment {
	//決定在 onActivityResult() 時要不要更新一個 session 的資訊
	private static final int REAUTH_ACTIVITY_CODE = 100;
	private ProfilePictureView fbPicture;
	private TextView tvName;
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callBack = new Session.StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callBack);
        uiHelper.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.login_layout, container, false);
		fbPicture = (ProfilePictureView) view.findViewById(R.id.fbPicture);
		fbPicture.setCropped(true);
		tvName = (TextView) view.findViewById(R.id.tvName);
		return view;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REAUTH_ACTIVITY_CODE) {
	        uiHelper.onActivityResult(requestCode, resultCode, data);
	    }
	}
	
	@Override
	public void onSaveInstanceState(Bundle bundle) {
	    super.onSaveInstanceState(bundle);
	    uiHelper.onSaveInstanceState(bundle);
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}
	
	//回應 session 的改變然後呼叫 makeMeRequest() 方法
	private void onSessionStateChange(final Session session,
			SessionState state, Exception exception) {
		if (session != null && session.isOpened()) {
			makeMeRequest(session);
		}
	}
	
	//透過此方法取得使用者資料
	private void makeMeRequest(final Session session) {
		Request request = Request.newMeRequest(session,
				new Request.GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (session == Session.getActiveSession()) {
							if (user != null) {
								fbPicture.setProfileId(user.getId());
								tvName.setText(user.getName());
							}
						}
						if (response.getError() != null) {
							//錯誤處理寫在這
						}
					}
				});
		request.executeAsync();
	}
	
}

package hello.sample.testing;

import java.util.Arrays;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NotLoginFragment extends Fragment {
	private LoginButton login_button;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.notlogin_layout, container, false);
		login_button = (LoginButton) view.findViewById(R.id.login_button);
		// 取得使用者授權同意
		// "public_profile" -> 取得使用者基本公開資訊
		// "user_friends" -> 取得使用這個APP的好友清單
		// "user_likes" -> 取得使用者說讚的內容
		// "user_status" -> 取得使用者狀態與打卡訊息
		login_button.setReadPermissions(Arrays.asList("public_profile",
				"user_friends", "user_likes", "user_status"));
		return view;
	}

}

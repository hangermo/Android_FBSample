package hello.sample.testing;

import java.util.Arrays;

import com.facebook.widget.LoginButton;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NotLoginFragment extends Fragment {
	private LoginButton login_button;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    		View view = inflater.inflate(R.layout.notlogin_layout, container, false);
    		//取得使用者授權同意
    		login_button = (LoginButton)view.findViewById(R.id.login_button);
    		login_button.setFragment(this);
    		login_button.setReadPermissions(Arrays.asList("user_likes", "user_status"));
    		return view;
    }
}


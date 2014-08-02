package hello.sample.testing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

public class MainActivity extends FragmentActivity {
	//定義一個 fragments 的私有陣列給 main activity 使用
	//也定義 array index 常數來操作這些 fragments
	private static final int NOTLOGIN = 0;
	private static final int LOGIN = 1;
	private static final int SETTINGS = 2;
	private static final int FRAGMENT_COUNT = SETTINGS + 1;
	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

	private boolean isResumed = false;
	private MenuItem settings;
	//使用 UiLifecycleHelper 來追蹤 session 以及 觸發 session state 改變的 listener
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callBack = new Session.StatusCallback() {
		
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			//呼叫先前定義好的 onSessionStateChange()方法。 
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//追蹤 session 所必要的
		uiHelper = new UiLifecycleHelper(this, callBack);
		uiHelper.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		FragmentManager fm = getSupportFragmentManager();
		fragments[NOTLOGIN] = fm.findFragmentById(R.id.notLoginFragment);
		fragments[LOGIN] = fm.findFragmentById(R.id.loginFragment);
		fragments[SETTINGS] = fm.findFragmentById(R.id.userSettingsFragment);
		//先將 fragment 都隱藏起來
		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++) {
			transaction.hide(fragments[i]);
		}
		transaction.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();
		isResumed = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		uiHelper.onPause();
		isResumed = false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}
	
	//處理在 fragment 被生成時的情況, 未授權跟授權的 UI 需要適當的設置
	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			showFragment(LOGIN, false);
		} else {
			showFragment(NOTLOGIN, false);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (fragments[LOGIN].isVisible()) {
			if (menu.size() == 0) {
				settings = menu.add(R.string.settings);
			}
			return true;
		} else {
			menu.clear();
			settings = null;
		}
		return false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.equals(settings)) {
	        showFragment(SETTINGS, true);
	        return true;
	    }
	    return false;
	}
	//這個方法基於使用者的授權狀態, 呼叫顯示相關的 fragment 。 
	//這個方法藉由 isResume flag , 使其只會在 activity visible 的情況下處理 UI 的改變。
	//在這方法裡, 在呼叫合適的 fragment 前, Fragment 的 backstack 會先被清除。
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (isResumed) {
			FragmentManager manager = getSupportFragmentManager();
			int backStackSize = manager.getBackStackEntryCount();
			for (int i = 0; i < backStackSize; i++) {
				manager.popBackStack();
			}
			if (state.equals(SessionState.OPENED)) {
				showFragment(LOGIN, false);
			} else if (state.isClosed()) {
				showFragment(NOTLOGIN, false);
			}
		}
	}
	//自定義此方法，負責顯示要求的某個 fragment 並且將其他的 fragments 隱藏
	void showFragment(int fragmentIndex, boolean addToBackStack) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++) {
			if (i == fragmentIndex) {
				transaction.show(fragments[i]);
			} else {
				transaction.hide(fragments[i]);
			}
		}
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}

}

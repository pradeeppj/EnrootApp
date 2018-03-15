package com.example.letsstart.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.letsstart.common.MyData;
import com.parse.ParseUser;

/**
 * Activity which starts an intent for either the logged in (MainActivity) or logged out
 * (SignUpOrLoginActivity) activity.
 */
public class DispatchActivity extends Activity {

  public DispatchActivity() {
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Check if there is current user info
    if (ParseUser.getCurrentUser() != null) {
      // Start an intent for the logged in activity
        MyData.setScreen(MyData.Screen.LIVE_VIEW);
      startActivity(new Intent(this, AugmentedReality.class));
        finish();
    } else {
      // Start and intent for the logged out activity
      startActivity(new Intent(this, LoginActivity.class));
       finish();
    }
  }

}

package com.example.letsstart.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.letsstart.R;
import com.example.letsstart.common.Message;
import com.example.letsstart.common.MyData;
import com.example.letsstart.common.MyMessages;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by SONY on 06-04-2014.
 */
public class NewPublicMessageActivity extends Activity {


    TextView message ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_public_message_activity);
        message  = (TextView) findViewById(R.id.public_message);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                MyData.setScreen(MyData.Screen.LIVE_VIEW);
                this.finish();
                return true;
            case R.id.menu_plant: {
                String textMessage = message.getText().toString();
                Message newMessage = new Message();
                newMessage.setIsPublic(true);
                newMessage.setUser(ParseUser.getCurrentUser());
                newMessage.setLocation(MyData.getCurrentLocation());
                newMessage.setMessage(textMessage);
                ParseACL acl = new ParseACL();
                acl.setPublicReadAccess(true);
                MyMessages.messagePlant = newMessage;
                MyData.setScreen(MyData.Screen.PLAT_VIEW);
                finish();

            }
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        MyData.setScreen(MyData.Screen.LIVE_VIEW);
        this.finish();
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.private_message_menu, menu);
        return true;
    }



}

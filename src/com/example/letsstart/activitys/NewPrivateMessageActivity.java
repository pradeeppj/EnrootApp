package com.example.letsstart.activitys;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.letsstart.AutoFill.ContactPickerAdapter;
import com.example.letsstart.AutoFill.CustomMultiAutoCompleteTextView;
import com.example.letsstart.AutoFill.SmsUtil;
import com.example.letsstart.R;
import com.example.letsstart.common.Message;
import com.example.letsstart.common.MyData;
import com.example.letsstart.common.MyMessages;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

/**
 * Created by SONY on 06-04-2014.
 */
public class NewPrivateMessageActivity  extends Activity {
    private CustomMultiAutoCompleteTextView EnRootUser;
    ArrayList<ParseUser> receipt;
    ActionBar actionBar;
    TextView message ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SmsUtil.selectedContact.clear();
        SmsUtil.getSelectedParseUser().clear();


        setContentView(R.layout.activity_new_private_message);

        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        EnRootUser = (CustomMultiAutoCompleteTextView)
                findViewById(R.id.editTextPrivate);
        ContactPickerAdapter contactPickerAdapter = new ContactPickerAdapter(this,
                android.R.layout.simple_list_item_1, SmsUtil.getContacts(
                this, false)
        );
        EnRootUser.setAdapter(contactPickerAdapter);


        message = (TextView) findViewById(R.id.private_message);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                MyData.setScreen(MyData.Screen.LIVE_VIEW);
                this.finish();
                return true;
            case   R.id.menu_plant :
            { receipt = SmsUtil.getSelectedParseUser();
                String textMessage = message.getText().toString();
                Message newMessage = new Message();
                newMessage.setIsPublic(false);
                newMessage.setUser(ParseUser.getCurrentUser());
                newMessage.setLocation(MyData.getCurrentLocation());
                newMessage.setMessage(textMessage);
                ParseACL acl = new ParseACL();
                acl.setPublicReadAccess(false);
                Log.d("size of selected user is " + receipt.size(), "");
                for (int i = 0; i < receipt.size(); i++) {
                    acl.setReadAccess(receipt.get(i), true);
                    sendPushTo(receipt.get(i));
                }
                newMessage.setACL(acl);
                MyMessages.messagePlant = newMessage ;
                MyData.setScreen(MyData.Screen.PLAT_VIEW);
                finish();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
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

    public void sendPushTo(ParseUser user){

        ParseQuery<ParseInstallation> userQuery  = ParseInstallation.getQuery();
        userQuery.whereEqualTo("user" , user);
        ParsePush.sendMessageInBackground(ParseUser.getCurrentUser().getUsername()+" sent you a new Message" , userQuery);
    }


}

package com.example.letsstart.AutoFill;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;

import com.example.letsstart.common.MyFriends;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SmsUtil {

	public static HashMap<String, String> selectedContact = new HashMap<String, String>();


    public static ArrayList<ParseUser> getSelectedParseUser(){
        ArrayList<ParseUser> selectedUser = new ArrayList<ParseUser>();
        HashMap<String, String> selectedContactClone = (HashMap<String, String>) selectedContact;

        for (Map.Entry<String, String> mapEntry : selectedContactClone
                .entrySet()) {
            selectedUser.add(MyFriends.getFriendByID(mapEntry.getKey()));

        }
        return selectedUser;
    }

	public static ArrayList<Contact> getContacts(Context context,
			boolean addAllConatct) {
		ArrayList<Contact> contacts = new ArrayList<Contact>();


			for(int i = 1 ; i < MyFriends.getMyFriends().size() ; i++){
				Contact contact = new Contact();
				contact.contactName = MyFriends.getMyFriends().get(i).getUsername();
				contact.num =MyFriends.getMyFriends().get(i).getObjectId();
				if (selectedContact.get(contact.num) == null)
					contacts.add(contact);
			}

			Collections.sort(contacts, new Comparator<Contact>() {

                @Override
                public int compare(Contact object1, Contact object2) {
                    return object1.contactName
                            .compareToIgnoreCase(object2.contactName);
                }

            });



		
		Log.i("contactLength",String.valueOf(contacts.size()));
		return contacts;
	}




	public static Object extractBitmapFromTextView(View view) {

		int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		view.measure(spec, spec);
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		c.translate(-view.getScrollX(), -view.getScrollY());
		view.draw(c);
		view.setDrawingCacheEnabled(true);
		Bitmap cacheBmp = view.getDrawingCache();
		Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
		view.destroyDrawingCache();
		return new BitmapDrawable(viewBmp);

	}

}

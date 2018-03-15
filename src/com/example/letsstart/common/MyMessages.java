package com.example.letsstart.common;

import android.location.Location;
import android.util.Log;

import com.example.letsstart.views.glshits.MessageDialog;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Message")
public class MyMessages {
    /**
     * this class has two type of ArrayList
     * 1. contains private_logo message
     * 2. contains public_logo message
     * 3. contains dialog Array
     * private_logo message will be saved locally
     */

    public static boolean isUpdateAvailable = false;
    public static boolean isTextureUnloaded = false ;
    private static Location lastLocation;
    public static final String TAG = "MY Message class";
    private static ArrayList<Message> myPrivateMessages = new ArrayList<Message>();
    private static ArrayList<Message> publicMessages = new ArrayList<Message>();
    private static ArrayList<MessageDialog> myMessageDialog = new ArrayList<MessageDialog>();
    public static Message messagePlant ;
    public static MessageDialog messageDialogPlant;


    public static void addPrivateMessage(Message message) {
        /**
         * this method will add a message in the message list if it doesn't contains the same
         */
        if (!doesContainsPrivate(message)) {
            myPrivateMessages.add(message);
        }
    }


    public static void addPublicMessage(Message message) {
        /**
         * this method will add a public_logo message to our data ...this data wont stored locaally
         */
      if(!doesContainsPublic(message))
        publicMessages.add(message);
    }


    public static boolean doesContainsPrivate(Message message) {
        for (Message p : myPrivateMessages)
            if (p.compareTo(message) == 0)
                return true;
        return false;
    }


    public static boolean doesContainsPublic(Message message) {
        for (Message p : publicMessages)
            if (p.compareTo(message) == 0)
                return true;
        return false;
    }


    public static void loadMessagesFromServer(Location location) {
        lastLocation = location;
        ParseQuery<Message> myQuery = ParseQuery.getQuery(Message.class);
        myQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
       // myQuery.include("user");
        myQuery.whereWithinKilometers("Location", new ParseGeoPoint(MyData.getCurrentLocation().getLatitude(), MyData.getCurrentLocation().getLongitude()), 1);
        myQuery.orderByDescending("createdAt");
        myQuery.setLimit(100);
        myQuery.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    boolean isPublic;
                    for (Message m : messages) {
                        Log.d(TAG , ""+ m.isPublic());
                       if(m.isPublic() == null)
                           isPublic = true;
                       else
                           isPublic = m.isPublic() ;
                        if (isPublic)
                            addPublicMessage(m);
                        else
                            addPrivateMessage(m);
                    }
                    Log.d(TAG ,"added : "+ messages.size() + " messages in public messages "+publicMessages.size() + " in private message " + myPrivateMessages.size());
                    isUpdateAvailable = true;
                } else {
                    Log.d(TAG, "Something went Wrong at loading message .."+e.getMessage());

                }
            }
        });
    }


    /**
     * this method will add a dialog without deleting any entry !
     */
    public static void addMessageDialog(Message message) {

        MessageDialog messageDialog = new MessageDialog(MyData.getContext(), MyData.getGl10());
        messageDialog.setMessage(message);
        myMessageDialog.add(messageDialog);
    }


    /**
     * will check for new message and will add them into dialogs
     */
    public static void updateMessageDialog() {

        MessageDialog messageDialog;
        for (Message m : myPrivateMessages) {
            if (!checkContainsDialog(m)) {
                messageDialog = new MessageDialog(MyData.getContext(), MyData.getGl10());
                messageDialog.setMessage(m);
                myMessageDialog.add(messageDialog);
            }
        }
        for(int i = 0 ; i < publicMessages.size() ; i++) {
            Message m = publicMessages.get(i);
            if(!checkContainsDialog(m)) {
                messageDialog = new MessageDialog(MyData.getContext(), MyData.getGl10());
                messageDialog.setMessage(m);
                myMessageDialog.add(messageDialog);
            }
        }
        Log.d(TAG,"size of message dialogs are :"+ myMessageDialog.size() );
    }


    public static void reloadMessageDialogTexture() {
        /**
         * wiil load the texture and bind it again with the dialog
         * Should Be use on onResume Method
         */

        for(int i=1; i <  myMessageDialog.size() ;i++) {
            MessageDialog md = myMessageDialog.get(i);
            md.loadGLTexture(MyData.getGl10(), MyData.getContext());
        }
    }


    private static boolean checkContainsDialog(Message m) {
        for (MessageDialog md : myMessageDialog) {
            if (md.getMessageEntity().compareTo(m) == 0)
                return true;
        }
        return false;
    }


    public static ArrayList<MessageDialog> getMessageDialogs() {

        return myMessageDialog;
    }


    public static void checkForUpdates(Location location) {

        float[] dist = new float[3];
        Location.distanceBetween(location.getLatitude(),
                location.getLongitude(), lastLocation.getLatitude(),
                lastLocation.getLongitude(), dist);
        if (dist[0] > 1000) {
            //TODO : location is changed by 1 km  reload the messages
            loadMessagesFromServer(MyData.getCurrentLocation());
            lastLocation = MyData.getCurrentLocation();
            updateMessageDialog();
        }
    }


    public static  ArrayList<Message> getPublicMessage(){
       return publicMessages ;
    }


    public static  ArrayList<Message> getMyPrivateMessages(){
        return  myPrivateMessages ;
    }
}

package com.example.letsstart.common;


import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MyFriends {

    public static final String TAG = "MyFriends Class";
    private static ArrayList<ParseUser> myFriendList = new ArrayList<ParseUser>();


    public static void addUser(ParseUser user){
        /**
         * this method will check if user already in the list than it will not add the user
         */


        myFriendList.add(user);
        save();
    }

    public static ArrayList<ParseUser>  getMyFriends(){
        /**
         * this method will return friend list of the current user !
         */
        return myFriendList;
    }


    private static void save() {


    }

    private static  void load(){

        // TODO : colpleate it later .
    }

    public static  void loadFromServer(){

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ONLY);
        query.findInBackground(new FindCallback<ParseUser>() {

            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    Log.d(TAG , objects.size() + "are sent by server");
                    for(ParseUser p : objects){
                        if(!checkIfInList(p)) {
                            //TODO Download the profile Picture here
                            addUser(p);
                        }
                    }
                } else {
                    Log.d(TAG , "Somthing went wrong at MyFreinds class loadFromServer Method" + e.getMessage());
                }
            }



        });

    }


    /**
     * this method will check if the passed user is persent in listt or not ?
     * @param p : user to be checkerd
     * @return true if present false if not present
     */
    public static Boolean checkIfInList(ParseUser p){
        for(ParseUser p0 : myFriendList){
            if(p0.getObjectId() == p.getObjectId())
                return true;
        }
        return false ;
    }

    /**
     * this method will return the friend i.e Parse user matches the ObjectID given
     */
    public static ParseUser getFriendByID(String userID){

        for(int i = 0 ; i < myFriendList.size() ; i ++){
            Log.d(TAG  , "search id is"+ userID + " friends username is : "+ myFriendList.get(i) );
            if(myFriendList.get(i).getObjectId().equals(userID) ){
                return myFriendList.get(i);
            };
        }
        return  null ;
    }


    /**
     * this method will return the friend i.e Parse user matches the ObjectID given
     */
    public static ParseUser getFriendByUserName(String userName){

        for(int i = 0 ; i < myFriendList.size() ; i ++){
            if(myFriendList.get(i).getObjectId().equals(userName) ){
                return myFriendList.get(i);
            };
        }
        return  null ;
    }
}

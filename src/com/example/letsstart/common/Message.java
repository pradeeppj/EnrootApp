package com.example.letsstart.common;

import android.location.Location;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * This is the entity class contains data ...usable to server
 * @author Sunil Dhaker <sunil965@live.com>
 */
@ParseClassName("Message")
public  class Message extends ParseObject implements Comparable<Message>  {

    /**
     * This method will set the message i.e. Text that user will see
     * @param message : message to be set
     */
    public void setMessage(String message) {

        put("message" , message);
    };

    /**
     * This will return String containing the message
     * @return : String containing message
     */
    public String getMessage() {

       return getString("message");
    }

    /**
     * this method will set the location i.e. geoPoint of the message
     * @param location current PhysicalLocation of the object !
     */
    public void setLocation(PhysicalLocation location){

        ParseGeoPoint parseGeoPoint = new ParseGeoPoint();
        parseGeoPoint.setLatitude(location.getLatitude());
        parseGeoPoint.setLongitude(location.getLongitude());
        put("Location" , parseGeoPoint);

    }

    /**
     * this method will set the location i.e. geoPoint of the message
     * @param location current Location of the object !
     */
    public void setLocation(Location location){

        ParseGeoPoint parseGeoPoint = new ParseGeoPoint();
        parseGeoPoint.setLatitude(location.getLatitude());
        parseGeoPoint.setLongitude(location.getLongitude());
        put("Location" , parseGeoPoint);

    }


    /**
     * this method will return a physical location which will be the location of the object
     * @return PhysicalLocation of the Object
     */
    public PhysicalLocation getLocation(){

        ParseGeoPoint location = getParseGeoPoint("Location");
        PhysicalLocation physicalLocation = new PhysicalLocation();
        physicalLocation.setLatitude(location.getLatitude());
        physicalLocation.setLongitude(location.getLongitude());
        physicalLocation.setAltitude(0);
        return physicalLocation;
    }


    /**
     *returns the Parse User
     * @return
     */
    public ParseUser getUser() {
        return getParseUser("user");
    }

    /**
     * sets the Parse user
     * @param value parseUser
     */
    public void setUser(ParseUser value) {
        put("user", value);
    }


    /**
     * this will return the unique id of the message
     */
    public String getMessageID(){

        return  getObjectId();
    }


    public void setIsPublic(Boolean isPublic){

        put("isPublic" , isPublic);
    }

    public Boolean isPublic(){

        return (Boolean)get("isPublic");
    }




    @Override
	public int compareTo(Message another) {

        if(getObjectId().equals( another.getObjectId()))
            return 0;
        return  -1 ;

	}

    public static ParseQuery<Message> getQuery() {
        return ParseQuery.getQuery(Message.class);
    }


//      TODO : add image uri field also !!


}

package com.example.letsstart.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.location.Location;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.letsstart.activitys.NewPrivateMessageActivity;
import com.example.letsstart.activitys.NewPublicMessageActivity;
import com.example.letsstart.common.Message;
import com.example.letsstart.common.MyData;
import com.example.letsstart.common.MyMessages;
import com.example.letsstart.common.PhysicalLocation;
import com.example.letsstart.notifs.NotificationActivity;
import com.example.letsstart.views.glshits.MessageDialog;
import com.example.letsstart.views.glshits.MessageViewRanderer;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class MessageView extends GLSurfaceView implements View.OnTouchListener {

    MessageViewRanderer myRederer;
    Context context;

    public MessageView(final Context context) {
        super(context);
        myRederer = new MessageViewRanderer();
        this.context = context;
        this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.setRenderer(myRederer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        this.getHolder().setFormat(PixelFormat.TRANSPARENT);
        setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // myRederer.addMessage();
        float touchX = event.getX();
        float touchY = event.getY();
        Log.d("","toucjh" + event.getAction());

       if(event.getAction() == 1) {//1 for pointer up
           if (MyData.getScreen() == MyData.Screen.LIVE_VIEW) {
               touchLiveView(touchX, touchY);
           }
           if (MyData.getScreen() == MyData.Screen.PUBLIC_OR_PRIVATE) {
               touchPublicOrPrivateScreen(touchX, touchY);
               Log.d("", "touch public or private called");
               return true;
           }
           if (MyData.getScreen() == MyData.Screen.TYPING) {
               touchTypingScreen(touchX, touchY);
           }
           if (MyData.getScreen() == MyData.Screen.PLAT_VIEW) {
               if (event.getAction() == MotionEvent.ACTION_UP)
                   touchPlantView(touchX, touchY);
               return true;
           }
       }
        return true;
    }


    private void touchPlantView(float touchX, float touchY) {
        if (isButtonPlantTouched(touchX, touchY)) {
            Location location = MyData.getCurrentLocation();
            Log.d("",location.toString());
            PhysicalLocation loc = new PhysicalLocation();
            float azimuth = MyData.getAzimuth();
            //azimuth = 30 ;//TODO : comment it later
            loc.setLongitude(location.getLongitude() + 0.0001 * Math.sin(Math.toRadians(azimuth)));
            loc.setLatitude(location.getLatitude() + 0.0001 * Math.cos(Math.toRadians(azimuth)));
            Log.d("" , loc.toString() + MyData.getCurrentLocation());
            final Message m = new Message();
            m.setLocation(loc);
            m.setUser(ParseUser.getCurrentUser());
            m.setIsPublic(MyMessages.messagePlant.isPublic());
            m.setMessage(MyMessages.messagePlant.getMessage());
           if(!MyMessages.messagePlant.isPublic())
            m.setACL(MyMessages.messagePlant.getACL());
            m.saveInBackground(new SaveCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if (e == null) {
                        Log.d("Message view ", "message sending successful");
                        MyMessages.addMessageDialog(m);
                        MyMessages.messagePlant = null;
                        MyMessages.isTextureUnloaded = true ;
                    } else
                        Log.d("message view class", "error in sending message" + e.getMessage());
                }
            });
            MyData.setScreen(MyData.Screen.LIVE_VIEW);
        }
    }


    private void touchPublicOrPrivateScreen(float touchX, float touchY) {
        Log.d("","here1");
        if (isButtonPublicIsPressed(touchX, touchY)) {
            MyData.setScreen(MyData.Screen.TYPING);
            Log.d("","here2");
            Intent i = new Intent(MyData.getContext(), NewPublicMessageActivity.class);
            MyData.getContext().startActivity(i);
            return;
        }
        if (isButtonPrivatePressed(touchX, touchY)) {
            MyData.setScreen(MyData.Screen.TYPING);
            Log.d("","here3");
            Intent i = new Intent(MyData.getContext(), NewPrivateMessageActivity.class);
            MyData.getContext().startActivity(i);
            return;
        }
    }

    private void touchTypingScreen(float touchX, float touchY) {
        //TODO :: see if something may touch manually !
    }


    public void touchLiveView(float touchX, float touchY) {

        if (isButtonPlantTouched(touchX, touchY)) {
            MyData.setScreen(MyData.Screen.PUBLIC_OR_PRIVATE);
        }
        if (isButtonNotifsTouched(touchX, touchY)) {
            //TODO MyData.setScreen(MyData.Screen.);
            Intent i = new Intent(MyData.getContext(), NotificationActivity.class);
            MyData.getContext().startActivity(i);
            return;
        }
        if (isButtonTrendsTouched(touchX, touchY))
            Log.d("", "buttotrendsIsPressed");

        getTouchedmessage((int) touchX, (int) touchY);
    }


    private Message[] getTouchedmessage(int x, int y) {

        float width = MyData.getGlWidth();
        float height = MyData.getGlHeight();
        MessageDialog msg;
        ArrayList<MessageDialog> msgs = MyMessages.getMessageDialogs();
        for (int i = 0; i < msgs.size(); i++) {
            msg = msgs.get(i);
            if (msg.isTouch(x, y))
                Log.v("is touched  ", msg.getMsg());
        }
        return null;
    }


    public boolean isButtonPlantTouched(float x, float y) {
        Log.d("","herex");
        float screenZ = 10f * MyData.getGlHeight() / 13f;
        float screenX = MyData.getGlWidth() / 2f + screenZ / -2.1f * 0;
        float screenY = MyData.getGlHeight() / 2f + (screenZ / -2.1f) * -1.11f;
        float screenR = (screenZ / -2.1f) * 0.2f;

        if (((x - screenX) * (x - screenX) + (y - screenY) * (y - screenY)) < screenR
                * screenR) {
            return true;
        }

        return false;

    }


    public boolean isButtonNotifsTouched(float x, float y) {

        float screenZ = 10f * MyData.getGlHeight() / 13f;
        float screenX = MyData.getGlWidth() / 2f + screenZ / -2.1f * 0.5f;
        float screenY = MyData.getGlHeight() / 2f + (screenZ / -2.1f) * -1.16f;
        float screenR = (screenZ / -2.1f) * 0.125f;

        if (((x - screenX) * (x - screenX) + (y - screenY) * (y - screenY)) < screenR
                * screenR) {
            return true;
        }

        return false;
    }


    public boolean isButtonTrendsTouched(float x, float y) {

        float screenZ = 10f * MyData.getGlHeight() / 13f;
        float screenX = MyData.getGlWidth() / 2f + screenZ / -2.1f * -0.5f;
        float screenY = MyData.getGlHeight() / 2f + (screenZ / -2.1f) * -1.16f;
        float screenR = (screenZ / -2.1f) * 0.125f;

        if (((x - screenX) * (x - screenX) + (y - screenY) * (y - screenY)) < screenR
                * screenR) {
            return true;
        }

        return false;

    }


    private boolean isButtonPublicIsPressed(float x, float y) {
        float screenZ = 10f * MyData.getGlHeight() / 13f;
        float screenX = MyData.getGlWidth() / 2f + screenZ / -2.1f * 0.5f;
        float screenY = MyData.getGlHeight() / 2f + (screenZ / -2.1f) * -0.9f;
        float screenR = (screenZ / -2.1f) * 0.15f;

        if (((x - screenX) * (x - screenX) + (y - screenY) * (y - screenY)) < screenR
                * screenR) {
            Log.d("", "button public is pressed");
            return true;
        }
        return false;
    }


    private boolean isButtonPrivatePressed(float x, float y) {
        float screenZ = 10f * MyData.getGlHeight() / 13f;
        float screenX = MyData.getGlWidth() / 2f + screenZ / -2.1f * -0.5f;
        float screenY = MyData.getGlHeight() / 2f + (screenZ / -2.1f) * -0.9f;
        float screenR = (screenZ / -2.1f) * 0.15f;

        if (((x - screenX) * (x - screenX) + (y - screenY) * (y - screenY)) < screenR
                * screenR) {
            Log.d("", "button public is pressed");
            return true;
        }

        return false;
    }

}

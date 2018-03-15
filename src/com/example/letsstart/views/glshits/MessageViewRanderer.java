package com.example.letsstart.views.glshits;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

import com.example.letsstart.R;
import com.example.letsstart.common.Message;
import com.example.letsstart.common.MyData;
import com.example.letsstart.common.MyMessages;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MessageViewRanderer implements GLSurfaceView.Renderer {

    public static final String TAG = "Message View Renderer";
    ArrayList<MessageDialog> msgs = new ArrayList<MessageDialog>();
    MessageDialog tempmsg;
    float ang = 0f;
    int buttonGlowCounter;
    private Context context;
    private float azimuth = 90, roll;
    private MyOpenGLCircle radar;
    private MyOpenGLCircle buttonPlant;
    private MyOpenGLCircle buttonNotifs;
    private MyOpenGLCircle buttonTrands;
    private MyGLPoint point;
    private TriangleGL navigat;
    private MyOpenGLSquare enrootLogo;
    private MyOpenGLCircle publicLogo;
    private MyOpenGLCircle privateLogo;
    private MyOpenGLCircle buttonPlantGlow;

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0f);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glLoadIdentity();
        azimuth = 25;
        azimuth = MyData.getAzimuth();
        roll = MyData.getDeviceOrientationAngle();
        if (MyData.getScreen() == MyData.Screen.LIVE_VIEW)
            renderLiveScreen(gl);
        if (MyData.getScreen() == MyData.Screen.PLAT_VIEW)
            renderPlantScreen(gl);
        if (MyData.getScreen() == MyData.Screen.TYPING)
            renderTypingScreen(gl);
        if (MyData.getScreen() == MyData.Screen.PUBLIC_OR_PRIVATE)
            renderPublicOrPrivate(gl);

    }

    private void renderTypingScreen(GL10 gl) {
        //TODO : to complete it by today !!
    }

    private void renderPlantScreen(GL10 gl) {
        if (MyMessages.messageDialogPlant == null) {
            if (MyMessages.messagePlant != null) {
                Log.d("plant screen renderer ", "new dialog is aded");
                MyMessages.messageDialogPlant = new MessageDialog(context, gl);
                MyMessages.messageDialogPlant.setMessage(MyMessages.messagePlant);
            }
        }
        if (MyMessages.messageDialogPlant != null) {
            MyMessages.messageDialogPlant.setX(0);
            MyMessages.messageDialogPlant.setZ(-5);

        }

        gl.glTranslatef(MyMessages.messageDialogPlant.getX(), 0, MyMessages.messageDialogPlant.getZ());
        gl.glRotatef(MyData.getRoll(), 0, 0, 1);
        MyMessages.messageDialogPlant.draw();
        gl.glRotatef(-MyData.getRoll(), 0, 0, 1);
        gl.glTranslatef(-MyMessages.messageDialogPlant.getX(), 0, -MyMessages.messageDialogPlant.getZ());
        drawGlowPlant(gl);


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        if (height == 0) { // Prevent A Divide By Zero By
            height = 1; // Making Height Equal One
        }
        MyData.setGlWidth(width);
        MyData.setGlHeight(height);

        gl.glViewport(0, 0, width, height); // Reset The Current Viewport
        gl.glMatrixMode(GL10.GL_PROJECTION); // Select The Projection Matrix
        gl.glLoadIdentity(); // Reset The Projection Matrix
        GLU.gluPerspective(gl, 67.0f, (float) width / (float) height, 0.5f, 50f);
        gl.glMatrixMode(GL10.GL_MODELVIEW); // Select The Modelview Matrix
        gl.glLoadIdentity(); // Reset The Modelview Matrix

    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        context = MyData.getContext();
        MyData.setGL10(gl);
        tempmsg = new MessageDialog(context, gl);
        radar = new MyOpenGLCircle(0.2f);
        publicLogo = new MyOpenGLCircle(0.15f);
        privateLogo = new MyOpenGLCircle(0.15f);
        radar.loadBallTexture(gl, context, R.drawable.radar);
        point = new MyGLPoint(0, 0, 1);
        navigat = new TriangleGL();
        buttonNotifs = new MyOpenGLCircle(0.125f);
        buttonPlant = new MyOpenGLCircle(.2f);
        buttonTrands = new MyOpenGLCircle(.125f);
        enrootLogo = new MyOpenGLSquare();
        buttonNotifs.loadBallTexture(gl, context, R.drawable.notif_icon);
        buttonPlant.loadBallTexture(gl, context, R.drawable.plant_icon);
        buttonTrands.loadBallTexture(gl, context, R.drawable.trending_icon);
        enrootLogo.loadBallTexture(gl, context, R.drawable.enroot_logo);
        navigat.loadBallTexture(gl, context, R.drawable.radar_tri);
        publicLogo.loadBallTexture(gl, context, R.drawable.public_logo);
        privateLogo.loadBallTexture(gl, context, R.drawable.private_logo);

        buttonPlantGlow = new MyOpenGLCircle(.2f);
        buttonPlantGlow.loadBallTexture(gl, context, R.drawable.plant_icon_glow);

    }


    private void renderLiveScreen(GL10 gl) {
        MyData.setGL10(gl);
        if (MyMessages.isUpdateAvailable) {
            Thread t = new Thread() {
                @Override
                public void run() {

                    MyMessages.updateMessageDialog();
                }
            };
            t.run();
            MyMessages.isUpdateAvailable = false;
        }
        if (MyMessages.isTextureUnloaded) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    MyMessages.reloadMessageDialogTexture();
                }
            };
            t.run();
            MyMessages.isTextureUnloaded = false;
        }
        drawRadarAndLogo(gl);
        drawButtons(gl);
        drawMessages(gl);
    }


    public void drawRadarAndLogo(GL10 gl) {

        gl.glTranslatef(.7f, 1.4f, -2.5f);
        gl.glRotatef(roll, 0, 0, 1);

        // drawing the whole radar ,,..with points ..
        gl.glRotatef(5 + azimuth, 0, 0, 1);
        radar.draw(gl);

        for (int i0 = 0; i0 < msgs.size(); i0++) {
            tempmsg = msgs.get(i0);
            point.draw(gl, tempmsg.getX() * 0.004f, tempmsg.getZ() * -0.004f, 0);
        }
        //TODO take care of message which are not in 100 meter range
        gl.glRotatef(-5 - azimuth, 0, 0, 1);
        navigat.draw(gl);

        gl.glRotatef(-roll, 0, 0, 1);
        gl.glTranslatef(-1.4f, 0, 0);
        enrootLogo.draw(gl);
        gl.glTranslatef(.7f, -1.4f, 2.5f);//back to 0 , 0 , 0

    }

    /**
     * Drawing the plant notif and trending button !!
     */
    public void drawButtons(GL10 gl) {

        gl.glTranslatef(0f, -1.1f, -2.1f);
        gl.glRotatef(180 + roll, 0, 0, 1);
        buttonPlant.draw(gl);  // at
        gl.glRotatef(-roll - 180, 0, 0, 1);
        gl.glTranslatef(.5f, -.06f, 0f);
        gl.glRotatef(roll + 180, 0, 0, 1);
        buttonTrands.draw(gl);
        gl.glRotatef(-roll - 180, 0, 0, 1);
        gl.glTranslatef(-1f, 0, 0);
        gl.glRotatef(roll + 180, 0, 0, 1);
        buttonNotifs.draw(gl);
        gl.glRotatef(-roll - 180, 0, 0, 1);
        gl.glTranslatef(.5f, 1.16f, 2.1f);
        gl.glRotatef(roll, 0, 0, 1);
    }

    public void drawMessages(GL10 gl) {

        gl.glRotatef(azimuth, 0, 1, 0);
        msgs = MyMessages.getMessageDialogs();

        for (int i = 0; i < msgs.size(); i++) {
            tempmsg = msgs.get(i);

            gl.glTranslatef(tempmsg.getX(), 0, tempmsg.getZ());
            gl.glRotatef(-azimuth, 0, 1, 0);

            tempmsg.update();
            tempmsg.draw();
            gl.glRotatef(azimuth, 0, 1, 0);
            gl.glTranslatef(-tempmsg.getX(), 0, -tempmsg.getZ());
        }
        gl.glRotatef(-azimuth, 0, 1, 0);

    }


    private void renderPublicOrPrivate(GL10 gl) {
        drawRadarAndLogo(gl);
        drawPublicAndPrivateButton(gl);
    }


    private void drawPublicAndPrivateButton(GL10 gl) {
        gl.glTranslatef(0f, -0.9f, -2.1f); //going Dawn middle
        gl.glTranslatef(0.5f, 0f, 0);
        gl.glRotatef(180 + roll, 0, 0, 1);
        privateLogo.draw(gl);  // at
        gl.glRotatef(-roll - 180, 0, 0, 1);
        gl.glTranslatef(-1f, 0, 0);
        gl.glRotatef(180 + roll, 0, 0, 1);
        publicLogo.draw(gl);  // at
        gl.glRotatef(-roll - 180, 0, 0, 1);
        gl.glTranslatef(0.5f, 0.9f, 2.1f);//back to 0 , 0  ,0
    }


    private void drawGlowPlant(GL10 gl) {

        buttonGlowCounter = (buttonGlowCounter + 1) % 20;
        gl.glTranslatef(0f, -1.1f, -2.1f);
        gl.glRotatef(180 + roll, 0, 0, 1);
        if (buttonGlowCounter < 10)
            buttonPlant.draw(gl);  // at
        else buttonPlantGlow.draw(gl);
        gl.glRotatef(-roll - 180, 0, 0, 1);
        gl.glTranslatef(0f, 1.1f, 2.1f);
    }
}

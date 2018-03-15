package com.example.letsstart.views.glshits;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.opengl.GLUtils;
import android.util.Log;

import com.example.letsstart.R;
import com.example.letsstart.common.Message;
import com.example.letsstart.common.MyData;
import com.example.letsstart.common.MyFriends;
import com.example.letsstart.common.PhysicalLocation;
import com.example.letsstart.common.Vector;
import com.parse.ParseException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.StringTokenizer;

import javax.microedition.khronos.opengles.GL10;

/**
 * this class should be used to draw a dialog !!
 *
 * @author Sunil Dhaker
 */

public class MessageDialog {
    int[] textures = new int[1];

    private float x = 0f;
    private float y = 0f;
    private float z = 0f;
    float screenX;
    float screenY;
    private Bitmap profPic;
    private Context context;
    private GL10 gl;
    private String title = "Enroot";
    private String msg = "DefaulT message";
    private Message messageEntity;
    public String name;
    float width = 0;
    float height = 0;
    float angle = 0;
    float screenZ = 10f * height / 13f;

    private FloatBuffer vertexBuffer; // buffer holding the vertices
    private FloatBuffer textureBuffer; // buffer holding the texture coordinates
    private float texture[] = {
            // Mapping coordinates for the vertices
            0.0f, 1.0f, // top left (V2)
            0.0f, 0.0f, // bottom left (V1)
            1.0f, 1.0f, // top right (V4)
            1.0f, 0.0f // bottom right (V3)
    };
    private float vertices[] = {-1.365f / 2, -1.0f / 2, 0.0f, // V1 - bottom left
            -1.365f / 2, 1.0f / 2, 0.0f, // V2 - top left
            1.365f / 2, -1.0f / 2, 0.0f, // V3 - bottom right
            1.365f / 2, 1.0f / 2, 0.0f // V4 - top right
    };

    public void setMessage(Message message) {

        this.msg = message.getMessage();
        this.messageEntity = message;
        loadGLTexture(gl, context);
        update();

    }

    public void update() {
        Vector vec = new Vector();
        if (messageEntity != null)
            PhysicalLocation.convLocationToVector(MyData.getCurrentLocation(),
                    messageEntity.getLocation(), vec);

        this.setX(vec.getX());
        this.setY(vec.getY());
        this.setZ(vec.getZ());

        angle = getAngle();// - MyData.getAzimuth();

    }

    public boolean isTouch(float touchX, float touchY) {

        width = MyData.getGlWidth();
        height = MyData.getGlHeight();
        screenZ = -10f * height / 13f;

		/*
         * getting the screen position of the message screenZ is the position of
		 * the screen in the 3d world
		 */

        this.screenX = width
                / 2
                + (screenZ * (float) Math.tan(Math.toRadians(angle)) * (float) Math
                .cos(Math.toRadians(MyData.getDeviceOrientationAngle())));
        this.screenY = height
                / 2
                - (screenZ * (float) Math.tan(Math.toRadians(angle)) * (float) Math
                .sin(Math.toRadians(MyData.getDeviceOrientationAngle())));
        if (angle < 180 + 67 && angle > 180 - 67 && z < -1.0 * (4.0f / 5.0f) && z > -20f) {

            float sWidth = 1.2f * screenZ / z;
            float sHeight = 1.0f * screenZ / z;

            Log.d("", "" + sWidth + " " + sHeight);

            if (touchX < screenX + sHeight / 2.0 && touchY < screenY + sHeight / 2.0 && touchX > screenX - sHeight / 2.0 && touchY > screenY - sHeight / 2.0)
                return true;

        }

        return false;

    }

    public void draw() {

        gl.glEnable(GL10.GL_TEXTURE_2D);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        gl.glFrontFace(GL10.GL_CW);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);

        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glDisable(GL10.GL_TEXTURE_2D);

    }


    public void loadGLTexture(GL10 gl, Context context) {
        // loading texture
        // Create an empty, mutable bitmap
        Bitmap bitmap = Bitmap.createBitmap(179*2, 131*2, Bitmap.Config.ARGB_4444);

        // get a canvas to paint over the bitmap
        Canvas canvas = new Canvas(bitmap);

        // get a background image from resources
        Drawable background = context.getResources().getDrawable(
                R.drawable.message_box);

        background.setBounds(0, 0, 179*2, 131*2);
        //background.setAlpha(256);
        background.draw(canvas); // draw the background to our bitmap

        // note the image format must match the bitmap format
        if (profPic == null)
            profPic = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.plant_icon);

        profPic = GetBitmapClippedCircle(profPic);
        Paint pnt = new Paint();
        canvas.drawBitmap(profPic, null, new Rect(0, 0 , 45*2 , 45*2), pnt);

        // Draw the text

        Paint textPaint = new Paint();
        // title
        textPaint.setTextSize(45);
        textPaint.setColor(Color.RED);
        if(name == null)
            name = title;
        canvas.drawText(name, 95,60, textPaint);
         // message

        textPaint.setAlpha(150);
        textPaint.setTextSize(20);
        textPaint.setAntiAlias(true);
        textPaint.setARGB(0xff, 2, 0x00, 0x00);
        textPaint.setDither(true);
        String[] line = splitIntoLine(msg, 23);

        for (int i = 0; i < Math.min(4, line.length); i++)
            canvas.drawText(line[i], 40, 105 + i * 22, textPaint);
        gl.glGenTextures(1, textures, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                GL10.GL_CLAMP_TO_EDGE); // Set U Wrapping
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                GL10.GL_CLAMP_TO_EDGE); // Set V Wrapping

        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

    }

    public static Bitmap GetBitmapClippedCircle(Bitmap bitmap) {

        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final Bitmap outputBitmap = Bitmap.createBitmap(width, height,
                Config.ARGB_8888);

        final Path path = new Path();
        path.addCircle((float) (width / 2), (float) (height / 2),
                (float) Math.min(width, (height / 2) - 5.0), Path.Direction.CCW);

        final Canvas canvas = new Canvas(outputBitmap);
        canvas.clipPath(path);
        canvas.drawBitmap(bitmap, 0, 0, null);
        return outputBitmap;
    }

    public String[] splitIntoLine(String input, int maxCharInLine) {

        StringTokenizer tok = new StringTokenizer(input, " ");
        StringBuilder output = new StringBuilder(input.length());
        int lineLen = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();

            while (word.length() > maxCharInLine) {
                output.append(word.substring(0, maxCharInLine - lineLen) + "\n");
                word = word.substring(maxCharInLine - lineLen);
                lineLen = 0;
            }

            if (lineLen + word.length() > maxCharInLine) {
                output.append("\n");
                lineLen = 0;
            }
            output.append(word + " ");

            lineLen += word.length() + 1;
        }
        return output.toString().split("\n");
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        loadGLTexture(gl, context);
    }

    public String getMsg() {
        return msg;
    }


    public float getAngle() {

        float ang = (float) (Math.toDegrees(Math.atan2(getX(), getZ())));
        if (ang >= 0)
            return ang;
        return ang + 360;
    }

    public double getDistance() {

        return Math.sqrt(x * x + z * z);
    }

    public MessageDialog(Context context, GL10 gl) {
        this.gl = gl;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuffer.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);
        this.context = context;

        loadGLTexture(gl, context);

    }


    public Message getMessageEntity() {
        return this.messageEntity;
    }

}

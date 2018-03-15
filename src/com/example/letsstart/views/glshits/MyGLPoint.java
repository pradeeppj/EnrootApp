package com.example.letsstart.views.glshits;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.R.color;

public class MyGLPoint {

	float ver[] = { 0f, 0f, 0f };
	float colour[] = { 0.766f	, 0.939f, 0.030f, 1f };
	private FloatBuffer vertBuff;
	private FloatBuffer colorBuffer;

	public MyGLPoint( float r , float g , float b ) {
        colour[0] = r ;
        colour[1] = g;
        colour[2] = b;
		ByteBuffer byt = ByteBuffer.allocateDirect(4 * 3 * 3);
		byt.order(ByteOrder.nativeOrder());
		vertBuff = byt.asFloatBuffer();
		vertBuff.put(ver);
		vertBuff.position(0);
		byt = ByteBuffer.allocateDirect(4 * 4 * 3);
		byt.order(ByteOrder.nativeOrder());
		colorBuffer = byt.asFloatBuffer();
		colorBuffer.put(colour);
		colorBuffer.position(0);

	}

	public void draw(GL10 gl, float x, float y, float z ){
		
	/*gl.glPushMatrix();
		gl.glPointSize(7);
		gl.glTranslatef(-0.2f + x, y, z + .01f);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		gl.glColor4f(r, g, b, 1);
		gl.glDrawArrays(GL10.GL_POINTS, 0, 1);
		gl.glColor4f(0, 0, 0, 1);
		gl.glTranslatef(0.2f - x, -y, -z - .01f);
        gl.glPopMatrix();
		*/
		
		
		gl.glPointSize(5);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertBuff);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
		gl.glTranslatef(x, y, z + .02f);
		gl.glDrawArrays(GL10.GL_POINTS, 0, 1);
		gl.glTranslatef(-x, -y, -z - 0.02f);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        

	}

}

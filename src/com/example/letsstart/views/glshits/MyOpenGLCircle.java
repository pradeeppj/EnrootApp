package com.example.letsstart.views.glshits;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class MyOpenGLCircle {

	private int points = 180;
	private float vertices[];
	private FloatBuffer vertBuff, textureBuffer, colourBuffer;
	float texData[] = null;

	float theta = 0;
	int[] textures = new int[1];

	float textCoordArray[] = new float[360];

	public MyOpenGLCircle(float r) {

		vertices = new float[points * 3];
		for (int i = 0; i < points * 3; i += 3) {

			vertices[i] = r * ((float) Math.cos(theta));
			vertices[i + 1] = r * ((float) Math.sin(theta));
			vertices[i + 2] = 0.0f;

			theta += Math.PI / 90;
		}
		theta = 0;
		for (int i = 0; i < points * 2; i += 2) {

			textCoordArray[i] = (0.5f + 0.5f * ((float) Math.cos(theta)));
			textCoordArray[i + 1] = (0.5f + 0.5f * ((float) Math.sin(theta)));

			theta += Math.PI / 90;
		}

		ByteBuffer bBuff = ByteBuffer.allocateDirect(vertices.length * 4);
		bBuff.order(ByteOrder.nativeOrder());
		vertBuff = bBuff.asFloatBuffer();
		vertBuff.put(vertices);
		vertBuff.position(0);

		ByteBuffer bBuff2 = ByteBuffer
				.allocateDirect(textCoordArray.length * 4);
		bBuff2.order(ByteOrder.nativeOrder());
		textureBuffer = bBuff2.asFloatBuffer();
		textureBuffer.put(textCoordArray);
		textureBuffer.position(0);

	}

	public void draw(GL10 gl) {

		gl.glRotatef(180, 0, 1, 0);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertBuff);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glFrontFace(GL10.GL_CW);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]); // 4
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer); // 5
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 180);
		gl.glDisableClientState(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glRotatef(-180, 0, 1, 0);
	}

	public void loadBallTexture(GL10 gl, Context context, int resource) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				resource);
		gl.glGenTextures(1, textures, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		bitmap.recycle();
	}

}
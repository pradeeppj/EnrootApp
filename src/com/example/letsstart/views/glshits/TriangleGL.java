package com.example.letsstart.views.glshits;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class TriangleGL {

	float ver[] = { -0.1f, 0.15f, 0.0f, // V1 - first vertex
			0.1f, 0.15f, 0.0f, // V2 - second vertex
			0.0f, 0.0f, 0.0f // V3 - third vertex
	};
	float textCoordArray[] = { 0 , 0   , 1 , 0  , .5f , 1 };
	private FloatBuffer vertBuff;
	private FloatBuffer textureBuffer;
	private int[] textures = new int[1];

	public TriangleGL() {

		ByteBuffer byt = ByteBuffer.allocateDirect(4 * 3 * 3);
		byt.order(ByteOrder.nativeOrder());
		vertBuff = byt.asFloatBuffer();
		vertBuff.put(ver);
		vertBuff.position(0);
		
		

		ByteBuffer bBuff2 = ByteBuffer
				.allocateDirect(textCoordArray.length * 4);
		bBuff2.order(ByteOrder.nativeOrder());
		textureBuffer = bBuff2.asFloatBuffer();
		textureBuffer.put(textCoordArray);
		textureBuffer.position(0);
	}

	public void draw(GL10 gl) {

		/*
		 * gl.glPointSize(5); gl.glTranslatef(-0.2f + x, y, z);
		 * 
		 * gl.glDrawArrays(GL10.GL_POINTS, 0, 1); gl.glTranslatef(0.2f-x, -y,
		 * -z);
		 */
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
 		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertBuff);
 		gl.glEnable(GL10.GL_TEXTURE_2D);
 		gl.glEnable(GL10.GL_BLEND);
 
 		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]); // 4
 		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer); // 5
 		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
         
 		
 		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, ver.length / 3);
        
         
        gl.glDisable(GL10.GL_BLEND);
        gl.glDisable(GL10.GL_TEXTURE_2D);
 		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
 		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        
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

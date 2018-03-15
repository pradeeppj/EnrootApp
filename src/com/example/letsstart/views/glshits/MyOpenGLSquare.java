package com.example.letsstart.views.glshits;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class MyOpenGLSquare {


	
	int[] textures = new int[1];
	
	private FloatBuffer vertexBuffer; // buffer holding the vertices
	private FloatBuffer textureBuffer; // buffer holding the texture coordinates
	private float texture[] = {
			// Mapping coordinates for the vertices
			0.0f, 1.0f, // top left (V2)
			0.0f, 0.0f, // bottom left (V1)
			1.0f, 1.0f, // top right (V4)
			1.0f, 0.0f // bottom right (V3)
	};
     private float vertices[] = {
    		 -2.16f/7, -1.0f/7, 0.0f, // V1 - bottom left
			-2.16f/7, 1.0f/7, 0.0f, // V2 - top left
			2.16f/7, -1.0f/7, 0.0f, // V3 - bottom right
			2.16f/7, 1.0f/7, 0.0f // V4 - top right
	};
     
     public MyOpenGLSquare(){
    	 

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
 

     }
     
     
     
     
     public void draw(GL10 gl) {
    		
 		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
 		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
 		gl.glEnable(GL10.GL_TEXTURE_2D);
 		gl.glEnable(GL10.GL_BLEND);
 
 		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]); // 4
 		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer); // 5
 		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
         
 		
 		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
        
         
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

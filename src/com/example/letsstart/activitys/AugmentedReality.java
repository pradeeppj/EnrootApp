package com.example.letsstart.activitys;

import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;

import com.example.letsstart.common.MyData;
import com.example.letsstart.common.MyFriends;
import com.example.letsstart.common.MyMessages;
import com.example.letsstart.views.MessageView;
import com.parse.Parse;
import com.parse.ParseAnalytics;

import java.io.IOException;

/**
 * This class extends the SensorsActivity and is designed tie the AugmentedView
 * @author Sunil dhaker <sunil965@live.com>
 */

public class AugmentedReality extends SensorsActivity implements Callback   {
	public static boolean useDataSmoothing = true;
	private Camera camera;
	private SurfaceView mSurfaceView;
	SurfaceHolder mSurfaceHolder;
    FragmentManager fm ;
	private MessageView mGLSurfaceView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
            Parse.initialize(this, "VKipN8qKOfPIzadjfVDcztnNXdUKr8J0IyFRtiLb", "DX7OTEIeGTQsbJ6Vf7Dj1xlyNhfD1vr2av00ZqZQ");
        }catch ( Exception e1){
           Log.d("Augumented reality " , "parse not initialized " + e1.getMessage() );
        }
		ParseAnalytics.trackAppOpened(getIntent());
		final String deviceId = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);
		


		MyData.setContext(this);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			//	WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		mSurfaceView = new SurfaceView(this);
		addContentView(mSurfaceView, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		mGLSurfaceView = new MessageView(this);
		addContentView(mGLSurfaceView, new LayoutParams(
				android.app.ActionBar.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mGLSurfaceView.setZOrderMediaOverlay(true);

		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT
				| LayoutParams.FILL_PARENT);

        MyMessages.loadMessagesFromServer(MyData.getCurrentLocation());
        MyFriends.loadFromServer();


	}


	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	
		Camera.Parameters p = camera.getParameters();
		p.setPreviewSize(arg2, arg3);
		try {
			camera.setPreviewDisplay(arg0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		camera.setDisplayOrientation(90);
		camera.startPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
    {
		
		camera = Camera.open();
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		mGLSurfaceView.onResume();
        MyMessages.updateMessageDialog();
        MyMessages.isTextureUnloaded = true ;
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		mGLSurfaceView.onPause();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		camera.release();
		
	}


    @Override
    public void onBackPressed() {
       if(MyData.getScreen() == MyData.Screen.PLAT_VIEW || MyData.getScreen() == MyData.Screen.PUBLIC_OR_PRIVATE)
          MyData.setScreen(MyData.Screen.LIVE_VIEW);
       else {
           this.finish();
           super.onBackPressed();
       }


    }


}
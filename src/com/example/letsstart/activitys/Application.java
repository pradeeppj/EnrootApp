package com.example.letsstart.activitys;

import com.example.letsstart.common.Message;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.PushService;

public class Application extends android.app.Application {

	public Application() {
	}

	@Override
	public void onCreate() {

          ParseObject.registerSubclass(Message.class);
		  Parse.initialize(this, "VKipN8qKOfPIzadjfVDcztnNXdUKr8J0IyFRtiLb", "DX7OTEIeGTQsbJ6Vf7Dj1xlyNhfD1vr2av00ZqZQ");
		  PushService.setDefaultPushCallback(this, AugmentedReality.class);

		}
}
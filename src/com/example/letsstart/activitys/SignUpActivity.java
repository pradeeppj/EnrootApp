package com.example.letsstart.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.letsstart.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;

/**
 * Activity which displays a login screen to the user.
 */
public class SignUpActivity extends Activity {
    // UI references.
    private EditText usernameView;
    private EditText passwordView;
    private EditText passwordAgainView;
    private EditText name;
    private EditText email;
    private EditText phoneNo;
    private ImageButton camera;
    private ImageButton gallary;
    private Bitmap profilePicture;
    private Bitmap profilePictureThumbnail;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLARY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        	WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_signup);

        // Set up the signup form.
        usernameView = (EditText) findViewById(R.id.username);
        passwordView = (EditText) findViewById(R.id.password);
        passwordAgainView = (EditText) findViewById(R.id.passwordAgain);
        name = (EditText) findViewById(R.id.sign_up_name);
        email = (EditText) findViewById(R.id.sign_up_email);
        phoneNo = (EditText) findViewById(R.id.sign_up_number);
        camera = (ImageButton) findViewById(R.id.sign_up_camera);
        gallary = (ImageButton) findViewById(R.id.sign_up_gallary);


        // Set up the submit button click handler
        findViewById(R.id.action_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // Validate the sign up data
                boolean validationError = false;
                StringBuilder validationErrorMessage =
                        new StringBuilder(getResources().getString(R.string.error_intro));
                if (isEmpty(usernameView)) {
                    validationError = true;
                    validationErrorMessage.append(getResources().getString(R.string.error_blank_username));
                }
                if (isEmpty(name)) {
                    if (validationError) {
                        validationErrorMessage.append(getResources().getString(R.string.error_join));
                    }
                    validationError = true;
                    validationErrorMessage.append(" name cannot be left empty ");
                }
                if (isEmpty(passwordView)) {
                    if (validationError) {
                        validationErrorMessage.append(getResources().getString(R.string.error_join));
                    }
                    validationError = true;
                    validationErrorMessage.append(getResources().getString(R.string.error_blank_password));
                }
                if (!isMatching(passwordView, passwordAgainView)) {
                    if (validationError) {
                        validationErrorMessage.append(getResources().getString(R.string.error_join));
                    }
                    validationError = true;
                    validationErrorMessage.append(getResources().getString(
                            R.string.error_mismatched_passwords));
                }
                if (!isValidEmail(email.getText().toString())) {
                    if (validationError) {
                        validationErrorMessage.append(getResources().getString(R.string.error_join));
                    }
                    validationError = true;
                    validationErrorMessage.append("invalid email");
                }

                if (!isValidPhone(phoneNo.getText().toString())) {
                    if (validationError) {
                        validationErrorMessage.append(getResources().getString(R.string.error_join));
                    }
                    validationError = true;
                    validationErrorMessage.append("invalid Mobile No. ");
                }

                if (profilePicture == null) {
                    if (validationError) {
                        validationErrorMessage.append(getResources().getString(R.string.error_join));
                    }
                    validationError = true;
                    validationErrorMessage.append("Please select Profile Picture");
                }

                //if(isEmpty())
                validationErrorMessage.append(getResources().getString(R.string.error_end));

                // If there is a validation error, display the error
                if (validationError) {
                    Toast.makeText(SignUpActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                // Set up a progress dialog
                final ProgressDialog dlg = new ProgressDialog(SignUpActivity.this);
                dlg.setTitle("Please wait.");
                dlg.setMessage("Signing up.  Please wait.");
                dlg.show();

                // Set up a new Parse user
                ParseUser user = new ParseUser();
                user.setUsername(usernameView.getText().toString());
                user.setPassword(passwordView.getText().toString());
                user.setEmail(email.getText().toString());
                user.put("phone", phoneNo.getText().toString());
                byte[] imageThubnail = getThumbnail(profilePicture);
                if (imageThubnail.length != 0) {

                    user.put("profpic", imageThubnail);
                }
                if (profilePicture != null) {
                    int bytes = profilePicture.getByteCount();
                    ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
                    profilePicture.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
                    byte[] array = buffer.array();
                    ParseFile fullPic = new ParseFile("profilePic", array);
                    fullPic.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.d("Sign UP", "error in uploading profile picture ");
                            }
                        }
                    });
                    user.put("fullpic", fullPic);
                }

                // Call the Parse signup method
                user.signUpInBackground(new SignUpCallback() {

                    @Override
                    public void done(ParseException e) {
                        dlg.dismiss();
                        if (e != null) {
                            // Show the error message
                            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                            installation.put("user", ParseUser.getCurrentUser());
                            installation.saveInBackground();
                           // Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            // Start an intent for the dispatch activity
                            Intent intent = new Intent(SignUpActivity.this, DispatchActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });


        findViewById(R.id.sign_up_camera).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                 /* For Image capture from camera */
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            }
        });

        findViewById(R.id.sign_up_gallary).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

               /* For Image capture from Gallary*/
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                        PICK_FROM_GALLARY);


            }
        });
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isMatching(EditText etText1, EditText etText2) {
        if (etText1.getText().toString().equals(etText2.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isValidPhone(String strng) {

        String regexStr = "^[0-9]$";
        String number = strng;
        if (number.length() < 10 || number.length() > 13) {
            return false;
        }
        return true;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case PICK_FROM_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    profilePicture = (Bitmap) intent.getExtras().get("data");
                }
                break;

            case PICK_FROM_GALLARY:
                if (resultCode == Activity.RESULT_OK) {
                    Uri mImageCaptureUri = intent.getData();
                    profilePicture = loadBitmap(mImageCaptureUri);
                }
                break;
        }
    }


    public Bitmap loadBitmap(Uri url) {

        Bitmap bm ;
        try {
             bm = BitmapFactory.decodeStream(
                    getContentResolver().openInputStream(url));

        } catch (FileNotFoundException e) {

            bm = BitmapFactory.decodeResource(getResources() , R.drawable.plant_icon);
            e.printStackTrace();
        }
        return bm;
    }


    byte[] getThumbnail(Bitmap pp) {
        byte[] imageData = null;

        try {

            final int THUMBNAIL_SIZE = 64;


            Bitmap imageBitmap = pp;

            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            imageData = baos.toByteArray();

        } catch (Exception ex) {

        }
        return imageData;
    }

}

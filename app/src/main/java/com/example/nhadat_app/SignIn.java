package com.example.nhadat_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhadat_app.DB.SQLiteDatabase;
import com.example.nhadat_app.ui.dashboard.DashboardFragment;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class SignIn extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout reFogotPass, reBackground, reLoad, reBlack, re;
    private ProgressBar pro;
    private Button btnLogin, btnLogUp, btnForgot;
    private EditText username, pass, txtEmail;
    private static final String TAG="Login";
    private ParseUser user;
    private TextView txtForgot;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getIDView();
        getBack4App();
        setListener();
        checkDataRegistor();
    }

    //getID
    private void getIDView(){
        reLoad=findViewById( R.id.reLoad );
        re=findViewById(R.id.re4);
        btnLogUp = findViewById(R.id.btnSigUp);
        btnLogin=findViewById(R.id.btnLogin);
        username=findViewById(R.id.txtLogin_Username);
        pass=findViewById(R.id.txtLogin_Password);
        reFogotPass=findViewById( R.id.reFogotPass );
        txtForgot=findViewById( R.id.forgotpass );
        btnForgot=findViewById( R.id.btnGui );
        reBackground=findViewById( R.id.rebackground );
        txtEmail=findViewById( R.id.txtFotgotPassword );
        reBlack=findViewById(R.id.re_black);

        db=new SQLiteDatabase(this);
        Intent a=getIntent();
        String s=a.getStringExtra("activity");
        if(s.equalsIgnoreCase("profile")==true){
            re.setVisibility(View.GONE);
        }
    }
    //ap su kien
    private void setListener(){
        btnForgot.setOnClickListener( this );
        btnLogin.setOnClickListener( this );
        btnLogUp.setOnClickListener( this );
        txtForgot.setOnClickListener( this );
        reBackground.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //button đăng nhập
            case R.id.btnLogin:{
                if(username.getText().toString().isEmpty() && pass.getText().toString().isEmpty()){
                    username.setError("Bạn chưa điền tên tài khoản");
                    pass.setError("Bạn chưa điền mật khẩu");
                }
                else{
                    if(username.getText().toString().isEmpty()){
                        username.setError("Bạn chưa điền tên tài khoản");
                    }
                    else if(pass.getText().toString().isEmpty()){
                        pass.setError("Bạn chưa điền mật khẩu");
                    }
                    else if(pass.getText().toString().length()>16){
                        pass.setError("Mật khẩu không được quá 16 ký tự");
                    }
                    else{
                        reLoad.setVisibility( View.VISIBLE );
                        reBlack.setVisibility(View.VISIBLE);
                        login( username.getText().toString(), pass.getText().toString(), "back4app");
                    }
                }
                break;
            }

            // button đăng ký
            case R.id.btnSigUp:{
                Intent b=getIntent();
                if(b.getStringExtra("activity").equalsIgnoreCase("dangtin")==true){
                    Intent a=new Intent(SignIn.this, SignUp.class);
                    a.putExtra("activity", "dangtin");
                    startActivity(a);
                }
                else if(b.getStringExtra("activity").equalsIgnoreCase("manager")==true){
                    Intent a=new Intent(SignIn.this, SignUp.class);
                    a.putExtra("activity", "manager");
                    startActivity(a);
                }
                else if(b.getStringExtra("activity").equalsIgnoreCase("message")==true){
                    Intent a=new Intent(SignIn.this, SignUp.class);
                    a.putExtra("activity", "message");
                    startActivity(a);
                }
                break;
            }

            // sự kiện khi nhấn vào background đen
            case R.id.rebackground:{
                Animation ani= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation);
                ani.setStartOffset(800);
                ani.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        reBackground.setVisibility( View.GONE );
                        reFogotPass.setVisibility( View.GONE );
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                reFogotPass.startAnimation(ani);
                reBackground.startAnimation( ani );
                break;
            }

            //button reset mật khẩu
            case R.id.btnGui:{
                ParseUser.requestPasswordResetInBackground(txtEmail.getText().toString(), new RequestPasswordResetCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            showMessageEmail( "Thành công" , "Vui lòng kiểm tra email ", false);
                        }
                        else{
                            showMessageEmail( "Lỗi", "Gửi thất bại ", true );
                        }
                    }
                } );
                break;
            }

            //textview reset mật khẩu
            case R.id.forgotpass:{
                Animation ani2= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animationfogot);
                ani2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        reBackground.setVisibility( View.VISIBLE );
                        reFogotPass.setVisibility( View.VISIBLE );
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                reFogotPass.startAnimation(ani2);
                break;
            }
        }

    }

    //hiển thị message reset password sử dụng email
    private void showMessageEmail(String tittle, String message, boolean error){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this)
                .setTitle(tittle)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();

                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    private void checkDataRegistor(){
        Intent ktra=getIntent();
        String frmdk=ktra.getStringExtra("dangky");
        if(frmdk!=null){
            String[] arr1=frmdk.split( "noi" );
            username.setText( arr1[0] );
            pass.setText( arr1[1] );
        }
    }

    private void getBack4App(){
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }

    private void login(String username, String pass, String type){
        user=new ParseUser();
        if(username.equalsIgnoreCase("khoi1404admin")==true){
            user.logInInBackground(username, pass, (user, e)->{
                if(user!=null){
                    Animation ani= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation);
                    ani.setStartOffset(1200);
                    ani.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Intent b=getIntent();
                            String s=b.getStringExtra("activity");
                            db.xoaTK("No");
                            db.themTK("Yes");
                            Intent a=new Intent(SignIn.this, Admin.class);
                            startActivity(a);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    reLoad.startAnimation(ani);
                    reBlack.startAnimation(ani);

                }
                else{
                    Animation ani= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation);
                    ani.setStartOffset(1200);
                    ani.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Toast.makeText( SignIn.this,  "Đăng nhập thất bại! Vui lòng thử lại" , Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    reBlack.startAnimation(ani);
                    reLoad.startAnimation(ani);
                    ParseUser.logOut();
                }
            });
        }
        else{
            user.logInInBackground( username, pass,  (user, e)->{
                if(user!=null){
                    Animation ani= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation);
                    ani.setStartOffset(1200);
                    ani.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Intent b=getIntent();
                            String s=b.getStringExtra("activity");
                            db.xoaTK("No");
                            db.themTK("Yes");
                            if(s.equalsIgnoreCase("profile")==true){
                                Intent a=new Intent(SignIn.this, Profile.class);
                                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(a);
                            }
                            else if(s.equalsIgnoreCase("dangtin")==true){
                                Intent a=new Intent(SignIn.this, MainActivity.class);
                                a.putExtra("type","dtin");
                                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(a);
                            }
                            else if(s.equalsIgnoreCase("manager")==true){
                                Intent a=new Intent(SignIn.this, MainActivity.class);
                                a.putExtra("type","mnager");
                                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(a);
                            }
                            else if(s.equalsIgnoreCase("message")==true){
                                Intent a=new Intent(SignIn.this, MainActivity.class);
                                a.putExtra("type","message");
                                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(a);
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    reLoad.startAnimation(ani);
                    reBlack.startAnimation(ani);

                }
                else{
                    Animation ani= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation);
                    ani.setStartOffset(1200);
                    ani.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Toast.makeText( SignIn.this,  "Đăng nhập thất bại! Vui lòng thử lại" , Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    reBlack.startAnimation(ani);
                    reLoad.startAnimation(ani);
                    ParseUser.logOut();
                }
            });
        }
    }

}
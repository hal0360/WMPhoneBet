package tw.com.lixin.wmphonebet;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.Gravity;

import java.util.Locale;

import tw.com.atromoby.widgets.CustomInput;
import tw.com.atromoby.widgets.Popup;
import tw.com.atromoby.widgets.RootActivity;
import tw.com.lixin.wmphonebet.global.Setting;
import tw.com.lixin.wmphonebet.global.User;

public class LoginActivity extends RootActivity {

    private CustomInput userIn, passIn;
    private SwitchCompat accountSwitch;
    private Popup popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        popup = new Popup(this,R.layout.login_setting_pop,R.style.SettingCasDialog);
        popup.setGravity(Gravity.TOP|Gravity.END);
        popup.clicked(R.id.english_btn,v -> {
            switchLocale(Locale.US);
            popup.dismiss();
        });
        popup.clicked(R.id.chinese_sim_btn,v -> {
            switchLocale(Locale.CHINA);
            popup.dismiss();
        });

        userIn = findViewById(R.id.userInput);
        passIn = findViewById(R.id.passInput);

        accountSwitch = findViewById(R.id.accountSwitch);
        accountSwitch.setChecked(Setting.savePassword());
        if(Setting.savePassword()) passIn.setText(Setting.remPass());

        clicked(R.id.loginBtn,v ->{
            String user = userIn.getRawText();
            String pass = passIn.getRawText();
            if(Setting.savePassword()) Setting.remPass(pass);
            User.account(user);
            toActivity(LoadActivity.class, pass);
        });

        clicked(R.id.questBtn, v->{
            User.account("ANONYMOUS");
            toActivity(LoadActivity.class, "1234");
        });

        clicked(R.id.setting_btn, v->{
            popup.show();
        });

        //   setTextView(R.id.table_txt, 4 + "");

        clicked(accountSwitch, v -> Setting.savePassword(accountSwitch.isChecked()));

        // setTextView(R.id.user_online_txt, 4 + "");


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
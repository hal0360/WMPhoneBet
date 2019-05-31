package tw.com.lixin.wmphonebet.Tools;

import android.view.Gravity;

import java.util.Locale;

import tw.com.atromoby.utils.Kit;
import tw.com.atromoby.widgets.Popup;
import tw.com.lixin.wmcasino.App;
import tw.com.lixin.wmcasino.LoginActivity;
import tw.com.lixin.wmcasino.R;
import tw.com.lixin.wmcasino.SocketActivity;
import tw.com.lixin.wmcasino.global.User;

public class SettingPopup {

    private Popup popup;

    public SettingPopup(SocketActivity context){
        popup = new Popup(context, R.layout.setting_popup, R.style.SettingCasDialog);
        popup.setGravity(Gravity.TOP|Gravity.END);

        popup.clicked(R.id.english_btn,v->{
            context.justRecreated = true;
            context.switchLocale(Locale.US);
            dismiss();
        });

        popup.clicked(R.id.chinese_sim_btn,v->{
            context.justRecreated = true;
            context.switchLocale(Locale.CHINA);
            dismiss();
        });

        popup.clicked(R.id.logout_btn, v -> {
            App.logout();
            dismiss();
            context.toActivity(LoginActivity.class);
        });

        popup.clicked(R.id.report_btn, v -> {
            if(!User.account().equals("ANONYMOUS")){
                new ReportPopup(context).show();
            }else {
                Kit.alert(context,"Registered member only" + "");
            }

            dismiss();
        });

    }

    public void dismiss(){
        popup.dismiss();
    }

    public void show(){
        popup.show();
    }
}

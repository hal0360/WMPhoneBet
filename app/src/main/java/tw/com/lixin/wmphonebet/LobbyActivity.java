package tw.com.lixin.wmphonebet;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import tw.com.atromoby.widgets.ItemHolder;
import tw.com.atromoby.widgets.ItemsView;
import tw.com.atromoby.widgets.RootActivity;
import tw.com.lixin.wmphonebet.Tools.SettingPopup;
import tw.com.lixin.wmphonebet.global.User;
import tw.com.lixin.wmphonebet.interfaces.LobbyBridge;
import tw.com.lixin.wmphonebet.models.EmptyHolder;
import tw.com.lixin.wmphonebet.models.Table;
import tw.com.lixin.wmphonebet.models.TableHolder;
import tw.com.lixin.wmphonebet.models.VerticalEmptyHolder;
import tw.com.lixin.wmphonebet.models.VerticalTableHolder;
import tw.com.lixin.wmphonebet.websocketSource.LobbySource;

public class LobbyActivity extends RootActivity implements LobbyBridge {

    ItemsView itemsView;
    LobbySource source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        source = LobbySource.getInstance();
        setTextView(R.id.member_txt, User.account());
        // setTextView(R.id.member_txt, "\u5e84:\u2666K\u26663\u26662\u95f2:\u2665K\u26633\u2660J");
        int orientation = getResources().getConfiguration().orientation;

        clicked(R.id.setting_icon, v->{
            new SettingPopup(this).show();
        });

        itemsView = findViewById(R.id.itemsView);
        List<ItemHolder> holders = new ArrayList<>();
        for(Table table: source.tables){
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                holders.add(new TableHolder(table));
            } else {
                holders.add(new VerticalTableHolder(table));
            }
        }

        int tRem = 10 - source.tables.size();
        if(tRem > 0){
            for (int g = 0; g<tRem;g++){
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    holders.add(new EmptyHolder());
                } else {
                    holders.add(new VerticalEmptyHolder());
                }
            }
        }

        itemsView.add(holders);

        setTextView(R.id.table_txt, source.tables.size() + "");

    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...

        if(!source.isConnected()){
            toActivity(LoginActivity.class);
        }


    }

    @Override
    public void onBackPressed() {
        App.logout();
        super.onBackPressed();
    }

    @Override
    public void wholeDataUpdated() {

    }

    @Override
    public void balanceUpdated() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setTextView(R.id.player_money, User.balance() + "");
        }
    }

    @Override
    public void peopleOnlineUpdate(int number) {

    }
}
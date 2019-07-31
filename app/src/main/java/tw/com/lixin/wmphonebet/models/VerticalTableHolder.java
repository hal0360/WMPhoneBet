package tw.com.lixin.wmphonebet.models;

import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.Locale;
import java.util.TimerTask;

import tw.com.atromoby.utils.Kit;
import tw.com.atromoby.utils.TimeTask;
import tw.com.atromoby.widgets.ItemHolder;
import tw.com.lixin.wmphonebet.BacActivity;
import tw.com.lixin.wmphonebet.R;
import tw.com.lixin.wmphonebet.Tools.CasinoGrid;
import tw.com.lixin.wmphonebet.WMActivity;
import tw.com.lixin.wmphonebet.websocketSource.BacSource;


public class VerticalTableHolder extends ItemHolder {

    private Table table;

    public VerticalTableHolder(Table table) {
        super(R.layout.table_vertical_item);
        this.table = table;
    }

    @Override
    public void onBind() {
        CasinoGrid grid = findViewById(R.id.road_grid);

        grid.post(() -> {
            double dim = grid.getMeasuredHeight() / 6.0;
            int wGrid = (int) Math.round(grid.getMeasuredWidth()/dim);
            grid.setGrid(wGrid, 6);
            grid.drawRoad(table.firstGrid);
        });


        TextView gyuTxt = findViewById(R.id.gyu_shu);
        gyuTxt.setText(getContex().getString(R.string.table_number) + "  " + table.number + " -- " + table.round);
        TextView numTxt = findViewById(R.id.table_num);
        numTxt.setText(String.format(Locale.US,"%02d", table.groupID));


        clicked(R.id.table_grid,v->{
            WMActivity activity = (WMActivity) getContex();
            BacSource source = BacSource.getInstance();


            source.tableLogin(table,ok->{
                Kit.alert(activity,"gsgscc");
                if(ok){
                    activity.toActivity(BacActivity.class);

                }else{
                    Kit.alert(activity,"Cannot go to this table ");
                }
            });


        });

    }


    @Override
    public void onRecycle() {

    }
}

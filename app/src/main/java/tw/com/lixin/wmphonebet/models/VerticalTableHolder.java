package tw.com.lixin.wmphonebet.models;

import android.os.Build;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.Locale;

import tw.com.atromoby.utils.Json;
import tw.com.atromoby.widgets.ItemHolder;
import tw.com.lixin.wmphonebet.App;
import tw.com.lixin.wmphonebet.R;
import tw.com.lixin.wmphonebet.Tools.CasinoGrid;
import tw.com.lixin.wmphonebet.jsonData.Client10;


public class VerticalTableHolder extends ItemHolder {

    private Table table;

    public VerticalTableHolder(Table table) {
        super(R.layout.table_vertical_item);
        this.table = table;
    }

    @Override
    public void onBind() {

        CasinoGrid grid = findViewById(R.id.road_grid);

        grid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    grid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                double dim = grid.getHeight() / 6.0;
                int wGrid = (int) Math.round(grid.getWidth()/dim);
                grid.setGrid(wGrid, 6);
                grid.drawRoad(table.firstGrid);
            }
        });



        TextView gyuTxt = findViewById(R.id.gyu_shu);
        gyuTxt.setText(getContex().getString(R.string.table_number) + "  " + table.number + " -- " + table.round);
        TextView numTxt = findViewById(R.id.table_num);
        numTxt.setText(String.format(Locale.US,"%02d", table.groupID));


        clicked(R.id.table_grid,v->{
            App.curTable = table;
            App.groupID = table.groupID;

            Client10 client = new Client10(table.groupID);
            App.socket.send(Json.to(client));

        });

    }

    @Override
    public void onRecycle() {

    }
}

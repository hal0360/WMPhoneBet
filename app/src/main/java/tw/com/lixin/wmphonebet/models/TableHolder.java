package tw.com.lixin.wmphonebet.models;

import android.widget.TextView;

import java.util.Locale;

import tw.com.atromoby.utils.Json;
import tw.com.atromoby.widgets.ItemHolder;
import tw.com.lixin.wmphonebet.App;
import tw.com.lixin.wmphonebet.R;
import tw.com.lixin.wmphonebet.Tools.CasinoGrid;
import tw.com.lixin.wmphonebet.jsonData.Client10;


public class TableHolder extends ItemHolder {

    private Table table;

    public TableHolder(Table table) {
        super(R.layout.table_item);
        this.table = table;
    }

    @Override
    public void onBind() {

        CasinoGrid grid = findViewById(R.id.road_grid);
        grid.setGrid(28, 6);

        TextView gyuTxt = findViewById(R.id.gyu_shu);
        gyuTxt.setText(getContex().getString(R.string.table_number) + "  " + table.number + " -- " + table.round);
        TextView numTxt = findViewById(R.id.table_num);
        numTxt.setText(String.format(Locale.US,"%02d", table.groupID));

        grid.drawRoad(table.firstGrid);

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

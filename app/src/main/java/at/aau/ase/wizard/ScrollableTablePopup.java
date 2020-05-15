package at.aau.ase.wizard;

/*

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class ScrollableTablePopup extends AppCompatActivity {

    //colum breite and colum hight
    private final static int[] columBreite = new int[]{40, 40, 40, 40, 40, 200};
    private final static int rowHight = 80;
    private final static int FIXED_HEADER_HEIGHT = 60;

    private TableLayout fixedTableLayout;
    private TableLayout activity_game_popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_popup);

        final HorizontalScrollView tblHeaderhorzScrollView = (HorizontalScrollView) findViewById(R.id.tblHeaderhorzScrollView);
        tblHeaderhorzScrollView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        tblHeaderhorzScrollView.setHorizontalScrollBarEnabled(false);

        final HorizontalScrollView horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        horizontalScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                tblHeaderhorzScrollView.setScrollX(horizontalScrollView.getScrollX());
            }
        });


        this.fixedTableLayout = (TableLayout) findViewById(R.id.fixed_column);
        this.activity_game_popup = (TableLayout) findViewById(R.id.scrollable_part);
        //Setzt die Spaltenbreite für die Tabelle.
        setTableHeaderWidth();
        //Befüllt die Tabelle mit Beispieldaten.
        fillTable();
    }

    private void setTableHeaderWidth() {
        TextView textView;
        textView = (TextView) findViewById(R.id.col0);
        setHeaderWidth(textView, columBreite[0]);

        textView = (TextView) findViewById(R.id.col1);
        setHeaderWidth(textView, columBreite[1]);
        textView = (TextView) findViewById(R.id.col2);
        setHeaderWidth(textView, columBreite[2]);
        textView = (TextView) findViewById(R.id.col3);
        setHeaderWidth(textView, columBreite[3]);
        textView = (TextView) findViewById(R.id.col4);
        setHeaderWidth(textView, columBreite[4]);
        textView = (TextView) findViewById(R.id.col5);
        setHeaderWidth(textView, columBreite[5]);
    }

    private void setHeaderWidth(TextView textView, int width) {
        textView.setWidth(width * getScreenWidth() / 100);
        textView.setHeight(FIXED_HEADER_HEIGHT);
    }

    //Zahlenwerte in Spalten geschreiben
    private void fillTable() {
        Context ctx = getApplicationContext();
        for (int i = 1; i <= 100; i++) {
            fixedTableLayout.addView(createTextView(String.valueOf(i), columBreite[0], i));
            TableRow row = new TableRow(ctx);
            for (int col = 1; col <= 5; col++) {
                row.addView(createTextView(String.valueOf(col), columBreite[col], i));
            }
            activity_game_popup.addView(row);

        }
    }

    private TextView createTextView(String text, int width, int index) {
        TextView textView = new TextView(getApplicationContext());
        textView.setText(text);
        textView.setWidth(width * getScreenWidth() / 100);
        textView.setHeight(rowHight);
        return textView;
    }

    private int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }
}
*/
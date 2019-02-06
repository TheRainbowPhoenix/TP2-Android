package ml.pho3.tp2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import android.view.View;

import static android.widget.AdapterView.*;

public class MainActivity extends Activity {

    private Menu menu;

    private static HashMap<String, String> countries = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BookDbHelper bd = new BookDbHelper(this);
        bd.reload();
        Cursor c = bd.fetchAllBooks();

        final ListView listview = (ListView) findViewById(R.id.listview);

        final ArrayList<String> items = new ArrayList<String>();
        /*for (String c : CountryList.getNameArray()) {
            if(!countries.containsKey(c)) countries.put(c,CountryList.getCountry(c));
            items.add(c);
            Toast toast =Toast.makeText(getApplicationContext(), c, Toast.LENGTH_SHORT);
            toast.show();
        }*/

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                //Country c = countries.get(item);
                Intent i = new Intent(getApplicationContext(), BookActivity.class);
                i.putExtra("Position", position);
                i.putExtra("Name", item);

                Bundle bundle = new Bundle();
                //bundle.putSerializable("Country", c);
                bundle.putString("Name", item);


                /*view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        Country c = CountryList.getCountry(item);
                        Toast toast =Toast.makeText(getApplicationContext(), c.getmCapital(), Toast.LENGTH_SHORT);
                        toast.show();
                        //view.setAlpha(1);
                        //adapter.notifyDataSetChanged();
                    }
                });*/
            }
        });

        this.overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.mainbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                //NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_save:
                //saveChanges();
                //onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

package ml.pho3.tp1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import android.view.View;

import static android.widget.AdapterView.*;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listview = (ListView) findViewById(R.id.listview);

        final ArrayList<String> items = new ArrayList<String>();
        for (String c : CountryList.getNameArray()) {
            items.add(c);
            /*Toast toast =Toast.makeText(getApplicationContext(), c, Toast.LENGTH_SHORT);
            toast.show();*/
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                Country c = CountryList.getCountry(item);
                Intent i = new Intent(getApplicationContext(), CountryActivity.class);
                i.putExtra("Position", position);
                i.putExtra("Name", item);

                Bundle bundle = new Bundle();
                bundle.putSerializable("Country", c);
                bundle.putString("Name", item);

                i.putExtras(bundle);
                startActivity(i);

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
    }

}
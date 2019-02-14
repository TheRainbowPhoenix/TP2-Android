package ml.pho3.tp2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import android.view.View;
import android.widget.Toast;

import static android.widget.AdapterView.*;

public class MainActivity extends Activity {

    private Menu menu;

    private static HashMap<String, String> countries = new HashMap<>();

    private BookAdapter bookAdapter;

    private BookDbHelper bd;

    private long index = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bd = new BookDbHelper(this);
        //bd.reload();
        Cursor c = bd.fetchAllBooks();

        final ListView listview = (ListView) findViewById(R.id.listview);

        bookAdapter = new BookAdapter(this, c);

        final ArrayList<String> items = new ArrayList<String>();
        /*for (String c : CountryList.getNameArray()) {
            if(!countries.containsKey(c)) countries.put(c,CountryList.getCountry(c));
            items.add(c);
            Toast toast =Toast.makeText(getApplicationContext(), c, Toast.LENGTH_SHORT);
            toast.show();
        }*/

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        listview.setAdapter(bookAdapter);

        registerForContextMenu(listview);

        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Cursor item = (Cursor) parent.getItemAtPosition(position);
                long _id = item.getLong(item.getColumnIndexOrThrow(BookDbHelper._ID));
                String title = item.getString(item.getColumnIndexOrThrow(BookDbHelper.COLUMN_BOOK_TITLE));
                String authors = item.getString(item.getColumnIndexOrThrow(BookDbHelper.COLUMN_AUTHORS));
                String year = item.getString(item.getColumnIndexOrThrow(BookDbHelper.COLUMN_YEAR));
                String genres = item.getString(item.getColumnIndexOrThrow(BookDbHelper.COLUMN_GENRES));
                String publisher = item.getString(item.getColumnIndexOrThrow(BookDbHelper.COLUMN_PUBLISHER));
                Book book = new Book(_id, title, authors, year, genres, publisher);
                //(long id, String title, String authors, String year, String genres, String publisher)
                //Country c = countries.get(item);
                Intent i = new Intent(getApplicationContext(), BookActivity.class);

                i.putExtra("edit", 1);
                i.putExtra("data", book);
                /*i.putExtra("Name", item);

                Bundle bundle = new Bundle();
                //bundle.putSerializable("Country", c);
                bundle.putString("Name", item);*/

                startActivityForResult(i, 1);

            }
        });

        this.overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        ListView l = (ListView) v;
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        Cursor item = (Cursor) l.getItemAtPosition(info.position);

        long _id = item.getLong(item.getColumnIndexOrThrow(BookDbHelper._ID));
        String title = item.getString(item.getColumnIndexOrThrow(BookDbHelper.COLUMN_BOOK_TITLE));
        String authors = item.getString(item.getColumnIndexOrThrow(BookDbHelper.COLUMN_AUTHORS));
        String year = item.getString(item.getColumnIndexOrThrow(BookDbHelper.COLUMN_YEAR));
        String genres = item.getString(item.getColumnIndexOrThrow(BookDbHelper.COLUMN_GENRES));
        String publisher = item.getString(item.getColumnIndexOrThrow(BookDbHelper.COLUMN_PUBLISHER));
        Book book = new Book(_id, title, authors, year, genres, publisher);

        index = _id;

        menu.setHeaderTitle(book.getTitle());
        menu.add(Menu.NONE, R.id.delete, Menu.NONE, "Delete");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.mainbar, menu);
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                deleteThis(index);
                updateList();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                //NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_add:
                //saveChanges();
                //onBackPressed();
                createNew();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if(resultCode==Activity.RESULT_OK) {
                    Log.w("result", "> update");
                    final Book b = data.getParcelableExtra("book");
                    bd.updateBook(b);
                    updateList();
                }
                break;
            case 2:
                if(resultCode==Activity.RESULT_OK) {
                    Log.w("result", "> add");
                    final Book b = data.getParcelableExtra("book");
                    bd.addBook(b);
                    updateList();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateList() {
        Cursor c = bd.fetchAllBooks();
        bookAdapter.changeCursor(c);
        bookAdapter.notifyDataSetChanged();
    }

    private void deleteThis(long id) {
        BookDbHelper bd = new BookDbHelper(this);
        bd.deleteBook(id);
        Toast toast = Toast.makeText(getApplicationContext(),
                "Delet this "+id,
                Toast.LENGTH_SHORT);

        toast.show();

        bookAdapter.notifyDataSetChanged();
    }

    public void createNew() {
        Intent i = new Intent(getApplicationContext(), BookActivity.class);
        startActivityForResult(i, 2);
    }

}

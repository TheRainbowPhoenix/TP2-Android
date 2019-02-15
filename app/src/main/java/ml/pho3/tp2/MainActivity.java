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
import android.widget.ListView;

import android.view.View;

import static android.widget.AdapterView.*;

public class MainActivity extends Activity {

    private Menu menu;


    private BookAdapter bookAdapter;

    private BookDbHelper bd;

    private long index = -1;
    private Cursor _item = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bd = new BookDbHelper(this);
        //bd.reload();
        Cursor c = bd.fetchAllBooks();

        final ListView listview = (ListView) findViewById(R.id.listview);

        bookAdapter = new BookAdapter(this, c);

        listview.setAdapter(bookAdapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listview.setItemChecked(2, true);

        registerForContextMenu(listview);

        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Cursor item = (Cursor) parent.getItemAtPosition(position);
                Book book = bd.cursorToBook(item);
                Intent i = new Intent(getApplicationContext(), BookActivity.class);

                i.putExtra("edit", 1);
                i.putExtra("data", book);


                startActivityForResult(i, 1);

            }
        });

        this.overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);

        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        ListView l = (ListView) v;
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        Cursor item = (Cursor) l.getItemAtPosition(info.position);

        Book book = bd.cursorToBook(item);
        _item = item;
        index = book.getId();

        menu.setHeaderTitle(book.getTitle());
        menu.add(Menu.NONE, R.id.delete, Menu.NONE, "Delete");
        menu.add(Menu.NONE, R.id.edit, Menu.NONE, "Edit");
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
            case R.id.edit:
                editThis();
                updateList();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_add:
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

    private void editThis() {
        Cursor item = _item;
        Book book = bd.cursorToBook(item);

        Intent i = new Intent(getApplicationContext(), BookActivity.class);

        i.putExtra("edit", 1);
        i.putExtra("data", book);


        startActivityForResult(i, 1);
    }

    private void deleteThis(long id) {
        BookDbHelper bd = new BookDbHelper(this);
        bd.deleteBook(id);
        bookAdapter.notifyDataSetChanged();
    }

    public void createNew() {
        Intent i = new Intent(getApplicationContext(), BookActivity.class);
        startActivityForResult(i, 2);
    }

}

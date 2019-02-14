package ml.pho3.tp2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class BookActivity extends Activity {

    private Menu menu;

    private Book book;

    private BookDbHelper bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        bd = new BookDbHelper(this);

        Intent i = getIntent();
        int edit = i.getIntExtra("edit", 0);

        final TextView name = findViewById(R.id.nameBook);
        final TextView authors = findViewById(R.id.editAuthors);
        final TextView years = findViewById(R.id.editYear);
        final TextView genres = findViewById(R.id.editGenres);
        final TextView publishers = findViewById(R.id.editPublisher);

        if (edit != 0) {
            book = i.getParcelableExtra("data");

            name.setText(book.getTitle());
            authors.setText(book.getAuthors());
            years.setText(book.getYear());
            genres.setText(book.getGenres());
            publishers.setText(book.getPublisher());
        } else {
            name.setText("");
            authors.setText("");
            years.setText("");
            genres.setText("");
            publishers.setText("");
        }

        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.actionbar, menu);
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
                saveChanges();
                //onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveChanges() {

        final TextView nameE = findViewById(R.id.nameBook);
        final TextView authorsE = findViewById(R.id.editAuthors);
        final TextView yearsE = findViewById(R.id.editYear);
        final TextView genresE = findViewById(R.id.editGenres);
        final TextView publishersE = findViewById(R.id.editPublisher);

        String name = "" + nameE.getText();
        String authors = "" + authorsE.getText();
        String years = "" + yearsE.getText();
        String genres = "" + genresE.getText();
        String publishers = "" + publishersE.getText();

        Intent i = new Intent(getApplicationContext(), BookActivity.class);

        if(book != null) {
            final String nameD = book.getTitle();
            final String authorsD = book.getAuthors();
            final String yearsD = book.getYear();
            final String genresD = book.getGenres();
            final String publishersD = book.getPublisher();

            long id = book.getId();

            if(nameD!=name || authorsD!=authors || yearsD!=years || genresD!=genres || publishersD!=publishers) {
                Book b = new Book(id, name, authors, years, genres, publishers);
                Log.w("getPos","> "+b.getTitle());
                i.putExtra("book", b);
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        } else {
            Log.w("name", "> "+name);
            if(name!=null && authors!=null&&years!=null&&genres!=null&&publishers!=null && name.length()>=1&&authors.length()>=1&&years.length()>=1&&genres.length()>=1&&publishers.length()>=1) {
                Book b = new Book(name, authors, years, genres, publishers);
                if(bd.checkDouble(b)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.duplicated_message)
                            .setTitle(R.string.add_error_title);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Log.w("createBook","> "+b.getTitle());
                    i.putExtra("book", b);
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.empty_message)
                        .setTitle(R.string.create_error_message);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }
}

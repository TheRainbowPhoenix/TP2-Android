package ml.pho3.tp2;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE;

public class BookDbHelper extends SQLiteOpenHelper {

    private static final String TAG = BookDbHelper.class.getSimpleName();

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "book.db";

    public static final String TABLE_NAME = "library";

    public static final String _ID = "_id";
    public static final String COLUMN_BOOK_TITLE = "title";
    public static final String COLUMN_AUTHORS = "authors";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_GENRES = "genres";
    public static final String COLUMN_PUBLISHER = "publisher";

    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void reload(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + "library");
        db.execSQL("Create table library(_id integer primary key autoincrement, title String, authors String, year String, genres String, publisher String);");
        populate();
        db.close();
    }

    public boolean checkDouble(Book book) {
        String name = book.getTitle();
        String author = book.getAuthors();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.query(TABLE_NAME, null, null,null
                ,null
                ,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(BookDbHelper.COLUMN_BOOK_TITLE));
            String authors = cursor.getString(cursor.getColumnIndexOrThrow(BookDbHelper.COLUMN_AUTHORS));
            Log.d("iterate", ""+title+":"+name+" "+author+":"+authors);
            if(title.equals(name) && author.equals(authors)) return true;
        }
        return false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT );", TABLE_NAME, _ID, COLUMN_BOOK_TITLE, COLUMN_AUTHORS, COLUMN_YEAR, COLUMN_GENRES, COLUMN_PUBLISHER);
        db.execSQL(sql);
        Log.e(null, "ERROR");
        populate();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Adds a new book
     * @return  true if the book was added to the table ; false otherwise
     */
    public boolean addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_TITLE,book.getTitle());
        values.put(COLUMN_AUTHORS,book.getAuthors());
        values.put(COLUMN_YEAR,book.getYear());
        values.put(COLUMN_GENRES,book.getGenres());
        values.put(COLUMN_PUBLISHER,book.getPublisher());
        db.insert(TABLE_NAME, null, values);

        // Inserting Row
        long rowID = 0;
	// call db.insert()
        db.close(); // Closing database connection

        return (rowID != -1);
    }

    /**
     * Updates the information of a book inside the data base
     * @return the number of updated rows
     */
    public int updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.w("updateBook","> "+book.getTitle()+" ("+book.getId()+")");
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_TITLE,book.getTitle());
        values.put(COLUMN_AUTHORS,book.getAuthors());
        values.put(COLUMN_YEAR,book.getYear());
        values.put(COLUMN_GENRES,book.getGenres());
        values.put(COLUMN_PUBLISHER,book.getPublisher());
        int res = db.update(TABLE_NAME, values, _ID+"="+book.getId(), null);
        Log.w("updateBook","> "+res);
        db.close();

        // updating row
        // call db.update()
        return res;
    }


    public Cursor fetchAllBooks() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor;
        cursor = db.query(TABLE_NAME, null, null,null
            ,null
            ,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void deleteBook(Cursor cursor) {
        SQLiteDatabase db = this.getWritableDatabase();
        // call db.delete();
        db.close();
    }

    public void deleteBook(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, _ID+"="+id, null);
        db.close();
    }

    public void populate() {
        Log.d(TAG, "call populate()");
        addBook(new Book("Rouge Brésil", "J.-C. Rufin", "2003", "roman d'aventure, roman historique", "Gallimard"));
        addBook(new Book("Guerre et paix", "L. Tolstoï", "1865-1869", "roman historique", "Gallimard"));
        addBook(new Book("Fondation", "I. Asimov", "1957", "roman de science-fiction", "Hachette"));
        addBook(new Book("Du côté de chez Swan", "M. Proust", "1913", "roman", "Gallimard"));
        addBook(new Book("Le Comte de Monte-Cristo", "A. Dumas", "1844-1846", "roman-feuilleton", "Flammarion"));
        addBook(new Book("L'Iliade", "Homère", "8e siècle av. J.-C.", "roman classique", "L'École des loisirs"));
        addBook(new Book("Histoire de Babar, le petit éléphant", "J. de Brunhoff", "1931", "livre pour enfant", "Éditions du Jardin des modes"));
        addBook(new Book("Le Grand Pouvoir du Chninkel", "J. Van Hamme et G. Rosiński", "1988", "BD fantasy", "Casterman"));
        addBook(new Book("Astérix chez les Bretons", "R. Goscinny et A. Uderzo", "1967", "BD aventure", "Hachette"));
        addBook(new Book("Monster", "N. Urasawa", "1994-2001", "manga policier", "Kana Eds"));
        addBook(new Book("V pour Vendetta", "A. Moore et D. Lloyd", "1982-1990", "comics", "Hachette"));

        SQLiteDatabase db = this.getReadableDatabase();
        long numRows = DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM "+TABLE_NAME, null);
        Log.d(TAG, "nb of rows="+numRows);
        db.close();
    }

    public static Book cursorToBook(Cursor cursor) {
        Book book = null;
	// build a Book object from cursor
        return book;
    }
}

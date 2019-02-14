package ml.pho3.tp2;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class BookAdapter extends CursorAdapter {
    // Default constructor
    public BookAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.items_todo, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView Title = (TextView) view.findViewById(R.id.textTitle);
        TextView Authors = (TextView) view.findViewById(R.id.textAuthors);
        ImageView bookCover = (ImageView) view.findViewById(R.id.bookCover);

        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String authors = cursor.getString(cursor.getColumnIndexOrThrow("authors"));
        bookCover.setImageResource(R.drawable.flag_of_emptyness);


        Title.setText(title);
        Authors.setText(authors);
    }
}

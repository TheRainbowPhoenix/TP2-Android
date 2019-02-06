package ml.pho3.tp2;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;

import ml.pho3.tp2.MainActivity;

public class Book implements Parcelable {

    public static final String TAG = Book.class.getSimpleName();

    private long id; // id inside the data base
    private String title;
    private String authors;
    private String year; // publication year
    private String genres;
    private String publisher;

    public Book(long id, String title, String authors, String year, String genres, String publisher) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.year = year;
        this.genres = genres;
        this.publisher = publisher;
    }

    public Book(String title, String authors, String year, String genres, String publisher) {
        this.title = title;
        this.authors = authors;
        this.year = year;
        this.genres = genres;
        this.publisher = publisher;
    }

    public Book() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenres() { return genres;}

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return this.title+"("+this.authors+")";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(authors);
        dest.writeString(year);
        dest.writeString(genres);
        dest.writeString(publisher);
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>()
    {
        @Override
        public Book createFromParcel(Parcel source)
        {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size)
        {
            return new Book[size];
        }
    };

    public Book(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.authors = in.readString();
        this.year = in.readString();
        this.genres = in.readString();
        this.publisher = in.readString();
    }
}

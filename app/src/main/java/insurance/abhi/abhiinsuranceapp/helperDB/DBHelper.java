package insurance.abhi.abhiinsuranceapp.helperDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import insurance.abhi.abhiinsuranceapp.helpers.Constants;
import insurance.abhi.abhiinsuranceapp.models.Post;
import insurance.abhi.abhiinsuranceapp.models.RcdAmount;

import static android.content.ContentValues.TAG;

/**
 * Created by rick on 07-07-2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "loanDB";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_POSTS = "posts";
    private static final String TABLE_AMOUNTS = "amounts";

    // Post Table Columns
    private static final String KEY_POST_ID = "id";
    //private static final String KEY_POST_USER_ID_FK = "userId";
    private static final String KEY_POST_AMOUNT = "text";
    private static final String KEY_POST_PARTYNAME = "name";
    private static final String KEY_POST_INTEREST = "interest";
    private static final String KEY_POST_TOTALAMOUNT = "total_amount";
    private static final String KEY_POST_TIME = "time";
    private static final String KEY_POST_DATESTARTON = "loan_start_date";
    private static final String KEY_POST_DATEENDON = "loan_end_date";
    private static final String KEY_POST_CREATED = "created_date";
    private static final String KEY_POST_STATUS = "loan_status";
    // Amount Table Columns
    private static final String KEY_AMOUNT_ID = "id";
    private static final String KEY_POST_AMOUNT_ID = "postId";
    private static final String KEY_RECD_AMOUNT = "rcdAmount";
    private static final String KEY_CREATED_ON = "created_date";
    private static final String KEY_START_DATE = "start_date";
    private static final String KEY_BALANCE = "balance_amount";
    private static final String KEY_END_DATE = "end_date";

    private static DBHelper sInstance;

    public static synchronized DBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_POSTS +
                "(" +
                KEY_POST_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_POST_PARTYNAME + " TEXT" + "," +
                KEY_POST_AMOUNT + " INTEGER " + "," +
                KEY_POST_INTEREST + " INTEGER " + "," +
                KEY_POST_TOTALAMOUNT + " INTEGER " + ", " +
                KEY_POST_TIME + " INTEGER " + ", " +
                KEY_POST_STATUS + " INTEGER " + ", " +
                KEY_POST_DATESTARTON + " TEXT " + ", " +
                KEY_POST_DATEENDON + " TEXT " + ", " +
                KEY_POST_CREATED + " DEFAULT CURRENT_TIMESTAMP " +
                ")";

        String CREATE_AMOUNT_TABLE = "CREATE TABLE " + TABLE_AMOUNTS +
                "(" +
                KEY_AMOUNT_ID + " INTEGER PRIMARY KEY," +
                KEY_POST_AMOUNT_ID + " TEXT ," +
                KEY_RECD_AMOUNT + " INTEGER ," +
                KEY_START_DATE + " TEXT " + ", " +
                KEY_END_DATE + " TEXT " + ", " +
                KEY_BALANCE+ " INTEGER " + ", " +
                KEY_POST_CREATED + " DEFAULT CURRENT_TIMESTAMP" +
                ")";

        db.execSQL(CREATE_POSTS_TABLE);
        db.execSQL(CREATE_AMOUNT_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_AMOUNTS);
            onCreate(db);
        }
    }

    // Insert a post into the database
    public void addPost(Post post) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).
            long userId = addOrUpdatePost(post);

            ContentValues values = new ContentValues();
            values.put(KEY_POST_PARTYNAME,post.partyName);
            values.put(KEY_POST_AMOUNT,post.totalAmount);
            values.put(KEY_POST_INTEREST,post.interest);
            values.put(KEY_POST_TOTALAMOUNT,post.amountTopay);
            values.put(KEY_POST_TIME,post.time);
            values.put(KEY_POST_STATUS,0);
            values.put(KEY_POST_DATESTARTON,post.getSimplifiedOnDate(post.getDateOn()));
            values.put(KEY_POST_DATEENDON,post.getSimplifiedOnDate(post.getEndDate()));
            values.put(KEY_POST_CREATED, Constants.getDateTime());


            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_POSTS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }
    public void addRecdAmount(RcdAmount rcdAmount) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).
           // long userId = addOrUpdatePost(post);

            ContentValues values = new ContentValues();
            values.put(KEY_POST_AMOUNT_ID, rcdAmount.getPostId());
            values.put(KEY_RECD_AMOUNT,rcdAmount.receivedAmount);
            values.put(KEY_START_DATE,rcdAmount.getSimplifiedStartDate());
            values.put(KEY_END_DATE,rcdAmount.getSimplifiedEndDate());
            values.put(KEY_BALANCE,rcdAmount.balanceAmount);
            values.put(KEY_CREATED_ON, Constants.getDateTime());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_AMOUNTS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }
    // Insert or update a user in the database
    // Since SQLite doesn't support "upsert" we need to fall back on an attempt to UPDATE (in case the
    // user already exists) optionally followed by an INSERT (in case the user does not already exist).
    // Unfortunately, there is a bug with the insertOnConflict method
    // (https://code.google.com/p/android/issues/detail?id=13045) so we need to fall back to the more
    // verbose option of querying for the user's primary key if we did an update.
    public long addOrUpdatePost(Post post) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long postId = -1;

        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_POST_PARTYNAME,post.partyName);
            values.put(KEY_POST_AMOUNT,post.totalAmount);
            values.put(KEY_POST_INTEREST,post.interest);
            values.put(KEY_POST_TOTALAMOUNT,post.amountTopay);
            values.put(KEY_POST_TIME,post.time);
            values.put(KEY_POST_STATUS,0);
            values.put(KEY_POST_DATESTARTON,post.getSimplifiedOnDate(post.getDateOn()));
            values.put(KEY_POST_DATEENDON,post.getSimplifiedOnDate(post.getEndDate()));
            values.put(KEY_POST_CREATED, Constants.getDateTime());
            //values.put(KEY_USER_PROFILE_PICTURE_URL, post.profilePictureUrl);

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_POSTS, values, KEY_POST_ID + "= ?", new String[]{post.id});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_POST_ID, TABLE_POSTS, KEY_POST_ID);
                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(post.partyName)});
                try {
                    if (cursor.moveToFirst()) {
                        postId = cursor.getInt(0);

                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                postId = db.insertOrThrow(TABLE_POSTS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return postId;
    }

    public void updatePostStatus(int status,String postId) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_POST_STATUS,status);

            // This assumes userNames are unique
            int rows = db.update(TABLE_POSTS, values, KEY_POST_ID +  " = ?", new String[]{postId});

            // Check if update succeeded
            if (rows == 1) {

                db.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();

        }

    }
    // Get all posts in the database
    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_POSTS);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Post newPost = new Post();
                    newPost.id = cursor.getString(cursor.getColumnIndex(KEY_POST_ID));
                    newPost.partyName = cursor.getString(cursor.getColumnIndex(KEY_POST_PARTYNAME));
                    newPost.totalAmount = cursor.getLong(cursor.getColumnIndex(KEY_POST_AMOUNT));
                    newPost.interest = cursor.getFloat(cursor.getColumnIndex(KEY_POST_INTEREST));
                    newPost.amountTopay = cursor.getLong(cursor.getColumnIndex(KEY_POST_TOTALAMOUNT));
                    newPost.time = cursor.getInt(cursor.getColumnIndex(KEY_POST_TIME));
                    newPost.loanStatus = cursor.getInt(cursor.getColumnIndex(KEY_POST_STATUS));
                    String dateStartOnStr = cursor.getString(cursor.getColumnIndex(KEY_POST_DATESTARTON));
                    String dateEndOnStr = cursor.getString(cursor.getColumnIndex(KEY_POST_DATEENDON));
                    String dateStr = cursor.getString(cursor.getColumnIndex(KEY_POST_CREATED));
                    Date date = Constants.getDate(dateStr);
                    if (date != null)
                    {
                        newPost.created_date = date;
                    }
                    Date dateStartOn = Constants.getOnlyDate(dateStartOnStr);
                    if (dateStartOn != null)
                    {
                        newPost.dateOn = dateStartOn;
                    }
                    Date dateEndOn = Constants.getOnlyDate(dateEndOnStr);
                    if (dateEndOn != null)
                    {
                        newPost.endDate = dateEndOn;
                    }
                    posts.add(newPost);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return posts;
    }

    public Post getPost(String postId) {
        Post newPost = new Post();
        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        String usersSelectQuery = String.format("SELECT * FROM %s WHERE %s = ?",
                 TABLE_POSTS, KEY_POST_ID);
        Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(postId)});
        try {
            if (cursor.moveToFirst()) {
                    newPost.id = cursor.getString(cursor.getColumnIndex(KEY_POST_ID));
                    newPost.partyName = cursor.getString(cursor.getColumnIndex(KEY_POST_PARTYNAME));
                    newPost.totalAmount = cursor.getLong(cursor.getColumnIndex(KEY_POST_AMOUNT));
                    newPost.interest = cursor.getFloat(cursor.getColumnIndex(KEY_POST_INTEREST));
                    newPost.amountTopay = cursor.getLong(cursor.getColumnIndex(KEY_POST_TOTALAMOUNT));
                    newPost.time = cursor.getInt(cursor.getColumnIndex(KEY_POST_TIME));
                    newPost.loanStatus = cursor.getInt(cursor.getColumnIndex(KEY_POST_STATUS));
                    String dateStartOnStr = cursor.getString(cursor.getColumnIndex(KEY_POST_DATESTARTON));
                    String dateEndOnStr = cursor.getString(cursor.getColumnIndex(KEY_POST_DATEENDON));
                    String dateStr = cursor.getString(cursor.getColumnIndex(KEY_POST_CREATED));
                    Date date = Constants.getDate(dateStr);
                    if (date != null) {
                        newPost.created_date = date;
                    }
                Date dateStartOn = Constants.getOnlyDate(dateStartOnStr);
                if (dateStartOn != null)
                {
                    newPost.dateOn = dateStartOn;
                }
                Date dateEndOn = Constants.getOnlyDate(dateEndOnStr);
                if (dateEndOn != null)
                {
                    newPost.endDate = dateEndOn;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return newPost;
    }

    public List<RcdAmount> getAllRecdAmount(String postId) {
        List<RcdAmount> amounts = new ArrayList<>();


        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        String usersSelectQuery = String.format("SELECT * FROM %s WHERE %s = ?",
                TABLE_AMOUNTS, KEY_POST_AMOUNT_ID);
        Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(postId)});
        try {
            if (cursor.moveToFirst()) {
                do {
                    RcdAmount rcdAmount = new RcdAmount();
                    rcdAmount.id = cursor.getString(cursor.getColumnIndex(KEY_AMOUNT_ID));
                    rcdAmount.receivedAmount = cursor.getLong(cursor.getColumnIndex(KEY_RECD_AMOUNT));
                    rcdAmount.balanceAmount = cursor.getLong(cursor.getColumnIndex(KEY_BALANCE));


                    String startDateStr = cursor.getString(cursor.getColumnIndex(KEY_START_DATE));
                    Date startDate = Constants.getOnlyDate(startDateStr);
                    if (startDate != null)
                    {
                        rcdAmount.startDate = startDate;
                    }

                    String endDateStr = cursor.getString(cursor.getColumnIndex(KEY_END_DATE));
                    Date endDate = Constants.getOnlyDate(endDateStr);
                    if (endDate != null)
                    {
                        rcdAmount.endDate = endDate;
                    }

                    String dateStr = cursor.getString(cursor.getColumnIndex(KEY_CREATED_ON));
                    Date date = Constants.getDate(dateStr);
                    if (date != null)
                    {
                        rcdAmount.createdDate = date;
                    }
                    amounts.add(rcdAmount);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return amounts;
    }


    public void deleteAllPostsAndUsers() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_POSTS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }
    public void deleteMainEntry(String postId) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_AMOUNTS, KEY_POST_AMOUNT_ID +  " = ?", new String[]{postId});
            db.delete(TABLE_POSTS, KEY_POST_ID +  " = ?", new String[]{postId});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts");
        } finally {
            db.endTransaction();
        }
    }
    public void deleteAmountGot(String amountId) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_AMOUNTS, KEY_AMOUNT_ID +  " = ?", new String[]{amountId});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all amounts");
        } finally {
            db.endTransaction();
        }
    }
}
package com.example.andri.trueorfalse1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Random;

public class DB {

    private String[] facts = {
            "На футбольному полі одночасно може бути 6 суддей",
            "На футбольному полі одночасно може знаходитись 11 гравців",
            "У футболі гра скалдається з 4 таймів",
            "У футболі команді зараховують поразку, якщо вилучено 4 граваців",
            "Переможцем Чемпіонату світу з футболу 2010 року є Німеччина",

            "Автомобіль з дизельним двигуном може працювати на бензині",
            "Вартість найдорожчого автомобіля - 56 млн доларів",
            "Автомобіль може проїхати без топлива 5 км",
            "Найбільша швидкість автомобіля - 547 км/год",
            "Бензиновий двигун може працювати на газі",

            "Греція знаходиться у східній Європі",
            "Німеччина межує з Францією",
            "Береги України омивають 3 моря",
            "Еверест знаходиться у Північній Америці",
            "Кіліманджаро - найвища точка Африки над рівнем моря"
    };
    private int[] answers = {
            1, 0, 0, 0, 0,
            0, 1, 0, 0, 1,
            0, 1, 0, 0, 1
    };
    private String[] categories = {
            "Футбол",
            "Автомобіль",
            "Географія"
    };
    private String[] mods = {
            "Survival",
            "Time"
    };
    private int[] categoriesId = {
            1, 1, 1, 1, 1,
            2, 2, 2, 2, 2,
            3, 3, 3, 3, 3
    };

    private static final String DB_NAME = "FactsDB";
    private static final int DB_VERSION = 1;
    private static final String FACTS_TABLE_NAME = "Fact";
    private static final String CATEGORIES_TABLE_NAME = "Category";
    private static final String BEST_SCORE_TABLE_NAME = "BestScore";

    public static final String FACT_COLUMN_ID = "_id";
    public static final String FACT_COLUMN_FACT = "fact";
    public static final String FACT_COLUMN_ANSWER = "answer";
    public static final String FACT_COLUMN_CATEGORY_ID = "category_id";
    public static final String CATEGORY_COLUMN_ID = "_id";
    public static final String CATEGORY_COLUMN_CATEGORY = "category";
    public static final String BEST_SCORE_COLUMN_ID = "_id";
    public static final String BEST_SCORE_COLUMN_MOD = "mod";
    public static final String BEST_SCORE_COLUMN_SCORE = "score";

    private static final String FACT_TABLE_CREATE = "create table " + FACTS_TABLE_NAME + "(" +
            FACT_COLUMN_ID + " integer primary key autoincrement," +
            FACT_COLUMN_FACT + " text," +
            FACT_COLUMN_ANSWER + " integer," +
            FACT_COLUMN_CATEGORY_ID + " integer" + ");";
    private static final String CATEGORY_TABLE_CREATE = "create table " + CATEGORIES_TABLE_NAME + "(" +
            CATEGORY_COLUMN_ID + " integer primary key autoincrement," +
            CATEGORY_COLUMN_CATEGORY + " text" + ");";
    private static final String BEST_SCORE_TABLE_CREATE = "create table " + BEST_SCORE_TABLE_NAME + "(" +
            BEST_SCORE_COLUMN_ID + " integer primary key autoincrement," +
            BEST_SCORE_COLUMN_MOD + " text," +
            BEST_SCORE_COLUMN_SCORE + " integer" + ");";

    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

    public DB(Context context){
        this.context = context;
    }

    public void open(){
        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public void close(){
        if(dbHelper!=null){
            dbHelper.close();
        }
    }

    public Cursor gectFactOfCategory(String[] args){
        Cursor cursor;
//        String catFactQuery = "select _id, fact, answer, category from Fact fac join Category cat" +
//                " on fac.category_id = cat._id";
        Random random = new Random();
        int catId = random.nextInt(args.length);
        String table = "Fact as fac join Category as cat on fac.category_id = cat._id";
        String[] columns = {"fac.fact", "fac.answer", "cat.category"};
        String selection = "category like ?";
        String[] selectionArgs = {args[catId]};
        cursor = sqLiteDatabase.query(table, columns, selection, selectionArgs, null, null, null);
        return cursor;
    }

    public Cursor getFact(int id){
        Cursor cursor;
        String[] columns = {FACT_COLUMN_FACT, FACT_COLUMN_ANSWER};
        String selection = "_id = ?";
        String[] selectionArgs = {String.valueOf(id)};

        cursor = sqLiteDatabase.query(FACTS_TABLE_NAME,
                columns, selection, selectionArgs, null, null, null);

        return cursor;
    }

    public Cursor getBestScore(String mod){
        Cursor cursor = null;
        int bestScore = 0;
        String[] columns = {BEST_SCORE_COLUMN_SCORE};
        String selection = "mod = ?";
        String[] selectionArgs = {mod};
        switch (mod){
            case "Survival":
                cursor = sqLiteDatabase.query(BEST_SCORE_TABLE_NAME,
                        columns, selection, selectionArgs, null, null, null);
                break;
            case "Time":
                cursor = sqLiteDatabase.query(BEST_SCORE_TABLE_NAME,
                        new String[]{BEST_SCORE_COLUMN_SCORE}, "_id = ?", new String[]{"2"}, null, null, null);
                break;
        }
        return cursor;
    }

    public void updateBestScore(int score, String mod){
        ContentValues cv = new ContentValues();
        if(mod.equals("Survival")){
            cv.put(BEST_SCORE_COLUMN_SCORE, score);
            sqLiteDatabase.update(BEST_SCORE_TABLE_NAME, cv, "_id = 1", null);
        }else if(mod.equals("Time")){
            cv.put(BEST_SCORE_COLUMN_SCORE, score);
            sqLiteDatabase.update(BEST_SCORE_TABLE_NAME, cv, "_id = 2", null);
        }
    }

/////////////////////////////////////////////////////////////////////////////////

    class DBHelper extends SQLiteOpenHelper{

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CATEGORY_TABLE_CREATE);
            db.execSQL(FACT_TABLE_CREATE);
            db.execSQL(BEST_SCORE_TABLE_CREATE);

            ContentValues cv = new ContentValues();
            for(int i=0; i<categories.length; i++){
                cv.clear();
                cv.put(CATEGORY_COLUMN_CATEGORY, categories[i]);
                db.insert(CATEGORIES_TABLE_NAME, null, cv);
            }
            for(int i=0; i<facts.length; i++){
                cv.clear();
                cv.put(FACT_COLUMN_FACT, facts[i]);
                cv.put(FACT_COLUMN_ANSWER, answers[i]);
                cv.put(FACT_COLUMN_CATEGORY_ID, categoriesId[i]);
                db.insert(FACTS_TABLE_NAME, null, cv);
            }
            for(int i=0; i<mods.length; i++){
                cv.clear();
                cv.put(BEST_SCORE_COLUMN_MOD, mods[i]);
                cv.put(BEST_SCORE_COLUMN_SCORE, 2);
                db.insert(BEST_SCORE_TABLE_NAME, null, cv);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}






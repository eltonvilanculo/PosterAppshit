package com.example.nameless.posterappshit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SUtui on 5/24/2018.
 */

public class PostHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "postar.db";
    public static final int DATABASE_VERSION = 12;

    public static final String TABLE_POST = "Post";
    public static final String COLUNA_IMAGEM_URI_POST = "imagem_uri";
    public static final String COLUNA_TEXTO_POST = "texto";
    public static final String COLUNA_EMISSOR_POST = "emissor";
    public static final String COLUNA_DATA_POST = "data";
    public static final String COLUNA__TOTAL_RATE_POST = "total_rating";
    public static final String COLUNA_RATE_SUM_POST = "rate_sum";
    public static final String COLUNA_EXT_POST = "ext";
    public static final String _COLUNA_ID_POST = "_id_mens";
    public static final String COLUNA_TIPO_POST = "tipo";
    public static final String COLUNA_RECEPTOR_POST = "receptor";


    public static final String TABLE_USER = "pessoa";
    public static final String _COLUNA_FIREBASE_ID_PES = "_id";
    public static final String COLUNA_USERNAME_PES = "username";
    public static final String COLUNA_EMAIL_PES = "email";
    public static final String COLUNA_PASSWORD_PES = "password";
    public static final String COLUNA_FOTO_URL_PES = "foto";


    public PostHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_POSTAGEM = "CREATE TABLE " + TABLE_POST + " ( "
                + _COLUNA_ID_POST + " VARCHAR (100) PRIMARY KEY, "
                + COLUNA_DATA_POST + " INTEGER, "
                + COLUNA_IMAGEM_URI_POST + " VARCHAR(500), "
                + COLUNA_TEXTO_POST + " VARCHAR(100), "
                + COLUNA_RATE_SUM_POST + " INTEGER, "
                + COLUNA__TOTAL_RATE_POST + " INTEGER, "
                + COLUNA_EMISSOR_POST + " VARCHAR(100),"
                + COLUNA_RECEPTOR_POST + " VARCHAR(100),"
                + COLUNA_TIPO_POST + " VARCHAR(15)"
                + ");";

        String CREATE_TABLE_PESSOA = "CREATE TABLE " + TABLE_USER + " ( "
                + _COLUNA_FIREBASE_ID_PES + " VARCHAR (50) PRIMARY KEY, "
                + COLUNA_EMAIL_PES + "  VARCHAR(100), "
                + COLUNA_USERNAME_PES + " VARCHAR(150),"
                + COLUNA_PASSWORD_PES + " VARCHAR(100),"
                + COLUNA_FOTO_URL_PES +
                " VARCHAR(500)"
                + ");";

        db.execSQL(CREATE_TABLE_POSTAGEM);
        db.execSQL(CREATE_TABLE_PESSOA);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_USER);
        db.execSQL("DROP TABLE " + TABLE_POST);

        onCreate(db);

    }


}

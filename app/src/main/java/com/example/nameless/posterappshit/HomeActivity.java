package com.example.nameless.posterappshit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    private DatabaseReference userRef;
    LinearLayout layoutprincipal;
    private final int IMAGEMGALERIA = 1;
    private final int IMAGEMCAMERA = 2;
    Uri enderecoImagem;
    ProgressBar progressBar;
    ArrayList<Post> listaDownload;
    // Inicializacao do local onde ira ficar a foto carregada
    Post post;
    // Cloud Storage
    PostAdapter postAdapter;
    UserAdapter userAdapter;
    AlertDialog.Builder alertBuilder;
    AlertDialog alertDialog;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private User user;

    //Verifica se o upload esta' completo na base de dado
    StorageTask myUploadTask;


    // --------------------------------------
    ImageView imagemCarregada;
    private SQLiteDatabase dataHelper;
    private String ANONIMO = "anonimo";
    private String QUALQUER = "qualquer";


    @SuppressLint({"ResourceAsColor", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Cloud Storage 
        userRef = FirebaseDatabase.getInstance().getReference(BDCaminhos.USER);
        mStorageRef = FirebaseStorage.getInstance().getReference(BDCaminhos.FOLDERFIREBASEIMAGE);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(BDCaminhos.FOLDERFIREBASEIMAGE);

        // Toolbar config

        if (!LoginActivity.firebaseUser.isAnonymous()) {
            userRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    user = dataSnapshot.child(LoginActivity.firebaseUser.getUid()).getValue(User.class);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        layoutprincipal = (LinearLayout) findViewById(R.id.layoutPrincipal);
        setContentView(R.layout.activity_home);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBarFeed);
        toolbar.setTitle(" FAJ Five");
        //toolbar.setTitleTextColor(R.color.colorT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10f);
            toolbar.setFocusable(true);
        }


        toolbar.inflateMenu(R.menu.menu_toolbar_config);
        // Framelayout
        /**
         *criaco da porra da ligacao com a motherfucking SQ fuckin Lite b!tch!!!
         */

        PostHelper helper = new PostHelper(this);
        dataHelper = helper.getWritableDatabase();
        // codigo para linkar

        //Adapter e RVIew
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        userAdapter = new UserAdapter(this);
        recyclerView.setAdapter(userAdapter);

        //Layout Manager e RVIew

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());


        // codigo para linkar Post

        //Adapter e RVIew
        RecyclerView recycleFeed = (RecyclerView) findViewById(R.id.recycleFeed);
        if (LoginActivity.firebaseUser.isAnonymous()) {
            postAdapter = new PostAdapter(this, getAnonimoCursor());
        } else {
            postAdapter = new PostAdapter(this, getRegistadoCursor());
        }


        recycleFeed.setAdapter(postAdapter);

        RecyclerView recycle5 = (RecyclerView) findViewById(R.id.recycle5);

        //PostAdapter postAdapter5 = new PostAdapter(this, getRegistadoCursor());

        //recycle5.setAdapter(postAdapter);
        //Layout Manager e RVIew

        LinearLayoutManager linearLayoutManagerFeed = new LinearLayoutManager(this);
        linearLayoutManagerFeed.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager linearLayoutManagerFeed5 = new LinearLayoutManager(this);
        linearLayoutManagerFeed5.setOrientation(LinearLayoutManager.VERTICAL);
        recycleFeed.setLayoutManager(linearLayoutManagerFeed);
        recycle5.setLayoutManager(linearLayoutManagerFeed5);

        recycleFeed.setItemAnimator(new DefaultItemAnimator());
        recycle5.setItemAnimator(new DefaultItemAnimator());


        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec aba1 = tabHost.newTabSpec("PRIMEIRA");
        aba1.setContent(R.id.feeding_area);
        aba1.setIndicator("FAJ NOTICIAS");


        TabHost.TabSpec aba2 = tabHost.newTabSpec("SEGUNDA");
        aba2.setContent(R.id.SEGUNDA);
        aba2.setIndicator("FAJ AMIGOS");

        TabHost.TabSpec aba3 = tabHost.newTabSpec("TERCEIRA");
        aba3.setContent(R.id.feeding5);
        aba3.setIndicator("FAJ 5");

        tabHost.addTab(aba1);
        tabHost.addTab(aba2);
        tabHost.addTab(aba3);


        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User value = dataSnapshot.getValue(User.class);
                userAdapter.add(value);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Erro: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        mDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post value = dataSnapshot.getValue(Post.class);
                System.out.println("value = " + value.toString());
                meteNoSQL(value, dataSnapshot.getKey());
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void meteNoSQL(Post post, String chave) {
        ContentValues cv69 = new ContentValues();
        cv69.put(PostHelper._COLUNA_ID_POST, chave);
        cv69.put(PostHelper.COLUNA_DATA_POST, post.getDate());
        cv69.put(PostHelper.COLUNA_TEXTO_POST, post.getText());
        cv69.put(PostHelper.COLUNA_EMISSOR_POST, post.getSender());
        cv69.put(PostHelper.COLUNA_RECEPTOR_POST, post.getReceptor());
        cv69.put(PostHelper.COLUNA_IMAGEM_URI_POST, post.getPhotoUri());

        dataHelper.insertWithOnConflict(PostHelper.TABLE_POST, null, cv69, SQLiteDatabase.CONFLICT_REPLACE);


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public void verificarPerfil(MenuItem item) {
        if (!LoginActivity.firebaseUser.isAnonymous()) {
            item.setTitle(user.getUsername());

        } else {
            item.setTitle(ANONIMO);
        }
    }


    public void verificarUs(MenuItem item) {
        String url = "https://web.facebook.com/FAJ-161044741417431/?modal=admin_todo_tour";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void verificarLogout(MenuItem item) {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);

    }


    public void postarMensagem(View view) {


        alertBuilder = new AlertDialog.Builder(HomeActivity.this);
        alertBuilder.setTitle("FAJ aqui !! ");

        // Estou a meter tudo num objecto de View para depois por no dialog

        View customView = getLayoutInflater().inflate(R.layout.dialog_layout, null, false);
        // Inicializacao do imageView que ficara a imagem carregada
        imagemCarregada = customView.findViewById(R.id.imagemCarregada);

        // Inicializacao dos botoes do post
        ImageButton accept = customView.findViewById(R.id.accept_dialog_btn);
        ImageButton acessoGaleria = customView.findViewById(R.id.acessoGaleria);
        ImageButton acessoCamera = customView.findViewById(R.id.acessoCamera);
        //ListView listDialogContact = customView.findViewById(R.id.list_view_contacts_dialog);
        final EditText text = customView.findViewById(R.id.input_text_dialog_post);

        //progress bar
        progressBar = customView.findViewById(R.id.progressBarUpload);


        //listDialogContact.setAdapter(userAdapter);

        alertBuilder.setView(customView);
        alertDialog = alertBuilder.create();
        alertDialog.show();

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post = new Post();
                post.setText(text.getEditableText().toString());
                post.setDate(System.currentTimeMillis());


                if (LoginActivity.firebaseUser.isAnonymous()) {
                    post.setSender(ANONIMO);
                } else {
                    post.setSender(user.getUsername());
                }

                if (enderecoImagem != null) {
                    if (myUploadTask != null && myUploadTask.isInProgress()) {
                        Toast.makeText(HomeActivity.this, "Processando...", Toast.LENGTH_SHORT).show();
                    } else uploadImagem();
                } else postarParaFireBase(null);
            }
        });


        acessoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (intentCamera.resolveActivity(getPackageManager()) != null) {

                    startActivityForResult(intentCamera, IMAGEMCAMERA);
                }
            }
        });

        acessoGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMAGEMGALERIA);
                //PEDIR identifica o nosso pedido
            }
        });


    }

    private String getPostSender() {
        for (User user : userAdapter.getList()) {
            if (user.getUid().equals(LoginActivity.firebaseUser.getUid())) {
                return user.getUsername();
            }
        }
        Toast.makeText(this, "Usuario nao Existe!", Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == IMAGEMGALERIA && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Aqui o uri ja tem o caminho
            enderecoImagem = data.getData();
            // Picasso vai converter e por no image view

            Picasso.with(this).load(enderecoImagem).into(imagemCarregada);


        }
    }

    void uploadImagem() {

        if (enderecoImagem != null) {

            StorageReference fileRef = mStorageRef.child("FAJImage" + System.currentTimeMillis() + "." + retornaFormato(enderecoImagem));
            myUploadTask = fileRef.putFile(enderecoImagem).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    postarParaFireBase(taskSnapshot.getDownloadUrl());
                    progressBar.setProgress(0);


                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(HomeActivity.this, "Falha de rede ", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progresso = 100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                    progressBar.setProgress((int) progresso);

                }
            });
        }

    }

    private void postarParaFireBase(@NonNull Uri downloadUrl) {
        if (post != null) {
            if (downloadUrl != null) {
                post.setPhotoUri(downloadUrl.toString());
            }
            mDatabaseRef.child(new Date(System.currentTimeMillis()).toString()).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    alertDialog.hide();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(HomeActivity.this, "Falha ao Enviar", Toast.LENGTH_SHORT).show();
                    //alertDialog.hide();
                }
            });
            post = null;
        }
    }

    private String retornaFormato(Uri uri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private Cursor getAnonimoCursor() {
        String[] args = {ANONIMO};
        return dataHelper.query(PostHelper.TABLE_POST,
                null,
                PostHelper.COLUNA_EMISSOR_POST + " = ? ",
                args,
                null,
                null,
                PostHelper.COLUNA_DATA_POST + " DESC");
    }


    private Cursor getRegistadoCursor() {
        Cursor cursor = dataHelper.query(PostHelper.TABLE_POST,
                null,
                null,
                null,
                null,
                null,
                PostHelper.COLUNA_DATA_POST + "  DESC");
        return cursor;
    }
}

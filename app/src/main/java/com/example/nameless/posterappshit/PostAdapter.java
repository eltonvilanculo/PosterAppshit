package com.example.nameless.posterappshit;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Nameless on 5/23/2018.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    private Cursor cursor;
    private LayoutInflater inflater;
    Context context;
    ArrayList<Post> posts = new ArrayList<>();


    public PostAdapter(List<Post> listaPosts, Context context) {
        this.context = context;
    }

    public PostAdapter(Context context, Cursor cursor) {

        this.inflater = LayoutInflater.from(context);
        this.cursor = cursor;
    }

    public void addicionaPost(Post post) {
        posts.add(post);
        notifyDataSetChanged();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cardview_item, parent, false);
       // PostAdapter.MyViewHolder holder = new PostAdapter.MyViewHolder(view);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostAdapter.MyViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            Picasso.with(inflater.getContext()).load(cursor.getString(cursor.getColumnIndex(PostHelper.COLUNA_IMAGEM_URI_POST))).into(holder.imageProfile);
            holder.nomeUsuario.setText(cursor.getString(cursor.getColumnIndex(PostHelper.COLUNA_EMISSOR_POST)));
            holder.statusFoto.setText(cursor.getString(cursor.getColumnIndex(PostHelper.COLUNA_TEXTO_POST)));
        }


    }


    @Override
    public int getItemCount() {
        return cursor.getCount();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageProfile;
        TextView nomeUsuario;
        TextView statusFoto;


        public MyViewHolder(View view) {
            super(view);

            nomeUsuario = (TextView) itemView.findViewById(R.id.emissor_messagem);
            imageProfile = (ImageView) itemView.findViewById(R.id.foto_messagem);
            statusFoto = (TextView) itemView.findViewById(R.id.texto_messagem);

        }

        void trocarCursor(Cursor novoCursor){
            if (cursor != null) {
                cursor.close();
            }

            cursor = novoCursor;

            if(novoCursor!=null){
                notifyDataSetChanged();
            }
        }


    }
}

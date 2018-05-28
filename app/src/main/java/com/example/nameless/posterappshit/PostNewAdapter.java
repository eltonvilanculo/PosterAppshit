package com.example.nameless.posterappshit;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Vector;

/**
 * Created by Nameless on 5/23/2018.
 */

public class PostNewAdapter extends RecyclerView.Adapter<PostNewAdapter.MyViewHolder> {

    private List<Post> listaPosts;
    private LayoutInflater inflater;
    Context context;

    public PostNewAdapter(List<Post> listaPosts, Context context) {
        this.listaPosts = listaPosts;
        this.context = context;
    }

    public PostNewAdapter(Context context) {
        this.listaPosts = new Vector<>();
        this.inflater = LayoutInflater.from(context);
    }
    public void add(Post post){
        listaPosts.add(post);
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cardview_item,parent,false);
         MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PostNewAdapter.MyViewHolder holder, int position) {
        Post postActual = listaPosts.get(position);
        holder.setData(postActual,position);
    }

    @Override
    public int getItemCount() {
        return listaPosts.size();
    }
    public List<Post> getList() {
        return listaPosts;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageProfile ;
        TextView nomeUsuario, statusFoto;
        private int position;
        Post postActual ;

        public MyViewHolder(View view) {
            super(view);

            nomeUsuario = (TextView) itemView.findViewById(R.id.emissor_messagem);
            imageProfile = (ImageView) itemView.findViewById(R.id.foto_messagem);
            statusFoto = (TextView) itemView.findViewById(R.id.texto_messagem);

        }


        public void setData(Post postActual, int position) {

            this.nomeUsuario.setText(postActual.getSender());
            this.statusFoto.setText(postActual.getText());
            if (postActual.getPhotoUri() != null) {

                Picasso.with(context).load(postActual.getPhotoUri())
                        .fit()
                        .centerCrop()
                        .into(imageProfile);

                //this.imageProfile.setImageURI(Uri.parse(postActual.getPhotoUri()));
            }



            this.position = position;
            this.postActual = postActual;
//                notifyDataSetChanged();

            //listaUsuarios.add(position,usaurioActual);



        }
    }
}

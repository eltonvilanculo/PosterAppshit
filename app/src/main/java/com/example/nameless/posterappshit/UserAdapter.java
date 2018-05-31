package com.example.nameless.posterappshit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Vector;

/**
 * Created by Nameless on 5/21/2018.
 */

// MyViewHolder esta dentro do UserAdapter

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {


    private List<User> listaUsuarios;
    private LayoutInflater inflater;
    Context context;

    // Construtor de meu adapter
    public UserAdapter(Context context) {
        this.listaUsuarios = new Vector<>();
        inflater = LayoutInflater.from(context);
    }

    public UserAdapter(Context context, List<User> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
        this.context = context;
    }

    public void add(User usuario){
        listaUsuarios.add(usuario);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = inflater.inflate(R.layout.list_item,parent,false);
      MyViewHolder holder = new MyViewHolder(view);
      return holder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        User usaurioActual = listaUsuarios.get(position);
        holder.setData(usaurioActual,position);
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public List<User> getList() {
        return listaUsuarios;
    }

    class MyViewHolder extends  RecyclerView.ViewHolder{

        // Inicializar componentes

        ImageView imageProfile ;
        TextView nomeUsuario;
        private int position;
        User usuarioActual ;



            public MyViewHolder(View itemView) {
                super(itemView);

                nomeUsuario = (TextView) itemView.findViewById(R.id.nomeUsuario);
                imageProfile = (ImageView) itemView.findViewById(R.id.imageProfile);


            }

        public void setData(User usaurioActual, int position) {

                this.nomeUsuario.setText(usaurioActual.getUsername());
            if (usaurioActual.getPhotoUri() != null) {
                this.imageProfile.setImageURI(usaurioActual.getPhotoUri());
            }

                this.position = position;
                this.usuarioActual = usaurioActual;
//                notifyDataSetChanged();

                //listaUsuarios.add(position,usaurioActual);



        }


    }

}

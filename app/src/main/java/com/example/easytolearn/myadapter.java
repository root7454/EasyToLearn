package com.example.easytolearn;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class myadapter extends FirebaseRecyclerAdapter<stored_data,myadapter.myviewholder>
{
    public myadapter(@NonNull FirebaseRecyclerOptions<stored_data> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, final int position, @NonNull stored_data model)
    {
       holder.name.setText(model.getName());
       holder.password.setText(model.getPassword());
       holder.email.setText(model.getEmail());
       holder.edit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               final DialogPlus dialogPlus= DialogPlus.newDialog(holder.name.getContext())
                       .setContentHolder(new ViewHolder(R.layout.dialogcontent))
                       .setExpanded(true,1100)
                       .create();

               View myview=dialogPlus.getHolderView();

               EditText name_update = myview.findViewById(R.id.name_update);
               EditText email_update = myview.findViewById(R.id.email_update);
               EditText pass_update = myview.findViewById(R.id.password_update);
               Button submit = myview.findViewById(R.id.submit_update);

               name_update.setText(model.getName());
               email_update.setText(model.getEmail());
               pass_update.setText(model.getPassword());

               dialogPlus.show();

               submit.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Map<String,Object> map=new HashMap<>();
                       map.put("name",name_update.getText().toString());
                       map.put("email",email_update.getText().toString());
                       map.put("password",pass_update.getText().toString());

                       FirebaseDatabase.getInstance().getReference().child("users")
                               .child(getRef(holder.getBindingAdapterPosition()).getKey()).updateChildren(map)
                               .addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void unused) {
                                       dialogPlus.dismiss();

                                   }
                               })
                               .addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       dialogPlus.dismiss();

                                   }
                               });
                   }
               });


           }
       });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.img.getContext());
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete the user");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("users")
                                .child(getRef(holder.getBindingAdapterPosition()).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();
            }
        });

//       Glide.with(holder.img.getContext()).load(model.getPurl()).into(holder.img);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.userdata,parent,false);
       return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        CircleImageView img;
        Button edit,delete;
        TextView name,password,email;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            img=(CircleImageView)itemView.findViewById(R.id.img1);
            name=(TextView)itemView.findViewById(R.id.Name);
            email =(TextView)itemView.findViewById(R.id.Email);
            password=(TextView)itemView.findViewById(R.id.Password);
            edit =(Button)itemView.findViewById(R.id.edit_user);
            delete =(Button)itemView.findViewById(R.id.delete_user);
        }
    }
}

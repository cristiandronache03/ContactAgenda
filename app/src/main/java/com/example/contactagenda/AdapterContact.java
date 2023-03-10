package com.example.contactagenda;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class AdapterContact extends RecyclerView.Adapter<AdapterContact.ContactViewHolder>
{

    private Context context;
    private ArrayList<Contact> contactList;
    private DBHandler dbHandler;



    public AdapterContact(Context context, ArrayList<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_contact_item,parent,false);
        ContactViewHolder vh = new ContactViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        Contact contact = contactList.get(position);

        int id = contact.getId();
        String image = contact.getImage();
        String name = contact.getName();
        String number = contact.getNumber();
        String email = contact.getEmail();
        String organization = contact.getOrganization();
        String relationship = contact.getRelationship();

        dbHandler = new DBHandler(context);

        //note, database extracts null as string for null values

        holder.contactName.setText(name);
        if(image != null && !image.equals("null")){
            holder.contactImage.setImageURI(Uri.parse(image));
        }else {
            holder.contactImage.setImageResource(R.drawable.baseline_person_24);
        }

        holder.contactDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContactDetails.class);
                intent.putExtra("contactId",""+id);
                context.startActivity(intent);
            }
        });

        holder.contactEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AddContact.class);

                intent.putExtra("id",""+id);
                intent.putExtra("name",name);
                intent.putExtra("phone",number);
                intent.putExtra("image",image);
                intent.putExtra("email",email);
                intent.putExtra("organization",organization);
                intent.putExtra("relationship",relationship);

                intent.putExtra("isEditMode",true);

                context.startActivity(intent);

            }
        });

        holder.contactDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.deleteContact(contact);
                ((MainActivity)context).onResume();
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder{

        ImageView contactImage,contactDial,contactEdit,contactDelete;
        TextView contactName;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            contactImage = itemView.findViewById(R.id.contact_image);
            contactDial = itemView.findViewById(R.id.contact_number_dial);
            contactName = itemView.findViewById(R.id.contact_name);
            contactEdit = itemView.findViewById(R.id.editIv);
            contactDelete = itemView.findViewById(R.id.deleteIv);
        }
    }
}

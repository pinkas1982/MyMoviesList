package com.myapps.pinkas.recyclerviewdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pinkas on 7/14/2016.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.Holder> {

    private List<ListItems> listItemsList;
    private Context context;

    public ListAdapter(Context context, List<ListItems> listItemsList) {
        this.context = context;
        this.listItemsList = listItemsList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent, false);
        Holder holder = new Holder(view);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        });


        return  holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ListItems listItems = listItemsList.get(position);
        holder.getLayoutPosition();
        holder.title.setText(listItems.getTitle());
        holder.title2.setText(listItems.getTitle2());
        holder.title3.setText(listItems.getTitle3());
      //  holder.imageView.
     }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView title2;
        private TextView title3;
        private ImageView imageView;
        private RelativeLayout relativeLayout;


        public Holder(View itemView) {
            super(itemView);
            this.imageView = (ImageView)itemView.findViewById(R.id.imageView);
            this.relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relLayout);
            this.title = (TextView)itemView.findViewById(R.id.title_tv);
            this.title2 = (TextView)itemView.findViewById(R.id.some_tv);
            this.title3 = (TextView)itemView.findViewById(R.id.tv_text3);
        }
    }
}

package com.example.android.tflitecamerademo.model;

import android.content.res.AssetManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.tflitecamerademo.R;

import java.util.ArrayList;

public class PoseRecyclerAdapter  extends RecyclerView.Adapter<PoseRecyclerAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<PoseList> listPose = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pose_item_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listPose.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listPose.size();
    }
    public void addItem(PoseList pose) {
        listPose.add(pose);
    }


    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView textView1;
        private TextView textView2;
        private TextView textView3;
        private TextView textView4;
        private TextView textView5;
        private TextView textView6;
        private ImageView skeleton;
        private ImageView model;

        ItemViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            textView5 = itemView.findViewById(R.id.textView5);
            textView6 = itemView.findViewById(R.id.textView6);
            skeleton = itemView.findViewById(R.id.skeleton);
            model = itemView.findViewById(R.id.model);
        }

        void onBind(PoseList data) {
            title.setText(data.getTitle());
            textView1.setText(String.valueOf(data.getAngles().get(0)));
            textView2.setText(String.valueOf(data.getAngles().get(1)));
            textView3.setText(String.valueOf(data.getAngles().get(2)));
            textView4.setText(String.valueOf(data.getAngles().get(3)));
            textView5.setText(String.valueOf(data.getAngles().get(4)));
            textView6.setText(String.valueOf(data.getAngles().get(5)));
        }
    }
}

package com.zyc.buslist;


import androidx.annotation.ColorInt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BusListAdapter extends RecyclerView.Adapter<BusListAdapter.ViewHolder> {

    private List<BusStation> mList;
    private int mCurrentSelected = -1;

    //记录不同字数时的字体大小
    Map<Integer, Float> textSizeHashMap = new HashMap<Integer, Float>();

    //region 单击回调事件
    //点击 RecyclerView 某条的监听
    public interface OnItemClickListener {
        /**
         * 当RecyclerView某个被点击的时候回调
         *
         * @param view     点击item的视图
         * @param position 在adapter中的位置
         * @param data     点击得到的数据
         */
        void onItemClick(View view, int position, String data);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //endregion
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvArrive;
        TextView tvPass;
        TextView name;
        TextView dot;
        View lineRight;
        View lineleft;
        LinearLayout ll_metor;

        public ViewHolder(View view) {
            super(view);
            tvArrive = view.findViewById(R.id.tv_arrive);
            tvPass = view.findViewById(R.id.tv_pass);
            name = view.findViewById(R.id.name);
            dot = view.findViewById(R.id.tv_dot);
            lineRight = view.findViewById(R.id.line_right);
            lineleft = view.findViewById(R.id.line_left);
            ll_metor = view.findViewById(R.id.ll_metor);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, getLayoutPosition(), mList.get(getLayoutPosition()).getName());
                    }
                }
            });
        }
    }

    public BusListAdapter(List<BusStation> iconList) {
        mList = iconList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bus_station, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        BusStation busStation = mList.get(position);

        if (mCurrentSelected < 0) mCurrentSelected = getItemCount() - 1;

        //region 显示站名,并自动调整字体大小
        holder.name.setText(busStation.getName());
        final TextView v = holder.name;
        if (textSizeHashMap.get(v.getText().length()) != null)
            v.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeHashMap.get(v.getText().length()));
        else v.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50);

        holder.name.post(new Runnable() {
            public void run() {
                if (v.getTop() + v.getHeight() > ((ConstraintLayout) v.getParent()).getHeight()) {
                    v.setTextSize(TypedValue.COMPLEX_UNIT_PX, v.getTextSize() - 1);
                    v.post(this);
                } else if (!textSizeHashMap.containsKey(v.getText().length())) {
                    textSizeHashMap.put(v.getText().length(), v.getTextSize());
                }
            }
        });
        //endregion

        //region 首位不显示线条
        if (position == 0) {
            holder.lineleft.setVisibility(View.INVISIBLE);
        } else if (position == mList.size() - 1) {
            holder.lineRight.setVisibility(View.INVISIBLE);
        } else {
            holder.lineleft.setVisibility(View.VISIBLE);
            holder.lineRight.setVisibility(View.VISIBLE);
        }
        //endregion

        //region 根据位置设置颜色
        if (position == mCurrentSelected) {
            holder.name.setTextColor(0xfffe871f);
            holder.dot.setText("0");
            holder.dot.setBackgroundResource(R.drawable.ic_dot_selected_24dp);
            holder.lineRight.setBackgroundColor(0xfff2f2f2);
            holder.lineleft.setBackgroundColor(0xffffe7a1);
        } else if (position < mCurrentSelected) {
            holder.name.setTextColor(0xff000000);
            holder.dot.setText(String.valueOf(mCurrentSelected - position));
            holder.dot.setBackgroundResource(R.drawable.ic_dot_selected_24dp);
            holder.lineRight.setBackgroundColor(0xffffe7a1);
            holder.lineleft.setBackgroundColor(0xffffe7a1);
        } else if (position > mCurrentSelected) {
            holder.name.setTextColor(0xff999999);
            holder.dot.setBackgroundResource(R.drawable.ic_dot_unselected_24dp);
            holder.dot.setText(String.valueOf(position - mCurrentSelected));
            holder.lineRight.setBackgroundColor(0xfff2f2f2);
            holder.lineleft.setBackgroundColor(0xfff2f2f2);
        }
        //endregion

        //region 设置车图标显示

        holder.tvArrive.setVisibility(busStation.getArrive() == 0 ? View.INVISIBLE : View.VISIBLE);
        holder.tvPass.setVisibility(busStation.getPass() == 0 ? View.INVISIBLE : View.VISIBLE);

//        if (busStation.getArrive() > 1) {
        holder.tvArrive.setText((busStation.getArrive() > 1) ? String.valueOf(busStation.getArrive()) : "");
        if (busStation.getArriveDoubleDeck() < busStation.getArrive()) {
            holder.tvArrive.setBackgroundResource(position > mCurrentSelected ? R.drawable.ic_bus_unselected_24dp : R.drawable.ic_bus_station_selected_24dp);
        } else {
            holder.tvArrive.setBackgroundResource(position > mCurrentSelected ? R.drawable.ic_bus_double_unselected_24dp : R.drawable.ic_bus_double_station_selected_24dp);
        }
//        } else {
//            holder.tvArrive.setText("");
//            if (busStation.getArriveDoubleDeck() < 1 || busStation.getArriveDoubleDeck() < busStation.getArrive()) {
//
//            } else {
//                holder.tvArrive.setBackgroundResource(position > mCurrentSelected ? R.drawable.ic_bus_unselected_24dp : R.drawable.ic_bus_station_selected_24dp);
//            }
//        }

        if (busStation.getPass() > 1) {
            holder.tvPass.setText(String.valueOf(busStation.getPass()));
            if (busStation.getPassDoubleDeck() < busStation.getPass()) {
                holder.tvPass.setBackgroundResource(position >= mCurrentSelected ? R.drawable.ic_bus_num_unselected_24dp : R.drawable.ic_bus_num_selected_24dp);
            } else {
                holder.tvPass.setBackgroundResource(position >= mCurrentSelected ? R.drawable.ic_bus_double_num_unselected_24dp : R.drawable.ic_bus_double_num_selected_24dp);
            }
        } else {
            holder.tvPass.setText("");
            if (busStation.getPassDoubleDeck() > 0) {
                holder.tvPass.setBackgroundResource(position >= mCurrentSelected ? R.drawable.ic_bus_double_unselected_24dp : R.drawable.ic_bus_double_selected_24dp);
            } else {
                holder.tvPass.setBackgroundResource(position >= mCurrentSelected ? R.drawable.ic_bus_unselected_24dp : R.drawable.ic_bus_selected_24dp);
            }
        }
        //endregion


        //region 显示地铁相关信息
        try {
            @ColorInt int motor_color[] = {
                    0xff000000,
                    0xff3080B7,  //1号线
                    0xffEB81B9,  //2号线
                    0xffDAC17D,  //3号线
                    0xff86B81C,  //4号线
                    0xffB85A4E,  //5号线
                    0xff018237,  //6号线
                    0xffEE782E,  //7号线
                    0xff99ADAC,  //8号线
                    0xff000000,  //9号线 无
                    0xff000000,  //10号线 无
                    0xffFCD600,  //11号线
                    0xff000000,  //12号线 无
                    0xff000000,  //13号线 无
                    0xff000000,  //14号线 无
                    0xff000000,  //15号线 无
                    0xffD10195,  //16号线
                    0xff000000,  //17号线 无
                    0xff000000,  //18号线 无
                    0xff000000,  //19号线 无
                    0xff000000,  //20号线 无
                    0xffD10195,  //21号线(阳逻线)
            };

            for (int i = 0; holder.ll_metor.getChildCount() > 0; i++) {
                holder.ll_metor.removeViewAt(0);
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 3);

            String metor[] = busStation.getMetro().split(",");
            for (String s : metor) {
                if (s.length() < 1) continue;
                int metor_line = 0;
                try {
                    metor_line = Integer.parseInt(s.replaceAll("\\D", ""));
                    if (metor_line >= motor_color.length) metor_line = 0;
                } catch (NumberFormatException e) {
                    metor_line = 0;
                }
                TextView t = new TextView(holder.name.getContext());
                t.setText(s);
                t.setEms(1);
                t.setMaxEms(1);
                t.setMinEms(1);
                t.setGravity(Gravity.CENTER);
                t.setBackgroundColor(0xff3080B7);
                t.setTextColor(0xffffffff);
                t.setLineSpacing(-10, 1);
                t.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40);
                t.setLayoutParams(params);
                t.setPadding(4, 1, 4, 1);
                GradientDrawable gradientDrawable = (GradientDrawable) (t.getContext().getResources().getDrawable(R.drawable.metor_fillet));
                gradientDrawable.setColor(motor_color[metor_line]);
                t.setBackground(gradientDrawable);
                holder.ll_metor.addView(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //endregion
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void clear() {
        mList.clear();
    }

    public BusStation getItem(int position) {
        return mList.get(position);
    }

    public void setSelected(int s) {
        mCurrentSelected = s;
    }

    public int getSelected() {
        return mCurrentSelected;
    }
}
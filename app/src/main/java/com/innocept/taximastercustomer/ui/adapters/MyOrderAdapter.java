//package com.innocept.taximastercustomer.ui.adapters;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.support.design.widget.Snackbar;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageButton;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.innocept.taximastercustomer.model.foundation.Order;
//
//import java.util.List;
//
///**
// * Created by Dulaj on 08-Mar-16.
// */
//public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder>{
//
//    Context context;
//    private List<Order> orderList;
//
//    public MyOrderAdapter(Context context, List<Order> orderList) {
//        this.context = context;
//        this.orderList = orderList;
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder{
//
//        TextView textViewWorkoutName;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//
//            relativeLayoutSavedWorkouts = (RelativeLayout)itemView.findViewById(R.id.inflater_relativelayout_saved_workouts);
//            imageButtonRemove = (ImageButton)itemView.findViewById(R.id.inflater_imagebutton_remove);
//            textViewWorkoutName = (TextView)itemView.findViewById(R.id.inflater_textview_workout_name);
//        }
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
//        View v = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.inflater_saved_workouts_listview, parent, false);
//        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(0,3,0,3);
//        v.setLayoutParams(layoutParams);
//        ViewHolder vh = new ViewHolder(v);
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, final int position) {
//        final ViewHolder viewHolder = (ViewHolder) holder;
//
//        viewHolder.textViewWorkoutName.setText(workoutList.get(position).getWorkoutName());
//        viewHolder.imageButtonRemove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LayoutInflater inflater = LayoutInflater.from(context);
//                final View view = inflater.inflate(R.layout.inflater_alertdialog_saved_workouts_menu, null);
//                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
//                dialogBuilder.setView(view);
//                final AlertDialog dialog = dialogBuilder.create();
//
//                Button buttonDelete = (Button)view.findViewById(R.id.inflater_button_delete_exercise);
//                buttonDelete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                        builder.setTitle("Delete Workout");
//                        builder.setMessage("Do you want to delete the workout?");
//                        builder.setNegativeButton("No", null);
//                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                new DatabaseHandler(AppContext.getContext()).deleteWorkOut(workoutList.get(position).getId());
//                                workoutList = new DatabaseHandler(AppContext.getContext()).getAllWorkouts();
//                                notifyDataSetChanged();
//                            }
//                        });
//                        builder.create().show();
//                        dialog.dismiss();
//                    }
//                });
//
//                Button buttonRename = (Button)view.findViewById(R.id.inflater_button_rename_exercise);
//                buttonRename.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        LayoutInflater inflater = LayoutInflater.from(context);
//                        final View view = inflater.inflate(R.layout.inflater_alertdialog_workout_name, null);
//
//                        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
//                        dialogBuilder.setTitle("Rename Workout");
//                        dialogBuilder.setView(view);
//
//                        dialogBuilder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                EditText editext = (EditText) view.findViewById(R.id.edittext_alertdialog_add_note);
//                                String name = editext.getText().toString();
//                                if(name==null || name.length()==0){
//                                    Snackbar.make(((Activity)context).findViewById(R.id.viewpager), "Not Renamed. Name cannot be empty!", Snackbar.LENGTH_SHORT).show();
//                                }
//                                else{
//                                    Workout workout = workoutList.get(position);
//                                    workout.setWorkoutName(name);
//                                    DatabaseHandler db = new DatabaseHandler(AppContext.getContext());
//                                    if(!db.renameWorkout(workout.getId(), name)){
//                                        Snackbar.make(((Activity)context).findViewById(R.id.viewpager), "Not Renamed. Name already exists!", Snackbar.LENGTH_SHORT).show();
//                                    }
//                                    else{
//                                        workoutList = db.getAllWorkouts();
//                                        notifyDataSetChanged();
//                                    }
//                                }
//                            }
//                        });
//
//                        dialogBuilder.setNegativeButton("Cancel", null);
//                        dialogBuilder.create().show();
//                        dialog.dismiss();
//                    }
//                });
//
//                dialog.show();
//
//
//
//            }
//        });
//
//        viewHolder.relativeLayoutSavedWorkouts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Workout workout = workoutList.get(position);
//                Intent intent = null;
//                if(workout.getType()==0){
//                    intent = new Intent(context, SetupExercisesActivity.class);
//                    Log.i("Type>>>", "Basic");
//                }
//                else if(workout.getType()==1){
//                    intent = new Intent(context, AdvanceSetupActivity.class);
//                    Log.i("Type>>>", "Advance");
//                }
//                intent.putExtra("workout", new DatabaseHandler(AppContext.getContext()).getWorkout(workout.getId()));
//                context.startActivity(intent);
//                ((Activity)context).finish();
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return workoutList.size();
//    }
//}

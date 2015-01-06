package com.bitdubai.smartwallet.ui.os.android.app.subapp.wallet_runtime.wallet_segment.age.sub_segment.teens.sub_segment.all.developer.bitdubai.version_1.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import com.bitdubai.smartwallet.R;
import com.bitdubai.smartwallet.ui.os.android.app.subapp.wallet_runtime.wallet_framework.version_1.classes.MyApplication;




public  class SendToContactFragment extends android.app.Fragment {


    View rootView;
    ExpandableListView lv;
    private String[] contacts;
    private String[] amounts;
    private String[] whens;
    private String[] notes;
    private String[] pictures;
    private String[][] transactions;
    private String[][] transactions_amounts;
    private String[][] transactions_whens;
    private String[] Discounted;
    private String[] Percentage;



    public static SendToContactFragment newInstance() {
        SendToContactFragment f = new SendToContactFragment();
        return f;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        contacts = new String[]{ "", "Luis Fernando Molina", "Guillermo Villanueva", "Pedro Perrotta", "Mariana Duyos"};
        amounts = new String[]{ "", "$325.00", "$1,400.00", "$0.50", "$25.00"};
        whens = new String[]{ "", "3 min ago", "2 hours ago", "today 9:24 AM", "yesterday"};
        notes = new String[]{"",  "Electricity bill", "Flat rent", "Test address", "More pictures"};
        Percentage = new String[]{"1%","10%","1%","0.8%"};
        Discounted = new String[]{"$3.25","$140.00","$0.05","$0.10"};

        pictures = new String[]{"", "luis_profile_picture", "guillermo_profile_picture", "pedro_profile_picture", "mariana_profile_picture"};

        transactions = new String[][]{

                {},
                {"Electricity bill","New chair","New desk"},
                {"Flat rent","Flat rent","Flat rent","interest paid :(","Flat rent","Car repair","Invoice #2,356 that should have been paid on August"},
                {"Test address"},
                {"More pictures"}
        };

        transactions_amounts = new String[][]{

                {},
                {"$325.00","$55.00","$420.00"},
                {"$1,400.00","$1,200.00","$1,400.00","$40.00","$1,900.00","$10,550.00","$1.00"},
                {"$0.50"},
                {"$25.00"}
        };

        transactions_whens = new String[][]{

                {},
                {"3 min ago","15 min ago","yesterday"},
                {"2 hours ago","yesterday","last Friday","last Friday","14 May 14","11 May 14","5 Jan 14"},
                {"today 9:24 AM"},
                {"yesterday"}
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.wallets_teens_fragment_send_to_contact, container, false);


        String[] tagId = MyApplication.getChildId().split("\\|");
        TextView tv;
//if tagId[1] = -1 no show data
       if (Integer.parseInt(tagId[1]) == -1) {
            tv = (TextView) rootView.findViewById(R.id.notes);
            tv.setTypeface(MyApplication.getDefaultTypeface());

            tv = (TextView) rootView.findViewById(R.id.amount);
            tv.setTypeface(MyApplication.getDefaultTypeface());

//add listener text change, to update discount

           tv.setOnTouchListener(new View.OnTouchListener() {
               public boolean onTouch(View view, MotionEvent event) {
                   if (event.getAction() == MotionEvent.ACTION_DOWN) {
                       int porcen = (int )(Math.random() * 15 + 1);
                       TextView txtpercent = (TextView) rootView.findViewById(R.id.percentage);
                       txtpercent.setText(String.valueOf(porcen) + "%");
                   }
                   return false;
               }
           });


           tv.addTextChangedListener(new TextWatcher() {

               public void onTextChanged(CharSequence s, int start, int before,
                                         int count) {


               }

               public void beforeTextChanged(CharSequence s, int start, int count,
                                             int after) {
                   if(s.length() != 0 )
                   { //do your work here }
                       double porcen = 0;
                       TextView txtpercent = (TextView) rootView.findViewById(R.id.percentage);
                       porcen =  Double.parseDouble(txtpercent.getText().toString().replace("%", ""));

                       double discount =0;

                       if(s.toString() != "")
                           discount = (porcen * Double.parseDouble(s.toString().replace("$", ""))) / 100;

                       TextView txtdiscount = (TextView) rootView.findViewById(R.id.discounted);
                       txtdiscount.setText(String.valueOf(discount));


                   }

               }

               public void afterTextChanged(Editable s) {

               }


           });
           tv.requestFocus();
            tv = (TextView) rootView.findViewById(R.id.contact_name);
            tv.setTypeface(MyApplication.getDefaultTypeface());
           tv.setText(contacts[Integer.parseInt(tagId[0])]);

            tv = (TextView) rootView.findViewById(R.id.percentage);
            tv.setTypeface(MyApplication.getDefaultTypeface());
           int porcen = (int )(Math.random() * 15 + 1);
           tv.setText(String.valueOf(porcen) + "%");

            tv = (TextView) rootView.findViewById(R.id.discounted);
            tv.setTypeface(MyApplication.getDefaultTypeface());
            tv.setText("$0.00");
        }
        else {
            tv = (TextView) rootView.findViewById(R.id.notes);
            tv.setTypeface(MyApplication.getDefaultTypeface());
                tv.setText(transactions[Integer.parseInt(tagId[0])][Integer.parseInt(tagId[1])]);
            tv.setSelected(false);

            tv = (TextView) rootView.findViewById(R.id.amount);
            tv.setTypeface(MyApplication.getDefaultTypeface());
                tv.setText(transactions_amounts[Integer.parseInt(tagId[0])][Integer.parseInt(tagId[1])]);

           //add listener text change, to update discount


          /* tv.setOnTouchListener(new View.OnTouchListener() {
               public boolean onTouch(View view, MotionEvent event) {
                   if (event.getAction() == MotionEvent.ACTION_DOWN) {
                       int porcen = (int )(Math.random() * 15 + 1);
                       TextView txtpercent = (TextView) rootView.findViewById(R.id.percentage);
                       txtpercent.setText(String.valueOf(porcen) + "%");
                   }
                   return false;
               }
           });*/
           tv.addTextChangedListener(new TextWatcher() {

               public void onTextChanged(CharSequence s, int start, int before,
                                         int count) {


               }

               public void beforeTextChanged(CharSequence s, int start, int count,
                                             int after) {

                   if(s.toString().length() != 0 )
                   { //do your work here }
                       double porcen = 0;
                       TextView txtpercent = (TextView) rootView.findViewById(R.id.percentage);
                       porcen =  Double.parseDouble(txtpercent.getText().toString().replace("%", ""));

                       double discount =0;

                     try {
                         discount = (porcen * Double.parseDouble(s.toString().replace("$", ""))) / 100;
                     }
                     catch (Exception ex)
                     {

                     }


                       TextView txtdiscount = (TextView) rootView.findViewById(R.id.discounted);
                       txtdiscount.setText(String.valueOf(discount));


                   }
               }

               public void afterTextChanged(Editable s) {


               }


           });
           tv.requestFocus();

            tv = (TextView) rootView.findViewById(R.id.contact_name);
            tv.setTypeface(MyApplication.getDefaultTypeface());
           tv.setText(contacts[Integer.parseInt(tagId[0])]);

            tv = (TextView) rootView.findViewById(R.id.percentage);
            tv.setTypeface(MyApplication.getDefaultTypeface());
            tv.setText(Percentage[Integer.parseInt(tagId[0])]);


            tv = (TextView) rootView.findViewById(R.id.discounted);
            tv.setTypeface(MyApplication.getDefaultTypeface());
            tv.setText(Discounted[Integer.parseInt(tagId[0])]);
        }


        ImageView profile_picture = (ImageView) rootView.findViewById(R.id.profile_picture);
        switch (Integer.parseInt(tagId[0]))
        {
            case 1:
                profile_picture.setImageResource(R.drawable.luis_profile_picture);

                break;
            case 2:
                profile_picture.setImageResource(R.drawable.guillermo_profile_picture);

                break;
            case 3:
                profile_picture.setImageResource(R.drawable.pedro_profile_picture);
                break;
            case 4:
                profile_picture.setImageResource(R.drawable.mariana_profile_picture);

                break;
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


}
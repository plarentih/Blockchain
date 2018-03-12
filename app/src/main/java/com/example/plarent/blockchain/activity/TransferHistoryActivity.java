package com.example.plarent.blockchain.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.plarent.blockchain.R;
import com.example.plarent.blockchain.adapter.TransferHistoryAdapter;
import com.example.plarent.blockchain.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class TransferHistoryActivity extends AppCompatActivity {

    private ListView listView;
    private TransferHistoryAdapter adapter;
    private List<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_history);
        centerTitle();

        contactList = new ArrayList<>();
        listView = findViewById(R.id.list_view);
        contactList = dummyData();
        adapter = new TransferHistoryAdapter(getApplicationContext(), contactList);
        listView.setAdapter(adapter);

    }

    private void centerTitle() {
        ArrayList<View> textViews = new ArrayList<>();

        getWindow().getDecorView().findViewsWithText(textViews, getTitle(), View.FIND_VIEWS_WITH_TEXT);

        if(textViews.size() > 0) {
            AppCompatTextView appCompatTextView = null;
            if(textViews.size() == 1) {
                appCompatTextView = (AppCompatTextView) textViews.get(0);
            } else {
                for(View v : textViews) {
                    if(v.getParent() instanceof Toolbar) {
                        appCompatTextView = (AppCompatTextView) v;
                        break;
                    }
                }
            }

            if(appCompatTextView != null) {
                ViewGroup.LayoutParams params = appCompatTextView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                appCompatTextView.setLayoutParams(params);
                appCompatTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        }
    }

    private List<Contact> dummyData(){
        List<Contact> contacts = new ArrayList<>();
        Contact c1 = new Contact("1", "John Doe", "jufiheuf232", 321, "EUR");
        Contact c2 = new Contact("2", "Max Kellerman", "fkjns3iejnfe", 432, "EUR");
        Contact c3 = new Contact("3", "Stephen Smith", "lasfenirkne34", 764, "EUR");
        Contact c4 = new Contact("4", "Molly Qerim", "9083urdfnsk", 100, "EUR");
        Contact c5 = new Contact("5", "Aaron Rodgers", "lksdfmdklf54", 893, "EUR");
        Contact c6 = new Contact("6", "Mathew Perry", "23jkbn23nreb", 765, "EUR");
        contacts.add(c1);
        contacts.add(c2);
        contacts.add(c3);
        contacts.add(c4);
        contacts.add(c5);
        contacts.add(c6);
        return contacts;
    }
}

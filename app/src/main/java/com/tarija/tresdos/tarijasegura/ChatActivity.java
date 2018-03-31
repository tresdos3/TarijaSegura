package com.tarija.tresdos.tarijasegura;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarija.tresdos.tarijasegura.fragments.AlertFragment;
import com.tarija.tresdos.tarijasegura.other.chatmessage;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class ChatActivity extends AppCompatActivity {

    RelativeLayout activity_main;

    private EmojiconEditText emojiconEditText;
    private ImageView emojiButton,submitButton;
    private EmojIconActions emojIconActions;

    private ListView ListaView;

    private FirebaseUser user;
    private DatabaseReference rootRef, HijosRef;

    private SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String Huid = "HuidKey";
    private String Key;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ListaView = (ListView) findViewById(R.id.list_of_message);
        activity_main = (RelativeLayout) findViewById(R.id.activity_main);
        emojiButton = (ImageView) findViewById(R.id.emoji_button);
        submitButton = (ImageView) findViewById(R.id.submit_button);
        emojiconEditText = (EmojiconEditText) findViewById(R.id.emojicon_edit_text);
        emojIconActions = new EmojIconActions(getApplicationContext(),activity_main,emojiButton,emojiconEditText);
        emojIconActions.ShowEmojicon();
        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        user = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        HijosRef = rootRef.child(user.getUid());
        Key = sharedPreferences.getString(Huid, "");
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(emojiconEditText.getText().toString())){
                    HijosRef.child("hijos").child(Key).child("chat").push().setValue(
                            new chatmessage(
                                    "Hijo: ",
                                    emojiconEditText.getText().toString()
                            )
                    );
                    emojiconEditText.setText("");
                    emojiconEditText.requestFocus();
                }
                else {
                    MDToast.makeText(ChatActivity.this, "Ingrese un mensaje :)", MDToast.TYPE_ERROR).show();
                }
            }
        });

        displayChatMessage();
    }
    private void displayChatMessage() {
        final List<chatmessage> allItems = new ArrayList<chatmessage>();
        HijosRef.child("hijos").child(Key).child("chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allItems.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    chatmessage data = postSnapshot.getValue(chatmessage.class);
                    allItems.add(new chatmessage(
                                    data.getMessageText(),
                                    data.getMessageUser(),
                                    data.getMessageTime()
                            )
                    );
                }
                List<chatmessage> rowListItem = allItems;
                Adapter adapter = new Adapter(getApplicationContext(), rowListItem);
                ListaView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    class Adapter extends BaseAdapter {

        private Context context;
        private LayoutInflater layoutInflater;
        private List<chatmessage> messages;

        private Adapter(Context context, List<chatmessage> Mylist) {
            this.context = context;
            this.messages = Mylist;
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            Valor holders;
            if (view == null){
                holders = new Valor();
                view = layoutInflater.inflate(R.layout.item_for_list, parent, false);
                holders.message = (TextView) view.findViewById(R.id.message_text);
                holders.user = (TextView) view.findViewById(R.id.message_user);
                holders.date = (TextView) view.findViewById(R.id.message_time);
                view.setTag(holders);
            }
            else {
                holders = (Valor) view.getTag();
            }
            holders.user.setText(messages.get(position).getMessageUser());
            java.util.Date time=new java.util.Date(messages.get(position).getMessageTime()*1000);
            SimpleDateFormat pre = new SimpleDateFormat("HH:mm:ss");
            holders.date.setText(pre.format(time).toString());
            holders.message.setText(messages.get(position).getMessageText());
            return view;
        }
    }
    static  class Valor{
        TextView message, user, date;
    }
}

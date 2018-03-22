package com.tarija.tresdos.tarijasegura.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarija.tresdos.tarijasegura.R;
import com.tarija.tresdos.tarijasegura.other.chatmessage;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlertFragment extends Fragment {

    RelativeLayout activity_main;

    private EmojiconEditText emojiconEditText;
    private ImageView emojiButton,submitButton;
    private EmojIconActions emojIconActions;

    private ListView ListaView;

    private FirebaseUser user;
    private DatabaseReference rootRef, HijosRef;

    private SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String Child = "child_id";
    private String Key;
    private View view;

    public AlertFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_alert, container, false);

        ListaView = (ListView) view.findViewById(R.id.list_of_message);
        activity_main = (RelativeLayout)view.findViewById(R.id.activity_main);
        emojiButton = (ImageView)view.findViewById(R.id.emoji_button);
        submitButton = (ImageView)view.findViewById(R.id.submit_button);
        emojiconEditText = (EmojiconEditText)view.findViewById(R.id.emojicon_edit_text);
        emojIconActions = new EmojIconActions(getContext(),activity_main,emojiButton,emojiconEditText);
        emojIconActions.ShowEmojicon();

        sharedPreferences = this.getActivity().getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        user = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        HijosRef = rootRef.child(user.getUid());
        Key = sharedPreferences.getString(Child, "");
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(emojiconEditText.getText().toString())){
                    HijosRef.child("hijos").child(Key).child("chat").push().setValue(
                            new chatmessage(
                                    emojiconEditText.getText().toString()
                            )
                    );
                    emojiconEditText.setText("");
                    emojiconEditText.requestFocus();
                }
                else {
                    MDToast.makeText(getContext(), "Ingrese un mensaje :)", MDToast.TYPE_ERROR).show();
                }
            }
        });

        displayChatMessage();

        return view;
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
                Adapter adapter = new Adapter(getActivity(), rowListItem);
                ListaView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    class Adapter extends BaseAdapter{

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
                holders = new AlertFragment.Valor();
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
            holders.date.setText(Long.toString(messages.get(position).getMessageTime()));
            holders.message.setText(messages.get(position).getMessageText());
            return view;
        }
    }
    static  class Valor{
        TextView message, user, date;
    }
}

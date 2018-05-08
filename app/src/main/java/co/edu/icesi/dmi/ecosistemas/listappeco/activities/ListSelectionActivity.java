package co.edu.icesi.dmi.ecosistemas.listappeco.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.signin.SignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import co.edu.icesi.dmi.ecosistemas.listappeco.utils.AppConstants;
import co.edu.icesi.dmi.ecosistemas.listappeco.R;
import co.edu.icesi.dmi.ecosistemas.listappeco.models.ListModel;

public class ListSelectionActivity extends AppCompatActivity {

    private FirebaseListAdapter<ListModel> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_selection);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("lists")
                .limitToLast(100);

        ListView lists = (ListView) findViewById(R.id.lv_lists);

        FirebaseListOptions<ListModel> options = new FirebaseListOptions.Builder<ListModel>()
                .setQuery(query, ListModel.class)
                .setLayout(R.layout.list_as_item_layout)
                .build();

        adapter = new FirebaseListAdapter<ListModel>(options) {
            @Override
            protected void populateView(View v, ListModel model, int position) {
                ((TextView)v.findViewById(R.id.tv_list_name)).setText(model.getName());
                ((TextView)v.findViewById(R.id.tv_list_count)).setText( ""+model.getCount());
            }
        };

        lists.setAdapter(adapter);

        lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListSelectionActivity.this, ListDetailActivity.class);
                i.putExtra("list", adapter.getRef(position).getKey() );
                i.putExtra("name", adapter.getItem(position).getName() );
                startActivity(i);
            }
        });

        lists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CharSequence options[] = new CharSequence[] {"editar", "eliminar"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ListSelectionActivity.this);
                builder.setTitle("¿Qué quieres hacer?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0: // editar
                                Intent i = new Intent(getApplicationContext(), EditListActivity.class);
                                i.putExtra("list", adapter.getRef(position).getKey());
                                i.putExtra("name", adapter.getItem(position).getName());
                                startActivityForResult(i, AppConstants.EDIT_LIST_REQUEST);
                                break;
                            case 1: // eliminar
                                String keyItemToRemove = adapter.getRef(position).getKey();
                                FirebaseDatabase.getInstance().getReference("items").child(keyItemToRemove).removeValue();
                                adapter.getRef(position).removeValue();
                                break;
                        }
                    }
                });
                builder.show();
                return true;
            }
        });

        ((Button)findViewById(R.id.btn_create_list)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListSelectionActivity.this, CreateListActivity.class);
                startActivityForResult(i, AppConstants.CREATE_LIST_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == AppConstants.CREATE_LIST_RESPONSE){
            String name = data.getStringExtra("name");
            FirebaseDatabase db =  FirebaseDatabase.getInstance();
            DatabaseReference refNewList = db.getReference("lists").push();
            ListModel newList = new ListModel(name, refNewList.getKey());
            refNewList.setValue(newList);
        } else if(resultCode == AppConstants.EDIT_LIST_RESPONSE_OK){
            String name = data.getStringExtra("name");
            String key = data.getStringExtra("listKey");
            FirebaseDatabase db =  FirebaseDatabase.getInstance();
            DatabaseReference refListEditedName = db.getReference("lists").child(key).child("name");
            refListEditedName.setValue(name);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextual, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cerrar_sesion:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(ListSelectionActivity.this, SignInActivity.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

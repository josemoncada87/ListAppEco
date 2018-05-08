package co.edu.icesi.dmi.ecosistemas.listappeco.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import co.edu.icesi.dmi.ecosistemas.listappeco.utils.AppConstants;
import co.edu.icesi.dmi.ecosistemas.listappeco.R;
import co.edu.icesi.dmi.ecosistemas.listappeco.models.ItemModel;

public class ListDetailActivity extends AppCompatActivity {

    private FirebaseListAdapter<ItemModel> adapter;
    private String listKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail);

        listKey = getIntent().getStringExtra("list");

        TextView t = (TextView) findViewById(R.id.tv_title_list_detail);
        t.setText(getIntent().getStringExtra("name"));

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("items")
                .child(listKey)
                .limitToLast(100);

        ListView items = (ListView) findViewById(R.id.lv_items);

        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListDetailActivity.this, ItemDetailActivity.class);
                i.putExtra("item", adapter.getRef(position).getKey());
                i.putExtra("name", adapter.getItem(position).getName());
                i.putExtra("description", adapter.getItem(position).getDescription());
                startActivity(i);
            }
        });

        items.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                CharSequence options[] = new CharSequence[] {"editar", "eliminar"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ListDetailActivity.this);
                builder.setTitle("¿Qué quieres hacer?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent i = new Intent(getApplicationContext(), EditItemActivity.class);
                                i.putExtra("item", adapter.getRef(position).getKey());
                                i.putExtra("name", adapter.getItem(position).getName());
                                i.putExtra("description", adapter.getItem(position).getDescription());
                                i.putExtra("listKey", listKey);
                                i.putExtra("estado", adapter.getItem(position).isCheck());
                                startActivityForResult(i, AppConstants.EDIT_ITEM_REQUEST);
                                break;
                            case 1:
                                FirebaseDatabase.getInstance().getReference("lists").child(listKey).child("count").setValue(adapter.getCount());
                                adapter.getRef(position).removeValue();
                                break;
                        }
                    }
                });
                builder.show();
                return true;
            }
        });

        FirebaseListOptions<ItemModel> options = new FirebaseListOptions.Builder<ItemModel>()
                .setQuery(query, ItemModel.class)
                .setLayout(R.layout.items_as_item_layout)
                .build();
        adapter = new FirebaseListAdapter<ItemModel>(options) {
            @Override
            protected void populateView(View v, final ItemModel model, final int position) {
                ((TextView) v.findViewById(R.id.tv_item_name)).setText(model.getName());
                ((CheckBox) v.findViewById(R.id.check_item)).setChecked(model.isCheck());
                ((CheckBox) v.findViewById(R.id.check_item)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        adapter.getRef(position).child("check").setValue(isChecked);
                    }
                });
                FirebaseDatabase.getInstance().getReference("lists").child(listKey).child("count").setValue(adapter.getCount());
            }
        };
        items.setAdapter(adapter);

        ((Button) findViewById(R.id.btn_create_item)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListDetailActivity.this, CreateItemActivity.class);
                startActivityForResult(i, AppConstants.CREATE_ITEM_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AppConstants.CREATE_ITEM_RESPONSE) {
            String name = data.getStringExtra("name");
            String description = data.getStringExtra("description");
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference refNewItem = db.getReference("items").child(listKey).push();
            ItemModel newItem = new ItemModel(name, description, refNewItem.getKey(),listKey);
            refNewItem.setValue(newItem);
        }else if (resultCode == AppConstants.EDIT_ITEM_RESPONSE_OK) {
            String name = data.getStringExtra("name");
            String description = data.getStringExtra("description");
            String item = data.getStringExtra("itemkey");
            boolean value = data.getBooleanExtra("estado", false);
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference refEditedItemName = db.getReference("items").child(listKey).child(item).child("name");
            DatabaseReference refEditedItemDescription = db.getReference("items").child(listKey).child(item).child("description");
            DatabaseReference refEditedItemValue = db.getReference("items").child(listKey).child(item).child("check");
            refEditedItemName.setValue(name);
            refEditedItemDescription.setValue(description);
            refEditedItemValue.setValue(value);
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
}

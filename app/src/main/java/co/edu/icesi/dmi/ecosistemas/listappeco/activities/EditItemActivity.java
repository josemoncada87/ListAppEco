package co.edu.icesi.dmi.ecosistemas.listappeco.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import co.edu.icesi.dmi.ecosistemas.listappeco.R;
import co.edu.icesi.dmi.ecosistemas.listappeco.utils.AppConstants;

public class EditItemActivity extends AppCompatActivity {

    private EditText edtItemName;
    private EditText edtItemDescription;

    private String itemkey;
    private String listkey;

    private CheckBox checkBoxItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        String nombre = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        itemkey = getIntent().getStringExtra("item");
        listkey = getIntent().getStringExtra("listKey");

        boolean estado = getIntent().getBooleanExtra("estado", false);
        checkBoxItem = (CheckBox)findViewById(R.id.check_edit_item_checkbox);
        checkBoxItem.setChecked(estado);

       /* checkBoxItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });*/

        edtItemName = (EditText) findViewById(R.id.edt_edit_item_name);
        edtItemDescription = (EditText) findViewById(R.id.edt_edit_item_description);
        edtItemName.setText(nombre);
        edtItemDescription.setText(description);

        ((Button)findViewById(R.id.btn_edit_item_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(AppConstants.EDIT_ITEM_RESPONSE_CANCEL);
                finish();
            }
        });



        ((Button)findViewById(R.id.btn_edit_item_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nuevoNombre = edtItemName.getText().toString();
                String nuevoDescription = edtItemDescription.getText().toString();
                boolean value = checkBoxItem.isChecked();

                Intent respuesta = new Intent();
                respuesta.putExtra("name", nuevoNombre);
                respuesta.putExtra("description", nuevoDescription);
                respuesta.putExtra("estado", value);
                respuesta.putExtra("itemkey", itemkey);

                setResult(AppConstants.EDIT_ITEM_RESPONSE_OK, respuesta);
                finish();
            }
        });
    }


}

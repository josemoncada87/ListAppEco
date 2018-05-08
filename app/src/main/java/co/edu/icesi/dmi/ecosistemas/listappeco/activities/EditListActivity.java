package co.edu.icesi.dmi.ecosistemas.listappeco.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import co.edu.icesi.dmi.ecosistemas.listappeco.R;
import co.edu.icesi.dmi.ecosistemas.listappeco.utils.AppConstants;

public class EditListActivity extends AppCompatActivity {

    private String listKey;
    private EditText edtListName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);
        listKey = getIntent().getStringExtra("list");
        String nombreActual = getIntent().getStringExtra("name");
        edtListName = (EditText) findViewById(R.id.edt_edit_list_name);
        edtListName.setText(nombreActual);

        ((Button)findViewById(R.id.btn_edit_list_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(AppConstants.EDIT_LIST_RESPONSE_CANCEL);
                finish();
            }
        });

        ((Button)findViewById(R.id.btn_edit_list_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nuevoNombre = edtListName.getText().toString();
                Intent respuesta = new Intent();
                respuesta.putExtra("name", nuevoNombre);
                respuesta.putExtra("listKey", listKey);
                setResult(AppConstants.EDIT_LIST_RESPONSE_OK, respuesta);
                finish();
            }
        });

    }
}

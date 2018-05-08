package co.edu.icesi.dmi.ecosistemas.listappeco.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import co.edu.icesi.dmi.ecosistemas.listappeco.utils.AppConstants;
import co.edu.icesi.dmi.ecosistemas.listappeco.R;

public class CreateItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        ((Button)findViewById(R.id.btn_create_item)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtName = (EditText) findViewById(R.id.edt_create_item_name);
                EditText edtNDescr = (EditText) findViewById(R.id.edt_create_item_description);

                Intent response = new Intent();
                response.putExtra("name", edtName.getText().toString());
                response.putExtra("description", edtNDescr.getText().toString());

                setResult(AppConstants.CREATE_ITEM_RESPONSE, response);
                finish();
            }
        });
    }
}

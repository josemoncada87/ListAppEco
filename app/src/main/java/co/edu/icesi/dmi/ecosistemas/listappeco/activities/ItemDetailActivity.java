package co.edu.icesi.dmi.ecosistemas.listappeco.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import co.edu.icesi.dmi.ecosistemas.listappeco.R;

public class ItemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        String listKey = getIntent().getStringExtra("item");

        TextView name = (TextView) findViewById(R.id.tv_item_detail_name);
        TextView descripcion = (TextView) findViewById(R.id.tv_item_detail_description);

        name.setText(getIntent().getStringExtra("name"));
        descripcion.setText(getIntent().getStringExtra("description"));

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
                Intent i = new Intent(ItemDetailActivity.this, SignInActivity.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

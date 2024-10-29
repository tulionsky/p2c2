package gt.edu.umg.gallery_and_memories;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import gt.edu.umg.gallery_and_memories.database.DatabaseHelper;
import gt.edu.umg.gallery_and_memories.galeria.EasterEggActivity;
import gt.edu.umg.gallery_and_memories.galeria.GaleriaActivity;
import gt.edu.umg.gallery_and_memories.models.PhotoItem;


public class MainActivity extends AppCompatActivity {

    private Button btnfoto;
    private TextView tvSaludo, lastPhotoDescription, lastPhotoDate;
    private ImageView lastPhotoImage;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupClickListeners();
        dbHelper = new DatabaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeViews() {
        tvSaludo = findViewById(R.id.tvSaludo);
        btnfoto = findViewById(R.id.btnfoto);
        lastPhotoImage = findViewById(R.id.lastPhotoImage);
        lastPhotoDescription = findViewById(R.id.lastPhotoDescription);
        lastPhotoDate = findViewById(R.id.lastPhotoDate);
    }

    private void setupClickListeners() {

        btnfoto.setOnClickListener(v -> {
            Intent intent = new Intent(this, GaleriaActivity.class);
            startActivity(intent);
        });
    }

    public void openEasterEgg(View view) {
        Intent intent = new Intent(this, EasterEggActivity.class);
        startActivity(intent);
    }




    public void showCredits(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Créditos");
        builder.setMessage("Aplicación desarrollada por:\n- Melki Bladimir Ortiz Martinez\n- Tulio Rene Alejandro Quintana Amezquita\n- Luke Skywalker");
        builder.setPositiveButton("OK", null);
        builder.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadLastPhoto();
    }

    private void loadLastPhoto() {
        List<PhotoItem> photos = dbHelper.getAllPhotos();
        if (!photos.isEmpty()) {
            PhotoItem lastPhoto = photos.get(0); // La primera foto es la más reciente

            // Cargar la imagen
            try {
                lastPhotoImage.setImageURI(Uri.parse(lastPhoto.getUri()));
            } catch (Exception e) {
                lastPhotoImage.setImageResource(R.drawable.kirby);
            }

            // Mostrar la descripción
            lastPhotoDescription.setText(lastPhoto.getDescription());

            // Mostrar la fecha
            lastPhotoDate.setText("Tomada el " + lastPhoto.getDate());

            // Hacer visible el CardView
            findViewById(R.id.lastPhotoCard).setVisibility(View.VISIBLE);
        } else {
            // Si no hay fotos, ocultar el CardView
            findViewById(R.id.lastPhotoCard).setVisibility(View.GONE);
        }
    }
}

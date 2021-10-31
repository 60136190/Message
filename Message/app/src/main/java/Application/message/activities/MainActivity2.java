package Application.message.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

import Application.message.R;
import Application.message.adapters.RecentConversationsAdapter;
import Application.message.databinding.ActivityMainBinding;
import Application.message.models.ChatMessage;
import Application.message.utilities.Constans;
import Application.message.utilities.PreferenceManager;

public class MainActivity2 extends AppCompatActivity {
private Button chuyen;
    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;
    private List<ChatMessage> conversations;
    private RecentConversationsAdapter conversationsAdapter;
    private FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        preferenceManager  = new PreferenceManager(getApplicationContext());
        setContentView(R.layout.activity_main2);
        chuyen = findViewById(R.id.btn_chuyen);
        chuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentchuyen = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intentchuyen);
            }
        });
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }


    private void setListeners(){
        binding.setOnClickListener(v -> signOut());
        binding.fabNewChat.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(),UsersActivity.class)));
    }
    private void signOut(){
        showToast("Signing out...");
        FirebaseFirestore database  = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constans.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constans.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constans.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->showToast("Unable to sign out "));
    }
}
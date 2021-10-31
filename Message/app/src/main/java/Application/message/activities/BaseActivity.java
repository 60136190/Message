package Application.message.activities;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import Application.message.utilities.Constans;
import Application.message.utilities.PreferenceManager;

public class BaseActivity extends AppCompatActivity {
    private DocumentReference documentReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        documentReference = database.collection(Constans.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constans.KEY_USER_ID));
    }



    @Override
    protected void onPause() {
        super.onPause();
        documentReference.update(Constans.KEY_AVAILABILITY, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        documentReference.update(Constans.KEY_AVAILABILITY,1);
    }
}


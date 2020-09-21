package views;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.englishapp.R;

public class LoadingDialog  extends Dialog {

    public LoadingDialog(@NonNull Context context) {
        super(context);

        setContentView(R.layout.dialog_loading_view);
        setCancelable(false);
    }
}

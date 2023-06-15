package at.jamo.pp_snowheightapp.ui.SendSettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import at.jamo.pp_snowheightapp.databinding.FragmentSentEpromSettingsBinding;
import at.jamo.pp_snowheightapp.ui.slideshow.ReadEpromSettingsViewModel;

public class SendEpromSettingsFragment extends Fragment {

    private FragmentSentEpromSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReadEpromSettingsViewModel slideshowViewModel =
                new ViewModelProvider(this).get(ReadEpromSettingsViewModel.class);

        binding = FragmentSentEpromSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSendEprom;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

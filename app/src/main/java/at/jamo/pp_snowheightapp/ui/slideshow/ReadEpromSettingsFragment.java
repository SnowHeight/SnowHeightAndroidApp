package at.jamo.pp_snowheightapp.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import at.jamo.pp_snowheightapp.databinding.FragmentReadEpromSettingsBinding;

public class ReadEpromSettingsFragment extends Fragment {

    private FragmentReadEpromSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReadEpromSettingsViewModel slideshowViewModel =
                new ViewModelProvider(this).get(ReadEpromSettingsViewModel.class);

        binding = FragmentReadEpromSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package at.jamo.pp_snowheightapp.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import at.jamo.pp_snowheightapp.databinding.FragmentDatadiagramBinding;

public class DataDiagramFragment extends Fragment {

    private FragmentDatadiagramBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DataDiagramViewModel galleryViewModel =
                new ViewModelProvider(this).get(DataDiagramViewModel.class);

        binding = FragmentDatadiagramBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
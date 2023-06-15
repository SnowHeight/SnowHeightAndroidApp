package at.jamo.pp_snowheightapp.ui.home;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import at.jamo.pp_snowheightapp.R;
import at.jamo.pp_snowheightapp.databinding.FragmentLoadSdDataBinding;

public class ReadSdDataFragment extends Fragment {

    private static final int REQUEST_PERMISSION_BLUETOOTH = 3;
    private FragmentLoadSdDataBinding binding;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 2;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice targetDevice;
    private UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Standard UUID für serielle Kommunikation über Bluetooth
    private Button btnConnect;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load_sd_data, container, false);
        Button button = view.findViewById(R.id.btnConnect);

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.BLUETOOTH) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.BLUETOOTH_ADMIN)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.BLUETOOTH_CONNECT)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            // Bluetooth-Berechtigungen fehlen, Anfrage an den Benutzer stellen
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
                                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.BLUETOOTH_CONNECT},
                    REQUEST_PERMISSION_BLUETOOTH);
        } else {
            // Bluetooth-Adapter initialisieren
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                // Bluetooth wird auf dem Gerät nicht unterstützt
                Toast.makeText(requireActivity(), "Bluetooth wird nicht unterstützt", Toast.LENGTH_SHORT).show();
                return view;
            }
        }


        btnConnect = view.findViewById(R.id.btnConnect);

        // Button-Click-Listener für "Bluetooth verbinden"
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter == null) {
                    // Bluetooth wird auf dem Gerät nicht unterstützt
                    Toast.makeText(getActivity(), "Bluetooth wird nicht unterstützt", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!bluetoothAdapter.isEnabled()) {
                    // Bluetooth aktivieren
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    selectBluetoothDevice();
                    if (targetDevice != null) {
                        transferData();
                    } else {
                        // Kein Bluetooth-Gerät ausgewählt
                        Toast.makeText(getActivity(), "Kein Bluetooth-Gerät ausgewählt", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }

    private void selectBluetoothDevice() {
        Context ReadSdDataFragment = requireContext();
        if (ActivityCompat.checkSelfPermission(ReadSdDataFragment, Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        targetDevice = null;

        for (BluetoothDevice device : pairedDevices) {
            if (device.getName().equals("HMSoft") || device.getName().equals("SnowHeight-Stubai")) {
                targetDevice = device;
                break;
            }
        }

        if (targetDevice == null) {
            // Das Zielerät wurde nicht gefunden
        }
    }

    private void transferData() {
        // Pfad zur SD-Karte
        File sdCard = Environment.getExternalStorageDirectory();

        // Datei auf der SD-Karte
        File file = new File(sdCard, "Dateiname.txt");

        try {
            // Bluetooth-Socket erstellen und verbinden
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                // Berechtigung nicht erteilt, hier kannst du den Benutzer auffordern, die Berechtigung zu gewähren
                return;
            }
            BluetoothSocket socket = targetDevice.createRfcommSocketToServiceRecord(MY_UUID);
            socket.connect();

            // OutputStream zum Schreiben von Daten über Bluetooth
            OutputStream outputStream = socket.getOutputStream();

            // FileInputStream zum Lesen der Datei
            FileInputStream fileInputStream = new FileInputStream(file);

            // Buffer für Datenübertragung
            byte[] buffer = new byte[1024];
            int bytesRead;

            // Daten von der Datei lesen und über Bluetooth übertragen
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Stream schließen
            fileInputStream.close();
            outputStream.close();

            // Übertragung erfolgreich
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "Datenübertragung erfolgreich", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();

            // Fehler bei der Datenübertragung
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "Fehler bei der Datenübertragung", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
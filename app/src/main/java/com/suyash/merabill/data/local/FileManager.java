package com.suyash.merabill.data.local;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.suyash.merabill.domain.model.Payment;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String FILENAME = "LastPayment.txt";
    private final Context context;
    private final Gson gson;

    public FileManager(Context context) {
        this.context = context;
        this.gson = new Gson();
    }
    public void savePayments(List<Payment> payments) {
        try (FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE)) {
            String json = gson.toJson(payments);
            fos.write(json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<Payment> loadPayments() {
        try (FileInputStream fis = context.openFileInput(FILENAME)) {
            InputStreamReader isr = new InputStreamReader(fis);
            Type type = new TypeToken<List<Payment>>(){}.getType();
            return gson.fromJson(isr, type);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}

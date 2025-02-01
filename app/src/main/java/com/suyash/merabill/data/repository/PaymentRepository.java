package com.suyash.merabill.data.repository;

import android.content.Context;

import com.suyash.merabill.data.local.FileManager;
import com.suyash.merabill.domain.model.Payment;

import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {
    private final FileManager fileManager;
    private List<Payment> cachedPayments;

    public PaymentRepository(Context context) {
        this.fileManager = new FileManager(context);
        this.cachedPayments = new ArrayList<>();
    }

    public List<Payment> loadPayments() {
        if (cachedPayments.isEmpty()) {
            cachedPayments = fileManager.loadPayments();
        }
        return new ArrayList<>(cachedPayments);
    }

    public boolean savePayment(List<Payment> payments) {
        try {
            fileManager.savePayments(payments);
            cachedPayments = payments;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void clearCache() {
        cachedPayments.clear();
    }
}

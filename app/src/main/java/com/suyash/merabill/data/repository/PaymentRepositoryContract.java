package com.suyash.merabill.data.repository;

import com.suyash.merabill.domain.model.Payment;

import java.util.List;

public interface PaymentRepositoryContract {
    List<Payment> loadPayments();
    boolean savePayment(List<Payment> payments);
    void clearCache();
}
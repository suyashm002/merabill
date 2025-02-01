package com.suyash.merabill.domain.usecase;

import com.suyash.merabill.domain.model.Payment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentManager {
    private List<Payment> payments;
    private static final int MAX_PAYMENTS = 3;

    public PaymentManager() {
        payments = new ArrayList<>();
    }

    public boolean addPayment(Payment payment) {
        if (payments.size() >= MAX_PAYMENTS || hasPaymentType(payment.getType())) {
            return false;
        }
        payments.add(payment);
        return true;
    }
    public boolean removePayment(String type) {
        return payments.removeIf(p -> p.getType().equals(type));
    }

    public double getTotalAmount() {
        return payments.stream().mapToDouble(Payment::getAmount).sum();
    }

    public List<String> getAvailablePaymentTypes() {
        List<String> allTypes = Arrays.asList("Cash", "Bank Transfer", "Credit Card");
        return allTypes.stream()
                .filter(type -> !hasPaymentType(type))
                .collect(Collectors.toList());
    }
    private boolean hasPaymentType(String type) {
        return payments.stream().anyMatch(p -> p.getType().equals(type));
    }

    public List<Payment> getPayments() {
        return new ArrayList<>(payments);
    }
}

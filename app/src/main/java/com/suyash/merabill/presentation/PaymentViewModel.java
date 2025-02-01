package com.suyash.merabill.presentation;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.suyash.merabill.data.repository.PaymentRepository;
import com.suyash.merabill.domain.model.Payment;
import com.suyash.merabill.domain.usecase.PaymentManager;

import java.util.List;

public class PaymentViewModel extends ViewModel {
    private final PaymentRepository repository;
    private final PaymentManager paymentManager;
    private final MutableLiveData<List<Payment>> paymentsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Double> totalAmountLiveData = new MutableLiveData<>();

    public PaymentViewModel(PaymentRepository repository) {
        this.repository = repository;
        this.paymentManager = new PaymentManager();
        loadSavedPayments();
    }
    public void loadSavedPayments() {
        List<Payment> savedPayments = repository.loadPayments();
        for (Payment payment : savedPayments) {
            paymentManager.addPayment(payment);
        }
        updateLiveData();
    }

    public boolean addPayment(Payment payment) {
        boolean success = paymentManager.addPayment(payment);
        if (success) {
            updateLiveData();
        }
        return success;
    }
    public void removePayment(String type) {
        if (paymentManager.removePayment(type)) {
            updateLiveData();
        }
    }
    public void savePayments() {
        repository.savePayment(paymentManager.getPayments());
    }

    private void updateLiveData() {
        paymentsLiveData.setValue(paymentManager.getPayments());
        totalAmountLiveData.setValue(paymentManager.getTotalAmount());
    }
    public LiveData<List<Payment>> getPayments() {
        return paymentsLiveData;
    }

    public LiveData<Double> getTotalAmount() {
        return totalAmountLiveData;
    }

    public List<String> getAvailablePaymentTypes() {
        return paymentManager.getAvailablePaymentTypes();
    }
}
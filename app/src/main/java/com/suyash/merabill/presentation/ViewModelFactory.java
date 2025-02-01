package com.suyash.merabill.presentation;



import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.suyash.merabill.data.repository.PaymentRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final PaymentRepository repository;

    public ViewModelFactory(PaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PaymentViewModel.class)) {
            return (T) new PaymentViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
package com.suyash.merabill.presentation.ui;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;


import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;
import com.suyash.merabill.R;

import com.suyash.merabill.data.repository.PaymentRepository;
import com.suyash.merabill.domain.model.Payment;

import com.suyash.merabill.presentation.PaymentViewModel;
import com.suyash.merabill.presentation.ViewModelFactory;

import java.util.List;


public class MainActivity extends AppCompatActivity{
    private PaymentViewModel viewModel;
    private FlexboxLayout paymentsContainer;
    private TextView totalAmountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PaymentRepository repository = new PaymentRepository(this);
        viewModel = new ViewModelProvider((ViewModelStoreOwner) this, new ViewModelFactory(repository))
                .get(PaymentViewModel.class);

        initializeViews();
        observeViewModel();
    }
    private void initializeViews() {
        totalAmountText = findViewById(R.id.totalAmountText);
        paymentsContainer = findViewById(R.id.paymentsContainer);

        findViewById(R.id.addPaymentButton).setOnClickListener(v -> showAddPaymentDialog());
        findViewById(R.id.saveButton).setOnClickListener(v -> viewModel.savePayments());
    }

    private void observeViewModel() {

        viewModel.getPayments().observe((LifecycleOwner) this, this::updatePaymentChips);
        viewModel.getTotalAmount().observe((LifecycleOwner) this, this::updateTotalAmount);
    }

    private void updatePaymentChips(List<Payment> payments) {
        paymentsContainer.removeAllViews();
        for (Payment payment : payments) {
            addPaymentChip(payment);
        }
    }
    private void updateTotalAmount(Double total) {
        totalAmountText.setText(String.format("Total Amount = ₹ %.2f", total));
    }

    private void showAddPaymentDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_payment, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle("Add Payment")
                .create();

        Spinner typeSpinner = dialogView.findViewById(R.id.typeSpinner);
        EditText amountEdit = dialogView.findViewById(R.id.amountEdit);
        View additionalFieldsContainer = dialogView.findViewById(R.id.additionalFieldsContainer);
        EditText providerEdit = dialogView.findViewById(R.id.providerEdit);
        EditText referenceEdit = dialogView.findViewById(R.id.referenceEdit);

        setupSpinner(typeSpinner, additionalFieldsContainer);

        dialogView.findViewById(R.id.okButton).setOnClickListener(v -> {
            if (validateAndAddPayment(typeSpinner, amountEdit, providerEdit, referenceEdit)) {
                dialog.dismiss();

            }
        });

        dialogView.findViewById(R.id.cancelButton).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void setupSpinner(Spinner spinner, View additionalFieldsContainer) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                viewModel.getAvailablePaymentTypes()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = (String) parent.getItemAtPosition(position);
                additionalFieldsContainer.setVisibility(
                        selectedType.equals("Cash") ? View.GONE : View.VISIBLE
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                additionalFieldsContainer.setVisibility(View.GONE);
            }
        });
    }

        private boolean validateAndAddPayment(Spinner typeSpinner, EditText amountEdit,
                                          EditText providerEdit, EditText referenceEdit) {
        String type = typeSpinner.getSelectedItem().toString();
        String amountStr = amountEdit.getText().toString();

        if (amountStr.isEmpty()) {
            amountEdit.setError("Amount is required");
            return false;
        }
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            amountEdit.setError("Invalid amount");
            return false;
        }

        Payment payment;
        if (type.equals("Cash")) {
            payment = new Payment(type, amount);
        } else {
            String provider = providerEdit.getText().toString();
            String reference = referenceEdit.getText().toString();

            if (provider.isEmpty() || reference.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return false;
            }
            payment = new Payment(type, amount, provider, reference);
        }

        return viewModel.addPayment(payment);
    }

    private void addPaymentChip(Payment payment) {
        Chip chip = new Chip(this);
        chip.setText(payment.getDisplayText());
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> {
            viewModel.removePayment(payment.getType());
        });
        paymentsContainer.addView(chip);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
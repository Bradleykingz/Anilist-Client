package com.discern.anillist.common.utils;


import android.app.Application;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.EditText;

public class ValidateCredentials extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static boolean isValidEmail(TextInputLayout textInputLayout) {
        boolean valid = true;
        resetEditText(textInputLayout);
        String email = textInputLayout.getEditText().getText().toString().trim();
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() ||
                email.length()==0) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("Please enter a valid email");
            valid = false;
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
        }
        return valid;
    }

    public static boolean isValidPassword(TextInputLayout passwordWrapper) {
        boolean valid= true;
        resetEditText(passwordWrapper);
        String uPassword = passwordWrapper.getEditText().getText().toString().trim();
        if (uPassword.isEmpty()||uPassword.length()<6){
            passwordWrapper.setErrorEnabled(true);
            passwordWrapper.setError("Needs to be at least 6 characters");
            valid = false;
        }else{
            passwordWrapper.setError(null);
            passwordWrapper.setErrorEnabled(false);
        }
        return valid;
    }

    public static boolean isValidUsername(TextInputLayout textInputLayout){
        boolean valid = true;
        resetEditText(textInputLayout);
        String username = textInputLayout.getEditText().getText().toString().trim();
        if (username.isEmpty()|| username.length()<6){
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("Must be at least 6 characters long");
            valid = false;
        }else if (DefUtils.containsIllegals(username)){
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("Sorry, that username contains illegal characters. Please try again");
            valid  = false;
        }else if (username.contains(" ")){
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("Your username cannot contain space");
            valid = false;
        }
        else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
        }
        return valid;
    }

    public static boolean isValidEmailProvider(TextInputLayout layout){
        String email = layout.getEditText().getText().toString().trim();
        String re = DefUtils.getEmailDomain(email);
        resetEditText(layout);
        if (!re.equals("yahoo.com")||!re.equals("gmail.com")||re.equals("aol.com")){
            layout.setError("Sorry. We currently only support gmail, yahoo and aol.");
            layout.getEditText().requestFocus();
            return false;
        } else {
            layout.setError(null);
            layout.setErrorEnabled(false);
            return true;
        }
    }

    private static void resetEditText(final TextInputLayout textInputLayout){
            EditText editText = textInputLayout.getEditText();

        if (editText != null) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    textInputLayout.setError(null);
                    textInputLayout.setErrorEnabled(false);

                }
            });
        }
    }


    public static boolean isGenerallyValid(EditText editText){
        if (editText.getText().toString().isEmpty()||editText.getText().toString().length()<1){
            return false;
        }else {
            return true;
        }
    }
}

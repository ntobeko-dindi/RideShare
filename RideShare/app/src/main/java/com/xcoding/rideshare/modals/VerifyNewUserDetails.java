package com.xcoding.rideshare.modals;

import android.util.Patterns;

import javax.crypto.SecretKey;

public class VerifyNewUserDetails {
    private String firstName;
    private String lastName;
    private String email;
    private String cell;
    private String pass;
    private String gender;
    private boolean verified;

    public VerifyNewUserDetails() {
    }

    public String validate() {
        //,,,
        if ("".equals(firstName)) {
            return "firstNameError";
        } else if ("".equals(lastName)) {
            return "lastNameError";
        } else if ("".equals(email) || !emailOkay()) {
            return "emailError";
        } else if ("".equals(cell) /*|| !isValidPhone()*/) {
            return "phoneNumberError";
        } else if ("".equals(pass) /*!passwordFormatOkay()*/) {
            return "passwordError";
        } else {
            return "ok";
        }
    }

    public boolean emailOkay() {
        boolean isValid = false;
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = true;
        }
        return isValid;
    }

    public boolean isValidPhone() {
        if (cell.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(cell).matches();
        }
    }

    public boolean passwordFormatOkay() {
        boolean isOkay = false;

        if (pass.length() < 8) {
            return false;
        } else {
            boolean hasDigit = false, hasUpper = false, hasLower = false, hasSpecialChar = false;

            for (int x = 0; x < pass.length(); x++) {
                if (Character.isDigit(pass.charAt(x))) {
                    hasDigit = true;
                } else if (Character.isUpperCase(pass.charAt(x))) {
                    hasUpper = true;
                } else if (Character.isLowerCase(pass.charAt(x))) {
                    hasLower = true;
                } else {
                    hasSpecialChar = true;
                }
            }
            if (hasDigit && hasUpper && hasLower && hasSpecialChar) {
                isOkay = true;
            }
        }
        return isOkay;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setVerified(boolean isVerified){this.verified = isVerified;}

    //getters


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCell() {
        return cell;
    }

    public String getPass() {
        return pass;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail(){return email;}

    public boolean getVerified(){
        return verified;
    }
}

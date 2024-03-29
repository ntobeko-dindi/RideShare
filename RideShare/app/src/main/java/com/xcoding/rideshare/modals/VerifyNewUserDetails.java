package com.xcoding.rideshare.modals;

import android.util.Patterns;

public class VerifyNewUserDetails {
    private String firstName;
    private String lastName;
    private String email;
    private String cell;
    private String pass;
    private String gender;
    private boolean isDriver;
    private String dateOfBirth;

    public VerifyNewUserDetails() {
    }

    public String validate() {
        //,,,
        if ("".equals(firstName) || !this.isCorrectName(this.firstName)) {
            return "firstNameError";
        } else if ("".equals(lastName) || !this.isCorrectName(this.lastName)) {
            return "lastNameError";
        } else if ("".equals(email) || !emailOkay()) {
            return "emailError";
        } else if ("".equals(cell) || !isValidPhone()) {
            return "phoneNumberError";
        } else if ("".equals(pass) || !passwordFormatOkay()) {
            return "passwordError";
        } else {
            return "ok";
        }
    }

    public boolean isCorrectName(String name) {
        boolean isValidName = true;

        int count = 0;
        while (count < name.length()) {
            if (Character.isDigit(name.charAt(count))) {
                isValidName = false;
            }
            count++;
        }
        return isValidName;
    }

    public boolean emailOkay() {
        boolean isValid = false;
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = true;
        }
        return isValid;
    }

    public boolean isValidPhone() {
        if (cell.length() != 9) {
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

    public void setDriver(boolean driver) {
        isDriver = driver;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

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

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public boolean isDriver() {
        return isDriver;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }
}

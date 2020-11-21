package com.xcoding.rideshare.modals;

public class VerifyNewUserDetails {
    private String firstName;
    private String lastName;
    private String email;
    private String cell;
    private String pass;
    private String gender;

    public VerifyNewUserDetails() {
    }

    public String validate() {
        //,,,
        if ("".equals(firstName)) {
            return "firstNameError";
        } else if ("".equals(lastName)) {
            return "lastNameError";
        } else if ("".equals(email)) {
            return "emailError";
        } else if ("".equals(cell) || !cellIncorect()) {
            return "phoneNumberError";
        } else if ("".equals(pass) || !passwordFormatOkay()) {
            return "passwordError";
        } else {
            return "ok";
        }
    }

    public boolean cellIncorect() {
        boolean corectPhone = true;

        if (cell.length() != 10) {
            corectPhone = !corectPhone;
        } else if (!"0".equals(String.valueOf(cell.charAt(0)))) {
            corectPhone = !corectPhone;
        } else {
            for (int x = 0; x < cell.length(); x++) {
                if (!Character.isDigit(cell.charAt(x))) {
                    corectPhone = !corectPhone;
                }
            }
        }
        return corectPhone;
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

    //getters

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
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
}

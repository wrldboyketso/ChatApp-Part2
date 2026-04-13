package chatapp;

public class Login {

    private String registeredFirstName;
    private String registeredLastName;
    private String registeredUsername;
    private String registeredPassword;
    private String registeredCellNumber;

    public boolean checkUserName(String username) {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (char currentCharacter : password.toCharArray()) {
            if (Character.isUpperCase(currentCharacter)) {
                hasCapital = true;
            } else if (Character.isDigit(currentCharacter)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(currentCharacter)) {
                hasSpecial = true;
            }
        }

        return hasCapital && hasNumber && hasSpecial;
    }

    public boolean checkCellPhoneNumber(String cellNumber) {
        return cellNumber != null && cellNumber.matches("^\\+\\d{10,12}$");
    }

    public String getUsernameValidationMessage(String username) {
        if (checkUserName(username)) {
            return "Username successfully captured.";
        }

        return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than 5 characters in length.";
    }

    public String getPasswordValidationMessage(String password) {
        if (checkPasswordComplexity(password)) {
            return "Password successfully captured.";
        }

        return "Password is not correctly formatted, please ensure that the password contains at least 8 characters, a capital letter, a number, and a special character.";
    }

    public String getCellPhoneValidationMessage(String cellNumber) {
        if (checkCellPhoneNumber(cellNumber)) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number incorrectly formatted or does not contain international code.";
    }

    public String registerUser(String firstName, String lastName, String username, String password, String cellNumber) {
        if (!checkUserName(username)) {
            return getUsernameValidationMessage(username);
        }

        if (!checkPasswordComplexity(password)) {
            return getPasswordValidationMessage(password);
        }

        if (!checkCellPhoneNumber(cellNumber)) {
            return getCellPhoneValidationMessage(cellNumber);
        }

        registeredFirstName = firstName;
        registeredLastName = lastName;
        registeredUsername = username;
        registeredPassword = password;
        registeredCellNumber = cellNumber;

        return "User has been registered successfully.";
    }

    public String registerUser(String username, String password, String cellNumber) {
        return registerUser("", "", username, password, cellNumber);
    }

    public boolean loginUser(String username, String password) {
        return username != null
                && password != null
                && username.equals(registeredUsername)
                && password.equals(registeredPassword);
    }

    public String returnLoginStatus(boolean loginSuccess) {
        if (loginSuccess) {
            return "Welcome " + registeredFirstName + " ," + registeredLastName + " it is great to see you again.";
        }

        return "Username or password incorrect, please try again.";
    }

    public String getRegisteredFirstName() {
        return registeredFirstName;
    }

    public String getRegisteredLastName() {
        return registeredLastName;
    }

    public String getRegisteredUsername() {
        return registeredUsername;
    }

    public String getRegisteredCellNumber() {
        return registeredCellNumber;
    }
}

package chatapp;

import java.util.Scanner;

public class Main {

    private static final String JSON_FILE = "storedMessages.json";

    private static String promptValidUsername(Scanner input, Login login) {
        while (true) {
            System.out.print("Enter username: ");
            String username = input.nextLine();
            System.out.println(login.getUsernameValidationMessage(username));

            if (login.checkUserName(username)) {
                return username;
            }
        }
    }

    private static String promptValidPassword(Scanner input, Login login) {
        while (true) {
            System.out.print("Enter password: ");
            String password = input.nextLine();
            System.out.println(login.getPasswordValidationMessage(password));

            if (login.checkPasswordComplexity(password)) {
                return password;
            }
        }
    }

    private static String promptValidCellNumber(Scanner input, Login login) {
        while (true) {
            System.out.print("Enter cellphone number: ");
            String cellNumber = input.nextLine();
            System.out.println(login.getCellPhoneValidationMessage(cellNumber));

            if (login.checkCellPhoneNumber(cellNumber)) {
                return cellNumber;
            }
        }
    }

    private static void registerUser(Scanner input, Login login) {
        System.out.println("\n=== REGISTER ===");
        System.out.print("Enter first name: ");
        String firstName = input.nextLine();

        System.out.print("Enter last name: ");
        String lastName = input.nextLine();

        String username = promptValidUsername(input, login);
        String password = promptValidPassword(input, login);
        String cellNumber = promptValidCellNumber(input, login);

        System.out.println(login.registerUser(firstName, lastName, username, password, cellNumber));
    }

    private static boolean loginUser(Scanner input, Login login) {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Enter username: ");
        String username = input.nextLine();

        System.out.print("Enter password: ");
        String password = input.nextLine();

        boolean loginSuccess = login.loginUser(username, password);
        System.out.println(login.returnLoginStatus(loginSuccess));
        return loginSuccess;
    }

    private static void runQuickChat(Scanner input) {
        System.out.println("\nWelcome to QuickChat.");
        System.out.print("How many messages do you want to enter? ");
        int messageLimit = Integer.parseInt(input.nextLine());

        Message[] sentMessages = new Message[messageLimit];
        int sentCount = 0;
        int messageCounter = 1;
        int choice;

        do {
            System.out.println("\nChoose an option:");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(input.nextLine());

            if (choice == 1) {
                if (messageCounter > messageLimit) {
                    System.out.println("You have reached your message limit.");
                    continue;
                }

                int remainingMessages = messageLimit - messageCounter + 1;

                for (int index = 0; index < remainingMessages; index++) {
                    boolean messageCaptured = false;

                    while (!messageCaptured) {
                        System.out.println("\nMessage " + messageCounter);
                        System.out.print("Enter recipient number: ");
                        String recipient = input.nextLine();

                        System.out.print("Enter your message: ");
                        String messageText = input.nextLine();

                        Message newMessage = new Message(
                                String.format("%010d", messageCounter),
                                recipient,
                                messageText,
                                messageCounter - 1
                        );

                        if (!newMessage.checkMessageID()) {
                            System.out.println("Message ID is incorrectly formatted.");
                            continue;
                        }

                        String messageLengthResult = newMessage.checkMessageLength();
                        System.out.println(messageLengthResult);

                        if (!"Message ready to send.".equals(messageLengthResult)) {
                            continue;
                        }

                        String recipientResult = newMessage.getRecipientValidationMessage();
                        System.out.println(recipientResult);

                        if (!newMessage.checkRecipientCell()) {
                            continue;
                        }

                        System.out.println("\nChoose what to do with the message:");
                        System.out.println("1. Send Message");
                        System.out.println("2. Disregard Message");
                        System.out.println("3. Store Message");
                        System.out.print("Enter your choice: ");
                        int sendChoice = Integer.parseInt(input.nextLine());

                        String sendResult = newMessage.sentMessage(sendChoice);
                        System.out.println(sendResult);
                        System.out.println(newMessage.printMessage());

                        if ("Sent".equals(newMessage.getMessageStatus())) {
                            sentMessages[sentCount] = newMessage;
                            sentCount++;
                        } else if ("Stored".equals(newMessage.getMessageStatus())) {
                            newMessage.storeMessage(JSON_FILE);
                        }

                        messageCounter++;
                        messageCaptured = true;
                    }
                }

                System.out.println("Total messages sent: " + Message.returnTotalMessagess());

            } else if (choice == 2) {
                System.out.println("Coming Soon.");

            } else if (choice == 3) {
                System.out.println("Goodbye.");

            } else {
                System.out.println("Invalid option.");
            }
        } while (choice != 3);
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login login = new Login();
        boolean running = true;

        while (running) {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            String option = input.nextLine();

            if ("1".equals(option)) {
                registerUser(input, login);
            } else if ("2".equals(option)) {
                if (loginUser(input, login)) {
                    runQuickChat(input);
                    running = false;
                }
            } else if ("3".equals(option)) {
                System.out.println("Goodbye.");
                running = false;
            } else {
                System.out.println("Invalid option.");
            }
        }

        input.close();
    }
}

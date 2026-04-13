package chatapp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Message {

    private static int totalMessages = 0;

    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String messageStatus;
    private int messageNumber;

    public Message(String messageID, String recipient, String messageText) {
        this(messageID, recipient, messageText, 0);
    }

    public Message(String messageID, String recipient, String messageText, int messageNumber) {
        this.messageID = messageID;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageNumber = messageNumber;
        this.messageHash = createMessageHash();
        this.messageStatus = "";
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    public String checkMessageLength() {
        if (messageText != null && messageText.length() <= 250) {
            return "Message ready to send.";
        }

        int extraCharacters = messageText == null ? 0 : messageText.length() - 250;
        return "Message exceeds 250 characters by " + extraCharacters + ", please reduce size.";
    }

    public boolean checkRecipientCell() {
        return recipient != null && recipient.matches("^\\+\\d{10,12}$");
    }

    public String getRecipientValidationMessage() {
        if (checkRecipientCell()) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public String createMessageHash() {
        String firstTwoDigits = "";
        if (messageID != null && !messageID.isEmpty()) {
            firstTwoDigits = messageID.substring(0, Math.min(2, messageID.length()));
        }

        String cleanedMessage = messageText == null ? "" : messageText.trim();
        String[] words = cleanedMessage.isEmpty() ? new String[0] : cleanedMessage.split("\\s+");
        String firstWord = words.length == 0 ? "" : words[0];
        String lastWord = words.length == 0 ? "" : words[words.length - 1];

        return (firstTwoDigits + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    public String sentMessage(int choice) {
        if (choice == 1) {
            if (!"Sent".equals(messageStatus)) {
                totalMessages++;
            }
            messageStatus = "Sent";
            return "Message successfully sent.";
        }

        if (choice == 2) {
            messageStatus = "Disregarded";
            return "Press 0 to delete message.";
        }

        if (choice == 3) {
            messageStatus = "Stored";
            return "Message successfully stored.";
        }

        return "Invalid choice.";
    }

    public String printMessage() {
        return "MessageID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText;
    }

    public static String printMessages(Message[] sentMessages, int sentCount) {
        if (sentCount == 0) {
            return "No messages have been sent.";
        }

        StringBuilder result = new StringBuilder();

        for (int index = 0; index < sentCount; index++) {
            if (sentMessages[index] != null) {
                if (result.length() > 0) {
                    result.append("\n--------------------\n");
                }
                result.append(sentMessages[index].printMessage());
            }
        }

        return result.toString();
    }

    public static int returnTotalMessagess() {
        return totalMessages;
    }

    public void storeMessage(String fileName) {
        Path filePath = Path.of(fileName);
        String jsonEntry = "{\n"
                + "  \"messageID\": \"" + escapeJson(messageID) + "\",\n"
                + "  \"recipient\": \"" + escapeJson(recipient) + "\",\n"
                + "  \"messageText\": \"" + escapeJson(messageText) + "\",\n"
                + "  \"messageHash\": \"" + escapeJson(messageHash) + "\",\n"
                + "  \"messageStatus\": \"" + escapeJson(messageStatus) + "\"\n"
                + "}";

        try {
            if (!Files.exists(filePath)) {
                Files.writeString(filePath, "[\n" + jsonEntry + "\n]", StandardCharsets.UTF_8);
                return;
            }

            String currentContent = Files.readString(filePath, StandardCharsets.UTF_8).trim();

            if (currentContent.isEmpty() || "[]".equals(currentContent)) {
                Files.writeString(filePath, "[\n" + jsonEntry + "\n]", StandardCharsets.UTF_8);
                return;
            }

            String contentWithoutClosingBracket = currentContent.substring(0, currentContent.lastIndexOf(']')).trim();
            String newContent = contentWithoutClosingBracket + ",\n" + jsonEntry + "\n]";
            Files.writeString(filePath, newContent, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to store the message in JSON.", exception);
        }
    }

    public static void resetTotalMessages() {
        totalMessages = 0;
    }

    private static String escapeJson(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public String getMessageID() {
        return messageID;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getMessageStatus() {
        return messageStatus;
    }
}

package chatapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTest {

    @BeforeEach
    public void resetTotals() {
        Message.resetTotalMessages();
    }

    @Test
    public void messageLengthSuccessReturnsCorrectOutput() {
        Message message = new Message("0000000001", "+27718693002",
                "Hi Mike, can you join us for dinner tonight", 0);

        assertEquals("Message ready to send.", message.checkMessageLength());
    }

    @Test
    public void messageLengthFailureReturnsCorrectOutput() {
        Message message = new Message("0000000002", "+27718693002", "A".repeat(260), 1);

        assertEquals("Message exceeds 250 characters by 10, please reduce size.", message.checkMessageLength());
    }

    @Test
    public void recipientNumberSuccessReturnsCorrectOutput() {
        Message message = new Message("0000000001", "+27718693002",
                "Hi Mike, can you join us for dinner tonight", 0);

        assertEquals("Cell phone number successfully captured.", message.getRecipientValidationMessage());
    }

    @Test
    public void recipientNumberFailureReturnsCorrectOutput() {
        Message message = new Message("0000000002", "08575975889",
                "Hi Keegan, did you receive the payment?", 1);

        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                message.getRecipientValidationMessage()
        );
    }

    @Test
    public void messageHashIsCreatedCorrectly() {
        Message message = new Message("0000000001", "+27718693002",
                "Hi Mike, can you join us for dinner tonight", 0);

        assertEquals("00:0:HITONIGHT", message.createMessageHash());
    }

    @Test
    public void sendMessageReturnsCorrectOutput() {
        Message message = new Message("0000000001", "+27718693002",
                "Hi Mike, can you join us for dinner tonight", 0);

        assertEquals("Message successfully sent.", message.sentMessage(1));
    }

    @Test
    public void disregardMessageReturnsCorrectOutput() {
        Message message = new Message("0000000002", "+27718693002",
                "Hi Keegan, did you receive the payment?", 1);

        assertEquals("Press 0 to delete message.", message.sentMessage(2));
    }

    @Test
    public void storeMessageReturnsCorrectOutput() {
        Message message = new Message("0000000003", "+27718693002",
                "This message will be stored.", 2);

        assertEquals("Message successfully stored.", message.sentMessage(3));
    }

    @Test
    public void messageIdIsCheckedCorrectly() {
        Message message = new Message("0000000001", "+27718693002",
                "Hi Mike, can you join us for dinner tonight", 0);

        assertEquals(true, message.checkMessageID());
    }

    @Test
    public void totalMessagesSentIsReturnedCorrectly() {
        Message firstMessage = new Message("0000000001", "+27718693002",
                "Hi Mike, can you join us for dinner tonight", 0);
        Message secondMessage = new Message("0000000002", "+27718693002",
                "Hi Keegan, did you receive the payment?", 1);

        firstMessage.sentMessage(1);
        secondMessage.sentMessage(2);

        assertEquals(1, Message.returnTotalMessagess());
    }
}

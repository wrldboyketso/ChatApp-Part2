package chatapp;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageStoreTest {

    @Test
    public void storeMessageWritesNdjson(@TempDir Path tempDir) throws Exception {
        Path tempFile = tempDir.resolve("storedMessages.ndjson");

        Message message = new Message("0000000005", "+27718693002", "Hello NDJSON!", 4);
        message.sentMessage(3); // mark as Stored
        message.storeMessage(tempFile.toString());

        java.util.List<String> lines = Files.readAllLines(tempFile, StandardCharsets.UTF_8);
        assertEquals(1, lines.size());

        JsonObject obj = JsonParser.parseString(lines.get(0)).getAsJsonObject();
        assertEquals(message.getMessageID(), obj.get("messageID").getAsString());
        assertEquals(message.getRecipient(), obj.get("recipient").getAsString());
        assertEquals(message.getMessageText(), obj.get("messageText").getAsString());
        assertEquals(message.getMessageStatus(), obj.get("messageStatus").getAsString());
    }
}

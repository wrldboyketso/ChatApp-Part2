# Copilot / AI Assistant Instructions for ChatApp-Part2

Short summary
- This is a small console-based Java (Java 17) chat demo using Maven. Core logic lives in `src/main/java/chatapp` and tests in `src/test/java/chatapp`.

Key files
- `src/main/java/chatapp/Main.java` — CLI flow, user input, uses `JSON_FILE = "storedMessages.json"` and calls `Message.storeMessage(...)` when messages are stored.
- `src/main/java/chatapp/Message.java` — message model, validation, `createMessageHash()` and `storeMessage()` (now uses Gson to append NDJSON lines to `storedMessages.json`).
- `src/main/java/chatapp/Login.java` — simple in-memory registration/login and input validation rules.
- `storedMessages.json` — sample NDJSON/JSON example at repo root visible in the IDE.
- `pom.xml` — Maven config (Java 17), JUnit Jupiter for tests, Gson dependency added.

Architecture & data flows (what matters to code changes)
- Single-process console app. No networking or services; all persistence is a single file at the repo root (`storedMessages.json`).
- Message lifecycle: created in `Main`, validated via `Message` helpers, status set via `sentMessage()`. If status == "Stored" `Message.storeMessage(JSON_FILE)` is invoked.
- Storage design: NDJSON (newline-delimited JSON). Each stored message is one JSON object per line. Use Gson for serialization (already added to `pom.xml`).

Project-specific conventions and gotchas
- Validation patterns are conservative and enforced in code (copy them exactly when writing tests/fuzzers):
  - cellphone: `^\\+\\d{10,12}$` (international format including leading `+`).
  - username: contains `_` and length <= 5.
  - password: >=8 chars, at least one uppercase, one digit, one special char (see `Login.checkPasswordComplexity`).
- `Message.createMessageHash()` builds a custom hash string (first 2 digits of ID + `:` + `messageNumber` + `:` + firstWord + lastWord, uppercased). Tests rely on that exact output.
- Tests do not assert on `storedMessages.json` content — you can change storage formatting (within reason) without breaking existing tests. Still, prefer NDJSON for append-friendly behavior.
- Static counters: `Message.returnTotalMessagess()` and `Message.resetTotalMessages()` control total sent count — when writing tests, reset totals in `@BeforeEach`.

Build / test / debug
- Run unit tests: `mvn test` (JUnit Jupiter configured). Java 17 required.
- Run application in NetBeans: open as Maven project and run `Main` (NetBeans will use project JDK). The sample `storedMessages.json` at repo root will be visible in the IDE.

Recommended guidance for edits
- Prefer using Gson (already added) for JSON serialization. For append-heavy flows, keep NDJSON (one JSON object per line). Example append pattern used in `Message.storeMessage`:

```
Gson gson = new GsonBuilder().disableHtmlEscaping().create();
String json = gson.toJson(message);
Files.writeString(path, json + System.lineSeparator(), UTF_8, StandardOpenOption.CREATE | StandardOpenOption.APPEND);
```

- If you change JSON format (array vs NDJSON), update `storedMessages.json` sample and document the reasoning in README.
- Avoid adding heavy frameworks; this project is intentionally minimal. If you add libraries, update `pom.xml` and keep the `maven.compiler.release` property at 17.

Where to start when fixing bugs
- Reproduce via `mvn test` (unit tests are small and fast).
- For interactive debugging, run `Main` in the IDE and exercise the paths that call `Message.storeMessage` and `sentMessage`.

Notes for future AI agents
- Preserve small-footprint design choices unless the change reduces complexity (e.g., NDJSON is preferable to fragile manual array edits).
- When editing `Message.storeMessage`, ensure proper escaping/encoding by using Gson; prefer `StandardOpenOption.APPEND` for safe appends.
- Mention any changed file paths in PR descriptions (`Message.java`, `pom.xml`, `storedMessages.json`) so reviewers know about storage changes.

If anything here is unclear or you'd like more granular examples (unit tests that assert stored JSON, or switching to JSON-array writes), say which you prefer and I'll implement it.

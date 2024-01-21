package outbox;

public interface OutboxScheduler {
    void processOutboxMessage();
}

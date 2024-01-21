package saga;

public interface SagaStep <T>{
    void process(T data); //with transaction
    void rollback(T data); //compensating transaction in case a failure occurs
}

package saga;

import domain.event.DomainEvents;

public interface SagaStep <T, S extends DomainEvents, U extends DomainEvents>{
    S process(T data); //with transaction
    U rollback(T data); //compensating transaction in case a failure occurs
}

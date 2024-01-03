package domain.event;

public final class EmptyEvent implements DomainEvents<Void>{
    public static final EmptyEvent INSTANCE = new EmptyEvent();

    private EmptyEvent(){

    }

    @Override
    public void fire() {

    }
}

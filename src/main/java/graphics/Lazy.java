package graphics;

public class Lazy<T> {
    private T cached = null;
    private boolean produced = false;
    private final Producer<T> producer;

    public Lazy(Producer<T> producer){
        this.producer = producer;
    }
    public T get(){
        if (produced)
            return cached;
        produced = true;
        cached = producer.invoke();
        return cached;
    }
}

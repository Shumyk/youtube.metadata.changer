package rocks.shumyk.youtube.metadata.changer.jms.receiver;

public interface JmsDataReceiver<T> {
	void receiveData(final T data);
}

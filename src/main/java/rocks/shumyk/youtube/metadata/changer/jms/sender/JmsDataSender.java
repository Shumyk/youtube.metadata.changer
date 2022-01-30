package rocks.shumyk.youtube.metadata.changer.jms.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import rocks.shumyk.youtube.metadata.changer.data.YoutubeVideoMetadata;

@Component
@RequiredArgsConstructor
public class JmsDataSender {

	public static final String QUEUE_A = "queueA";
	public static final String QUEUE_B = "queueB";

	private final JmsTemplate jmsTemplate;

	public void send(final String destination, final YoutubeVideoMetadata metadata) {
		jmsTemplate.convertAndSend(destination, metadata);
	}
}

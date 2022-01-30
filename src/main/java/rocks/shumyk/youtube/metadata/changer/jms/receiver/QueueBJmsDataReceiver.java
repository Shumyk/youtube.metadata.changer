package rocks.shumyk.youtube.metadata.changer.jms.receiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import rocks.shumyk.youtube.metadata.changer.data.YoutubeVideoMetadata;
import rocks.shumyk.youtube.metadata.changer.jms.sender.JmsDataSender;

import static rocks.shumyk.youtube.metadata.changer.YoutubeTransmittingMetadataApplication.JSM_FACTORY_NAME;

/**
 * Receiver from Queue B (modified metadata queue) only purpose to verify modified data received by queue;
 */
@Slf4j
@Component
public class QueueBJmsDataReceiver implements JmsDataReceiver<YoutubeVideoMetadata> {

	@JmsListener(destination = JmsDataSender.QUEUE_B, containerFactory = JSM_FACTORY_NAME)
	public void receiveData(final YoutubeVideoMetadata metadata) {
		log.info("Received from Queue B metadata: {}", metadata);
	}
}

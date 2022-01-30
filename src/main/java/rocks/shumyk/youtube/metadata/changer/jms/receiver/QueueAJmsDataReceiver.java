package rocks.shumyk.youtube.metadata.changer.jms.receiver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import rocks.shumyk.youtube.metadata.changer.YoutubeTransmittingMetadataApplication;
import rocks.shumyk.youtube.metadata.changer.data.YoutubeVideoMetadata;
import rocks.shumyk.youtube.metadata.changer.jms.sender.JmsDataSender;

import static rocks.shumyk.youtube.metadata.changer.YoutubeTransmittingMetadataApplication.JSM_FACTORY_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueAJmsDataReceiver implements JmsDataReceiver<YoutubeVideoMetadata>  {

	private final JmsDataSender jmsDataSender;

	@JmsListener(destination = JmsDataSender.QUEUE_A, containerFactory = JSM_FACTORY_NAME)
	public void receiveData(final YoutubeVideoMetadata metadata) {
		log.info("Received from Queue A metadata: {}", metadata);

		final YoutubeVideoMetadata modifiedMetadata = modifyVideoTitle(metadata);

		jmsDataSender.send(JmsDataSender.QUEUE_B, modifiedMetadata);
		log.info("Sent modified message to Queue B: {}", modifiedMetadata);
	}

	public static YoutubeVideoMetadata modifyVideoTitle(final YoutubeVideoMetadata metadata) {
		return metadata.toBuilder()
			.title(metadata.getTitle().replaceAll("(?i)telecom", "telco"))
			.build();
	}
}

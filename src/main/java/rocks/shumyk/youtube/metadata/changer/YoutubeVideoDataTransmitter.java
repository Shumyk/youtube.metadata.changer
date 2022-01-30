package rocks.shumyk.youtube.metadata.changer;

import com.google.api.services.youtube.model.SearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import rocks.shumyk.youtube.metadata.changer.data.YoutubeVideoMetadata;
import rocks.shumyk.youtube.metadata.changer.jms.sender.JmsDataSender;
import rocks.shumyk.youtube.metadata.changer.youtube.proxy.YoutubeProxy;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class YoutubeVideoDataTransmitter {

	private static final String SEARCH_KEYWORD = "telecom";

	private final YoutubeProxy youtubeProxy;
	private final JmsDataSender jmsDataSender;

	@PostConstruct
	public void postConstruct() {
		transferYoutubeDataToJms();
	}

	public void transferYoutubeDataToJms() {
		youtubeProxy
			.safeSearch(SEARCH_KEYWORD)
			.forEach(this::convertAndSendToQueue);
	}

	private void convertAndSendToQueue(final SearchResult result) {
		log.info("fetched from Youtube API video - ID: {}, kind: {}, title: {}", result.getId().getVideoId(), result.getId().getKind(), result.getSnippet().getTitle());
		final YoutubeVideoMetadata metadata = YoutubeVideoMetadata.ofYoutubeSearchResult(result);
		jmsDataSender.send(JmsDataSender.QUEUE_A, metadata);
	}
}

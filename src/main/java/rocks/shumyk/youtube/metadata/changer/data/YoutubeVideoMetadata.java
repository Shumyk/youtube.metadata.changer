package rocks.shumyk.youtube.metadata.changer.data;

import com.google.api.services.youtube.model.SearchResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class YoutubeVideoMetadata {

	private static final String YOUTUBE_URL_TEMPLATE = "https://www.youtube.com/watch?v=%s";

	private String id;
	private String url;
	private String kind;
	private String channelId;
	private String channelTitle;
	private String title;
	private String description;

	public static YoutubeVideoMetadata ofYoutubeSearchResult(final SearchResult searchResult) {
		return new YoutubeVideoMetadata(
			searchResult.getId().getVideoId(),
			youtubeVideoUrlFromId(searchResult.getId().getVideoId()),
			searchResult.getId().getKind(),
			searchResult.getSnippet().getChannelId(),
			searchResult.getSnippet().getChannelTitle(),
			searchResult.getSnippet().getTitle(),
			searchResult.getSnippet().getDescription()
		);
	}

	public static String youtubeVideoUrlFromId(final String id) {
		return String.format(YOUTUBE_URL_TEMPLATE, id);
	}
}

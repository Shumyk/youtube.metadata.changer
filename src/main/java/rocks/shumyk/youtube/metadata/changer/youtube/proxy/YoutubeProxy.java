package rocks.shumyk.youtube.metadata.changer.youtube.proxy;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Slf4j
@Component
public class YoutubeProxy {

	private static final String APPLICATION_NAME = "Youtube API search by title";

	private final YouTube youtube;

	public YoutubeProxy() throws GeneralSecurityException, IOException {
		this.youtube = getService();
	}

	/**
	 * Build and set an API client service.
	 *
	 * @return an API client service
	 * @throws GeneralSecurityException, IOException
	 */
	private YouTube getService() throws GeneralSecurityException, IOException {
		final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		return new YouTube.Builder(httpTransport, GsonFactory.getDefaultInstance(), httpRequest -> {})
			.setApplicationName(APPLICATION_NAME)
			.build();
	}

	/**
	 * Calls function to create API service object.
	 * Define and execute API request.
	 */
	public List<SearchResult> search(final String keyword) throws IOException {
		// Define and execute the API request
		YouTube.Search.List request = youtube.search().list(asList("id", "snippet"));
		SearchListResponse response = request.setMaxResults(25L)
			.setKey("") // private key
			.setType(singletonList("video"))
			.setQ(keyword)
			.execute();
		return response.getItems();
	}

	public List<SearchResult> safeSearch(final String keyword) {
		try {
			return search(keyword);
		} catch (Exception e) {
			log.info("Unexpected exception occurred: {}", e.getMessage(), e);
			return emptyList();
		}
	}
}

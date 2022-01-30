package rocks.shumyk.youtube.metadata.changer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import rocks.shumyk.youtube.metadata.changer.data.YoutubeVideoMetadata;

import javax.jms.ConnectionFactory;

@SpringBootApplication
@EnableJms
public class YoutubeTransmittingMetadataApplication {

	public static final String JSM_FACTORY_NAME = "jmsFactory";

	public static void main(String[] args) {
		SpringApplication.run(YoutubeTransmittingMetadataApplication.class, args);
	}

	@Bean(name = JSM_FACTORY_NAME)
	public JmsListenerContainerFactory<DefaultMessageListenerContainer> jmsFactory(final ConnectionFactory connectionFactory,
																				   final DefaultJmsListenerContainerFactoryConfigurer configurer) {
		final DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		// this provides all boot's default to this factory, including the message converter
		configurer.configure(factory, connectionFactory);
		// you could still override some of boot's default if necessary
		return factory;
	}

	@Bean // serialize message content to xml using jaxb
	public MessageConverter jaxbJmsMessageConverter(final Jaxb2Marshaller marshaller) {
		final MarshallingMessageConverter converter = new MarshallingMessageConverter();
		converter.setMarshaller(marshaller);
		converter.setUnmarshaller(marshaller);
		return converter;
	}

	@Bean
	public Jaxb2Marshaller marshaller() {
		final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(YoutubeVideoMetadata.class);
		return marshaller;
	}
}

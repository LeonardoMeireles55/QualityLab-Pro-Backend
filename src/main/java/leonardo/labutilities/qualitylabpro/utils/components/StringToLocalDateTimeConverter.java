package leonardo.labutilities.qualitylabpro.utils.components;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

@Component
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

	private static final DateTimeFormatter FORMATTER =
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Override
	public LocalDateTime convert(@NonNull String source) {
		return LocalDateTime.parse(source, FORMATTER);
	}
}

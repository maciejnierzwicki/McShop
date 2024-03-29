package pl.maciejnierzwicki.mcshop.utils;

import java.util.List;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import org.springframework.stereotype.Component;


@Component
@Converter
public class MultilineStringConverter implements AttributeConverter<List<String>, String> {

	@Override
	public String convertToDatabaseColumn(List<String> attribute) {
		return StringUtils.toString(attribute);
	}

	@Override
	public List<String> convertToEntityAttribute(String dbData) {
		return StringUtils.toList(dbData);
	}

}

package bet.lucky.game.converter;

import com.google.gson.Gson;
import bet.lucky.game.services.game_core.DataResults;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DataResultConverter implements AttributeConverter<DataResults, String> {
    @Override
    public String convertToDatabaseColumn(DataResults attribute) {
        return new Gson().toJson(attribute);
    }

    @Override
    public DataResults convertToEntityAttribute(String dbData) {
        if (dbData.isBlank())
            return null;
        return new Gson().fromJson(dbData, DataResults.class);
    }
}

package com.stellantis.securitization.entity;

import com.stellantis.securitization.enums.LimitOperator;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class OperatorConverter implements AttributeConverter<LimitOperator, String> {

    @Override
    public String convertToDatabaseColumn(LimitOperator operator) {
        if (operator == null) return null;
        return operator.getSymbol();
    }

    @Override
    public LimitOperator convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return LimitOperator.fromSymbol(dbData);
    }

}

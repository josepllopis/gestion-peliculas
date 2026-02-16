package com.gestionPeliculas.gestionPeliculas.mapper;

import com.gestionPeliculas.gestionPeliculas.enums.Pais;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaisConverter implements AttributeConverter<Pais, String> {

    @Override
    public String convertToDatabaseColumn(Pais pais) {
        return pais != null ? pais.getNombre() : null;
    }

    @Override
    public Pais convertToEntityAttribute(String dbData) {
        return dbData != null ? Pais.fromNombre(dbData) : null;
    }
}


package com.wiss.dragonball.backend.mapper;

import com.wiss.dragonball.backend.dto.CharacterDTO;
import com.wiss.dragonball.backend.entity.Character;

import java.util.List;

/**
 * Mapper-Klasse zur Umwandlung zwischen {@link Character} Entitäten und {@link CharacterDTO} Objekten.
 * <p>
 * Diese Klasse stellt statische Methoden bereit, um:
 * <ul>
 *     <li>einzelne Entitäten in DTOs zu konvertieren und umgekehrt</li>
 *     <li>Listen von Entitäten in DTO-Listen zu transformieren und umgekehrt</li>
 * </ul>
 * </p>
 *
 * @author Thierno
 * @version 1.0
 */
public class CharacterMapper {

    /**
     * Wandelt eine {@link Character} Entität in ein {@link CharacterDTO} um.
     *
     * @param entity Die Datenbank-Entität
     * @return Das entsprechende DTO-Objekt oder {@code null}, wenn {@code entity} null ist
     */
    public static CharacterDTO toDTO(Character entity) {
        if (entity == null) return null;

        CharacterDTO dto = new CharacterDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setKi(entity.getKi());
        dto.setMaxKi(entity.getMaxKi());
        dto.setRace(entity.getRace());
        dto.setGender(entity.getGender());
        dto.setDescription(entity.getDescription());
        dto.setImage(entity.getImage());
        dto.setAffiliation(entity.getAffiliation());
        dto.setPowerLevel(entity.getPowerLevel());
        dto.setUniverse(entity.getUniverse());
        dto.setVillain(entity.isVillain());
        dto.setTransformations(entity.getTransformations());
        dto.setImageUrl(entity.getImageUrl());

        return dto;
    }

    /**
     * Wandelt ein {@link CharacterDTO} in eine {@link Character} Entität um.
     *
     * @param dto Das DTO-Objekt
     * @return Die entsprechende Entität oder {@code null}, wenn {@code dto} null ist
     */
    public static Character toEntity(CharacterDTO dto) {
        if (dto == null) return null;

        return new Character(
                dto.getName(),
                dto.getRace(),
                dto.getKi(),
                dto.getMaxKi(),
                dto.getPowerLevel(),
                dto.getUniverse(),
                dto.isVillain(),
                dto.getTransformations(),
                dto.getImageUrl(),
                dto.getGender(),
                dto.getDescription(),
                dto.getImage(),
                dto.getAffiliation()
        );
    }

    /**
     * Wandelt eine Liste von {@link Character} Entitäten in eine Liste von {@link CharacterDTO}s um.
     *
     * @param entities Liste von Entitäten
     * @return Liste von DTOs
     */
    public static List<CharacterDTO> toDTOList(List<Character> entities) {
        return entities.stream()
                .map(CharacterMapper::toDTO)
                .toList();
    }

    /**
     * Wandelt eine Liste von {@link CharacterDTO}s in eine Liste von {@link Character} Entitäten um.
     *
     * @param dtos Liste von DTOs
     * @return Liste von Entitäten
     */
    public static List<Character> toEntityList(List<CharacterDTO> dtos) {
        return dtos.stream()
                .map(CharacterMapper::toEntity)
                .toList();
    }
}

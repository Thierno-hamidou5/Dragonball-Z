package com.wiss.dragonball.backend.mapper;

import com.wiss.dragonball.backend.dto.CharacterDTO;
import com.wiss.dragonball.backend.entity.Character;

import java.util.List;

/**
 * Mapper-Klasse fuer Character-Entitaeten und CharacterDTOs.
 * Stellt statische Hilfsmethoden bereit, auch fuer Listen, damit Controller und Services schlank bleiben.
 */
public class CharacterMapper {

    /**
     * Wandelt eine {@link Character} Entitaet in ein {@link CharacterDTO} um.
     *
     * @param entity die Datenbank-Entitaet
     * @return das entsprechende DTO oder {@code null}, falls {@code entity} null ist
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
     * Wandelt ein {@link CharacterDTO} in eine {@link Character} Entitaet um.
     *
     * @param dto das DTO-Objekt
     * @return die entsprechende Entitaet oder {@code null}, wenn {@code dto} null ist
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
     * Wandelt eine Liste von {@link Character} Entitaeten in eine Liste von {@link CharacterDTO}s um.
     *
     * @param entities Liste von Entitaeten
     * @return Liste von DTOs
     */
    public static List<CharacterDTO> toDTOList(List<Character> entities) {
        return entities.stream()
                .map(CharacterMapper::toDTO)
                .toList();
    }

    /**
     * Wandelt eine Liste von {@link CharacterDTO}s in eine Liste von {@link Character} Entitaeten um.
     *
     * @param dtos Liste von DTOs
     * @return Liste von Entitaeten
     */
    public static List<Character> toEntityList(List<CharacterDTO> dtos) {
        return dtos.stream()
                .map(CharacterMapper::toEntity)
                .toList();
    }
}

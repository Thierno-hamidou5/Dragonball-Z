package com.wiss.dragonball.backend.service;

import com.wiss.dragonball.backend.dto.CharacterDTO;
import com.wiss.dragonball.backend.entity.Character;
import com.wiss.dragonball.backend.entity.User;
import com.wiss.dragonball.backend.exception.*;
import com.wiss.dragonball.backend.mapper.CharacterMapper;
import com.wiss.dragonball.backend.repository.CharacterRepository;
import com.wiss.dragonball.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service fuer alle Businessregeln rund um Dragonball-Charaktere.
 * Validiert Eingaben, ruft die Repository-Schicht auf und mappt Entitaeten zu DTOs.
 */
@Service
public class CharacterService {

    private final CharacterRepository repository;
    private final UserRepository userRepository;

    public CharacterService(CharacterRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public CharacterDTO getCharacterByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Character name must not be empty");
        }
        Character character = repository.findByName(name)
                .orElseThrow(() -> new CharacterNotFoundException(name));
        return CharacterMapper.toDTO(character);
    }

    public CharacterDTO getCharacterById(Long id) {
        Character character = repository.findById(id)
                .orElseThrow(() -> new CharacterNotFoundException("Character with ID: " + id + " not found"));
        return CharacterMapper.toDTO(character);
    }

    public List<CharacterDTO> getCharactersByRace(String race) {
        validateRace(race);
        List<Character> characters = repository.findByRace(race);
        if (characters.isEmpty()) {
            throw new RaceNotFoundException(race);
        }
        return CharacterMapper.toDTOList(characters);
    }

    public List<CharacterDTO> getCharactersByRaceIgnoreCase(String race) {
        validateRace(race);
        List<Character> characters = repository.findByRaceIgnoreCase(race);
        if (characters.isEmpty()) {
            throw new RaceNotFoundException(race);
        }
        return CharacterMapper.toDTOList(characters);
    }

    public List<CharacterDTO> getCharactersByPowerLevel(long powerLevel) {
        if (powerLevel < 0) {
            throw new IllegalArgumentException("Power level must be positive");
        }
        List<Character> result = repository.findByPowerLevel(powerLevel);
        if (result.isEmpty()) {
            throw new PowerLevelNotFoundException(powerLevel);
        }
        return CharacterMapper.toDTOList(result);
    }

    public List<CharacterDTO> searchByName(String nameFragment) {
        if (nameFragment == null || nameFragment.isBlank()) {
            throw new IllegalArgumentException("Search term must not be empty");
        }
        List<Character> result = repository.findByNameContainingIgnoreCase(nameFragment);
        if (result.isEmpty()) {
            throw new CharacterNotFoundException("No character found for: " + nameFragment);
        }
        return CharacterMapper.toDTOList(result);
    }

    public long countCharactersByRace(String race) {
        validateRace(race);
        return repository.countByRace(race);
    }

    public List<CharacterDTO> getTop5ByPowerGreaterThan(int minPower) {
        if (minPower < 0) {
            throw new IllegalArgumentException("Minimum power level must be positive");
        }
        List<Character> top5 = repository.findTop5ByPowerLevelGreaterThanOrderByPowerLevelDesc(minPower);
        return CharacterMapper.toDTOList(top5);
    }

    public boolean existsByNameAndRace(String name, String race) {
        if (name == null || race == null || name.isBlank() || race.isBlank()) {
            throw new IllegalArgumentException("Name and race must not be empty");
        }
        validateRace(race);
        return repository.existsByNameAndRace(name, race);
    }

    public CharacterDTO createCharacter(CharacterDTO dto) {
        validateCharacterData(dto);
        Character entity = CharacterMapper.toEntity(dto);
        Character saved = repository.save(entity);
        return CharacterMapper.toDTO(saved);
    }

    @Transactional
    public CharacterDTO updateCharacter(Long id, CharacterDTO dto) {
        Character existing = repository.findById(id)
                .orElseThrow(() -> new CharacterNotFoundException("ID " + id));

        validateCharacterData(dto);

        // Standardfelder überschreiben
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setGender(dto.getGender());
        existing.setRace(dto.getRace());
        existing.setPowerLevel(dto.getPowerLevel());
        existing.setAffiliation(dto.getAffiliation());
        existing.setVillain(dto.isVillain());

        // KI-Werte aktualisieren, falls im DTO vorhanden
        if (dto.getKi() != null) {
            existing.setKi(dto.getKi());
        }
        if (dto.getMaxKi() != null) {
            existing.setMaxKi(dto.getMaxKi());
        }

        // Nur aktualisieren, wenn ein neues Bild bzw. eine neue Image‑URL mitkommt
        if (dto.getImageUrl() != null && !dto.getImageUrl().isBlank()) {
            existing.setImageUrl(dto.getImageUrl());
        }
        if (dto.getImage() != null) {
            existing.setImage(dto.getImage());
        }

        Character updated = repository.save(existing);
        return CharacterMapper.toDTO(updated);
    }

    @Transactional
    public void deleteCharacter(Long id) {
        Character character = repository.findById(id)
                .orElseThrow(() -> new CharacterNotFoundException(id));
        List<User> usersWithFavourite = userRepository.findAllByFavourites_Id(id);
        for (User user : usersWithFavourite) {
            user.removeFavourite(character);
        }
        if (!usersWithFavourite.isEmpty()) {
            userRepository.saveAll(usersWithFavourite);
        }
        repository.delete(character);
    }

    public List<CharacterDTO> getAllCharacters() {
        return repository.findAll()
                .stream()
                .map(CharacterMapper::toDTO)
                .toList();
    }

    private void validateCharacterData(CharacterDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new InvalidCharacterDataException("Name is required");
        }
        if (dto.getRace() == null || dto.getRace().isBlank()) {
            throw new InvalidCharacterDataException("Race is required");
        }
        if (dto.getPowerLevel() < 0) {
            throw new InvalidCharacterDataException("Power level must be positive");
        }
    }

    private void validateRace(String race) {
        List<String> validRaces = List.of(
                "saiyan", "namekian", "android", "human", "god", "frieza", "majin"
        );
        if (race == null || race.isBlank()) {
            throw new IllegalArgumentException("Race must not be empty");
        }
        if (!validRaces.contains(race.toLowerCase())) {
            throw new RaceNotFoundException(race);
        }
    }
}

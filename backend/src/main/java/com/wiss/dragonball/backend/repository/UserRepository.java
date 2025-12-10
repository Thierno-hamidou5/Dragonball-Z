package com.wiss.dragonball.backend.repository;

import com.wiss.dragonball.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository fuer {@link User}-Entitaeten. Stellt das von Spring Data
 * generierte CRUD-API bereit und ergaenzt eine Suche nach Benutzernamen
 * (wird z.B. beim Login oder Laden der Favoritenliste verwendet).
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    /** Liefert alle Nutzer, die den angegebenen Charakter als Favoriten gespeichert haben. */
    List<User> findAllByFavourites_Id(Long characterId);
}

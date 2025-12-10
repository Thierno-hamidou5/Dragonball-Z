import React, { useState, useEffect } from "react";
import CharacterCard from "../components/CharacterCard.jsx";
import { fetchCharactersByAffiliation } from "../services/characterService";

/**
 * Frontend-Seite: zeigt alle Z Fighters aus dem Backend,
 * filtert nach Affiliation 'Z Fighter' und ist wie die Backend-Klassen
 * dokumentiert.
 */
const Z_Fighters = () => {
  const [characters, setCharacters] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    let active = true;
    const fetchData = async () => {
      try {
        const data = await fetchCharactersByAffiliation("Z Fighter");
        if (!active) return;
        setCharacters(data || []);
      } catch (err) {
        if (!active) return;
        console.error("Fehler beim Laden der Charaktere:", err);
        setError("Konnte Z Fighters nicht laden");
      } finally {
        if (active) setLoading(false);
      }
    };
    fetchData();
    return () => {
      active = false;
    };
  }, []);

  if (loading) {
    return <p>Loading Z Fighters...</p>;
  }

  return (
      <div>
        <h2>Z Fighters</h2>
        {error && <p style={{ color: "red" }}>{error}</p>}
        {characters.length === 0 ? (
            <p>No Z Fighters available.</p>
        ) : (
            characters.map((character, i) => (
                <CharacterCard
                    key={character.id || character.name + i}
                    id={character.id}
                    name={character.name}
                    image={character.image || character.imageUrl || "/img/Jiren.webp"}
                    race={character.race}
                    gender={character.gender}
                    baseKi={character.ki}
                    totalKi={character.maxKi}
                    affiliation={character.affiliation}
                />
            ))
        )}
      </div>
  );
};

export default Z_Fighters;

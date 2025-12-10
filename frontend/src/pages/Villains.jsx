import React, { useState, useEffect } from "react";
import CharacterCard from "../components/CharacterCard.jsx";
import { fetchVillains } from "../services/characterService.js";

/**
 * Frontend-Seite: listet alle Villains aus dem Backend,
 * filtert per isVillain-Flag und ist wie die Backend-Klassen
 * dokumentiert.
 */
const Villains = () => {
  const [characters, setCharacters] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    let active = true;
    const fetchData = async () => {
      try {
        const data = await fetchVillains();
        if (!active) return;
        setCharacters(data || []);
      } catch (err) {
        if (!active) return;
        console.error("Fehler beim Laden der Charaktere:", err);
        setError("Konnte Villains nicht laden");
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
    return <p>Loading Villains...</p>;
  }

  return (
      <div>
        <h2>Villains</h2>
        {error && <p style={{ color: "red" }}>{error}</p>}
        {characters.length === 0 ? (
            <p>No villains available.</p>
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

export default Villains;

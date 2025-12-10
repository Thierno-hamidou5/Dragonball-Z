import React, { useEffect, useState } from "react";
import CharacterCard from "../components/CharacterCard.jsx";
import { getCharacters } from "../services/characterService.js";

/**
 * Frontend-Seite: laedt alle Charaktere aus dem Backend und zeigt sie
 * als Karten, dokumentiert wie die Klassen im Backend.
 */
const Home = () => {
    const [characters, setCharacters] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        let active = true;
        const load = async () => {
            try {
                const response = await getCharacters();
                if (!active) return;
                setCharacters(response.data || []);
            } catch (err) {
                if (!active) return;
                setError(err.message || "Failed to load characters");
            } finally {
                if (active) setLoading(false);
            }
        };
        load();
        return () => {
            active = false;
        };
    }, []);

    if (loading) return <p>Loading characters...</p>;

    return (
        <div>
            <h2>All Characters</h2>
            {error && <p style={{ color: "red" }}>{error}</p>}
            {characters.length === 0 ? (
                <p>No characters available.</p>
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

export default Home;

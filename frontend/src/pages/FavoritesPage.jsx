import React, { useEffect, useState } from "react";
import {
    getMyFavourites,
    removeFavourite,
} from "../services/favouriteService";
import CharacterCard from "../components/CharacterCard";

/**
 * Frontend-Seite: zeigt die eigenen Favoriten aus dem Backend und
 * erlaubt deren Entfernung, dokumentiert analog zu den Backend-Klassen.
 */
const FavoritesPage = () => {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        let active = true;
        const load = async () => {
            try {
                const response = await getMyFavourites();
                if (!active) return;
                setItems(response.data || []);
            } catch (err) {
                if (!active) return;
                setError(err.message || "Failed to load favourites");
            } finally {
                if (active) setLoading(false);
            }
        };
        load();
        return () => {
            active = false;
        };
    }, []);

    const handleRemove = async (id) => {
        try {
            await removeFavourite(id);
            setItems((prev) => prev.filter((c) => c.id !== id));
        } catch (err) {
            setError(err.message || "Could not remove favourite");
        }
    };

    if (loading) {
        return <p>Loading favourites...</p>;
    }

    return (
        <div>
            <h2>Favorites</h2>
            {error && <p style={{ color: "red" }}>{error}</p>}
            {items.length === 0 ? (
                <p>Your favorite characters will appear here.</p>
            ) : (
                <div>
                    {items.map((character) => (
                        <div key={character.id} style={{ position: "relative" }}>
                            <CharacterCard
                                id={character.id}
                                name={character.name}
                                image={character.image}
                                race={character.race}
                                gender={character.gender}
                                baseKi={character.ki}
                                totalKi={character.maxKi}
                                affiliation={character.affiliation}
                            />
                            <button
                                style={{ marginTop: "0.5rem" }}
                                onClick={() => handleRemove(character.id)}
                            >
                                Remove from favorites
                            </button>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default FavoritesPage;

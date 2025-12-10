import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import {
    addFavourite,
    removeFavourite,
    getMyFavourites,
} from "../services/favouriteService";

/**
 * Frontend-Seite: Detailansicht fuer einen Charakter inklusive
 * Favoriten-Handling und Aktionen fuer Custom-Charaktere,
 * dokumentiert wie die Klassen im Backend.
 */
// gleiche Helferfunktion wie in CharacterCard
const formatKi = (value) => {
    if (value === null || value === undefined || value === "") return "";

    const numeric = Number(String(value).replaceAll(",", "").trim());
    if (Number.isNaN(numeric)) return value;

    // EN-Format: 6,000,000
    return numeric.toLocaleString("en-US");
    // oder z.B.:
    // return numeric.toLocaleString("de-CH"); // 6’000’000
    // return numeric.toLocaleString("de-DE"); // 6.000.000
};

const CharacterDetail = ({
                             customCharacters = [],
                             setCustomCharacters = () => {},
                         }) => {
    const { state } = useLocation();
    const navigate = useNavigate();
    const { isAuthenticated, user } = useAuth();

    const [isFavourite, setIsFavourite] = useState(false);
    const [favError, setFavError] = useState(null);

    // character aus state holen (kein Hook!)
    const character = state?.character || null;

    // nur true, wenn es überhaupt einen Character gibt
    const isCustom =
        !!character &&
        (state?.isCustom ||
            customCharacters.some((c) => c.name === character.name));

    const displayImage = character?.image || "/img/Jiren.webp";

    // Load favourite state for the current user.
    useEffect(() => {
        const load = async () => {
            if (!isAuthenticated || !character?.id) {
                return;
            }
            try {
                const response = await getMyFavourites();
                const ids = (response.data || []).map((c) => c.id);
                setIsFavourite(ids.includes(character.id));
            } catch (err) {
                console.error("Failed to load favourites", err);
            }
        };
        load();
    }, [isAuthenticated, user, character?.id]);

    const handleToggleFavourite = async () => {
        if (!character?.id) {
            setFavError("Kein Character-ID vorhanden");
            return;
        }
        setFavError(null);
        try {
            if (isFavourite) {
                await removeFavourite(character.id);
                setIsFavourite(false);
            } else {
                await addFavourite(character.id);
                setIsFavourite(true);
            }
        } catch (err) {
            const status = err?.response?.status;
            if (status === 401) {
                setFavError("Bitte erst einloggen.");
            } else if (status === 403) {
                setFavError("Keine Berechtigung.");
            } else if (status === 404) {
                setFavError("Charakter nicht gefunden.");
            } else {
                setFavError(err.message || "Aktion fehlgeschlagen");
            }
        }
    };

    const handleDelete = () => {
        if (!character) return;
        setCustomCharacters((prev) =>
            prev.filter((c) => c.name !== character.name)
        );
        navigate(-1);
    };

    const handleEdit = () => {
        if (!character) return;
        navigate("/new-characters", { state: { character, edit: true } });
    };

    // **jetzt** darfst du früh zurückkehren – Hooks wurden schon alle aufgerufen
    if (!character) {
        return <div>Character not found.</div>;
    }

    return (
        <div style={{ padding: "2rem" }}>
            <h2>{character.name}</h2>
            <img
                src={displayImage}
                alt={character.name}
                style={{ maxWidth: "300px" }}
            />
            <p>Race: {character.race}</p>
            <p>Gender: {character.gender}</p>
            <p>Base KI: {formatKi(character.ki)}</p>
            <p>Max KI: {formatKi(character.maxKi)}</p>
            <p>
                Affiliation:{" "}
                {Array.isArray(character.affiliation)
                    ? character.affiliation.join(", ")
                    : character.affiliation}
            </p>

            {isAuthenticated && character.id && (
                <div style={{ marginTop: "1rem" }}>
                    <button onClick={handleToggleFavourite}>
                        {isFavourite
                            ? "Aus Favoriten entfernen"
                            : "Zu Favoriten hinzufuegen"}
                    </button>
                    {favError && <p style={{ color: "red" }}>{favError}</p>}
                </div>
            )}

            {isCustom && (
                <div className="action-buttons">
                    <button className="edit-btn" onClick={handleEdit}>
                        Bearbeiten
                    </button>
                    <button className="delete-btn" onClick={handleDelete}>
                        Loeschen
                    </button>
                </div>
            )}
        </div>
    );
};

export default CharacterDetail;

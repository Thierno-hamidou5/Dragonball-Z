import React, { useEffect, useState } from "react";
import {
    getCharacters,
    createCharacter,
    deleteCharacter,
    updateCharacter,
} from "../services/characterService.js";
import CharacterCard from "../components/CharacterCard";

/**
 * Admin-Seite zur Verwaltung der Charaktere im Backend.
 * Laedt alle Charaktere, erlaubt Anlegen/Bearbeiten/Loeschen und
 * aktualisiert die Liste nach jeder Aenderung.
 */
const ManageCharacters = () => {
    const [characters, setCharacters] = useState([]);
    const [editingId, setEditingId] = useState(null);
    const [formData, setFormData] = useState({
        name: "",
        race: "",
        gender: "",
        ki: "",
        maxKi: "",
        affiliation: "",
        villain: false,
    });
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    // Alle Charaktere laden
    useEffect(() => {
        const load = async () => {
            setLoading(true);
            try {
                const response = await getCharacters();
                setCharacters(response.data || []);
            } catch (err) {
                console.error("Failed to load characters", err);
                setError(err.message || "Failed to load characters");
            } finally {
                setLoading(false);
            }
        };
        load();
    }, []);

    // Formularfelder aktualisieren
    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: type === "checkbox" ? checked : value,
        }));
    };

    // Speichern (anlegen oder aktualisieren)
    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);

        // Grundpayload
        const payload = {
            name: formData.name,
            race: formData.race,
            gender: formData.gender || null,
            ki: formData.ki ? String(formData.ki) : null,
            maxKi: formData.maxKi
                ? String(formData.maxKi)
                : formData.ki
                    ? String(formData.ki)
                    : null,
            affiliation: formData.affiliation || null,
            villain: formData.villain,
        };

        // Nur beim Erstellen Platzhalter‑Bild mitsenden.
        if (!editingId) {
            payload.image = "/img/Jiren.webp";
        }

        try {
            if (editingId) {
                await updateCharacter(editingId, payload);
            } else {
                await createCharacter(payload);
            }
            // Liste neu laden
            const response = await getCharacters();
            setCharacters(response.data || []);
            setFormData({
                name: "",
                race: "",
                gender: "",
                ki: "",
                maxKi: "",
                affiliation: "",
                villain: false,
            });
            setEditingId(null);
        } catch (err) {
            console.error("Failed to save character", err);
            const status = err?.response?.status;
            if (status === 400) {
                setError(err.response?.data?.message || "Invalid character data");
            } else {
                setError(err.message || "Failed to save character");
            }
        }
    };

    // Löschen
    const handleDelete = async (id) => {
        setError(null);
        try {
            await deleteCharacter(id);
            setCharacters((prev) => prev.filter((c) => c.id !== id));
        } catch (err) {
            console.error("Failed to delete character", err);
            const status = err?.response?.status;
            if (status === 500) {
                setError(
                    "Character cannot be deleted because it is still a favourite of one or more users. Please remove it from all favourites first.",
                );
            } else {
                setError(err.message || "Failed to delete character");
            }
        }
    };

    // Formular mit Daten eines vorhandenen Charakters füllen
    const handleEdit = (character) => {
        setEditingId(character.id);
        setFormData({
            name: character.name || "",
            race: character.race || "",
            gender: character.gender || "",
            ki: character.ki || "",
            maxKi: character.maxKi || "",
            affiliation: character.affiliation || "",
            villain: Boolean(
                typeof character.villain === "boolean"
                    ? character.villain
                    : `${character.villain}`.toLowerCase() === "true",
            ),
        });
        window.scrollTo({ top: 0, behavior: "smooth" });
    };

    const handleCancelEdit = () => {
        setEditingId(null);
        setFormData({
            name: "",
            race: "",
            gender: "",
            ki: "",
            maxKi: "",
            affiliation: "",
            villain: false,
        });
    };

    if (loading) {
        return <p>Loading characters...</p>;
    }

    return (
        <div style={{ padding: "1rem" }}>
            <h2>Character Management</h2>
            {error && <p style={{ color: "red" }}>{error}</p>}
            <form onSubmit={handleSubmit} style={{ marginBottom: "2rem" }}>
                <h3>{editingId ? "Edit Character" : "Add New Character"}</h3>
                <div
                    style={{
                        display: "flex",
                        flexDirection: "column",
                        gap: "0.5rem",
                        maxWidth: "400px",
                    }}
                >
                    <input
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        placeholder="Name"
                        required
                    />
                    <input
                        name="race"
                        value={formData.race}
                        onChange={handleChange}
                        placeholder="Race"
                        required
                    />
                    <input
                        name="gender"
                        value={formData.gender}
                        onChange={handleChange}
                        placeholder="Gender"
                    />
                    {/* KI‑Felder als Textfelder, um auch formatierte Werte wie „60,000,000“ zuzulassen */}
                    <input
                        name="ki"
                        type="text"
                        value={formData.ki}
                        onChange={handleChange}
                        placeholder="Base KI"
                    />
                    <input
                        name="maxKi"
                        type="text"
                        value={formData.maxKi}
                        onChange={handleChange}
                        placeholder="Max KI (optional)"
                    />
                    <input
                        name="affiliation"
                        value={formData.affiliation}
                        onChange={handleChange}
                        placeholder="Affiliation"
                    />
                    <div style={{ display: "flex", gap: "0.5rem" }}>
                        <button type="submit">{editingId ? "Update" : "Create"}</button>
                        {editingId && (
                            <button type="button" onClick={handleCancelEdit}>
                                Cancel
                            </button>
                        )}
                    </div>
                </div>
            </form>
            <h3>Existing Characters</h3>
            {characters.length === 0 ? (
                <p>No characters found.</p>
            ) : (
                <div>
                    {characters.map((character) => (
                        <div
                            key={character.id}
                            style={{ marginBottom: "1rem", position: "relative" }}
                        >
                            <CharacterCard
                                id={character.id}
                                name={character.name}
                                image={character.image || character.imageUrl}
                                race={character.race}
                                gender={character.gender}
                                baseKi={character.ki}
                                totalKi={character.maxKi}
                                affiliation={character.affiliation}
                            />
                            <div style={{ display: "flex", gap: "0.5rem", marginTop: "0.5rem" }}>
                                <button type="button" onClick={() => handleEdit(character)}>
                                    Edit
                                </button>
                                <button type="button" onClick={() => handleDelete(character.id)}>
                                    Delete
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default ManageCharacters;

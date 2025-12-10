import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import CharacterCard from "../components/CharacterCard.jsx";

// Komponente zur Erstellung und Bearbeitung von eigenen Charakteren
const NeuCharacters = ({ customCharacters, setCustomCharacters }) => {
  const { state } = useLocation(); // State aus der Navigation (z.B. von "Bearbeiten"-Button)
  const navigate = useNavigate();

  // Prüfe, ob es sich um einen Bearbeitungsfall handelt
  const isEditing = state?.edit || false;
  const characterToEdit = state?.character || null;
  const originalName = characterToEdit?.name; // Wird benötigt, falls Name beim Bearbeiten geändert wird

  // Lokaler Zustand für Formularfelder
  const [name, setName] = useState("");
  const [race, setRace] = useState("");
  const [ki, setKi] = useState("");

  // Wenn in den Bearbeitungsmodus gewechselt wird, Formular mit bestehenden Daten befüllen
  useEffect(() => {
    if (isEditing && characterToEdit) {
      setName(characterToEdit.name);
      setRace(characterToEdit.race);
      setKi(characterToEdit.ki);
    }
  }, [isEditing, characterToEdit]);

  // Input-Handler für Formularfelder
  const handleNameInputChange = (e) => setName(e.target.value);
  const handleRaceInputChange = (e) => setRace(e.target.value);
  const handleKiInputChange = (e) => setKi(e.target.value);

  // Beim Absenden des Formulars
  const handleSubmit = (e) => {
    e.preventDefault();

    // Erstelle Objekt für neuen oder bearbeiteten Charakter
    const newCharacter = {
      name,
      race,
      ki,
      maxKi: ki,
      image: "/img/Jiren.webp",
      gender: "",
      affiliation: "",
    };

    if (isEditing && characterToEdit) {
      // Wenn bearbeitet wird: ersetze alten Charakter basierend auf Originalname
      const updatedList = customCharacters.map((c) =>
        c.name === originalName ? newCharacter : c
      );
      setCustomCharacters(updatedList);
    } else {
      // Falls Name schon existiert, abbrechen
      const exists = customCharacters.some((c) => c.name === name);
      if (exists) {
        alert("Ein Charakter mit diesem Namen existiert bereits.");
        return;
      }

      // Neuen Charakter hinzufügen
      setCustomCharacters((prev) => [...prev, newCharacter]);
    }

    // Formular leeren
    setName("");
    setRace("");
    setKi("");

    // Zurück zur Startseite
    navigate("/");
  };

  // Charakter aus der Liste löschen
  const handleDelete = (index) => {
    const updatedList = customCharacters.filter((_, i) => i !== index);
    setCustomCharacters(updatedList);
  };

  // JSX: Formular + Anzeige aller eigenen Charaktere
  return (
    <div>
      {/* Formular zur Eingabe */}
      <form onSubmit={handleSubmit} style={{ marginBottom: "2rem" }}>
        <h3>
          {isEditing ? "Charakter bearbeiten" : "Neuen Charakter hinzufügen"}
        </h3>
        <input
          type="text"
          name="name"
          placeholder="Name"
          value={name}
          onChange={handleNameInputChange}
          required
        />
        <input
          type="text"
          name="race"
          placeholder="Rasse"
          value={race}
          onChange={handleRaceInputChange}
        />
        <input
          type="number"
          name="ki"
          placeholder="Base KI"
          value={ki}
          onChange={handleKiInputChange}
        />
        <button type="submit">{isEditing ? "Speichern" : "Hinzufügen"}</button>
      </form>

      {/* Alle eigenen Charaktere anzeigen */}
      {customCharacters.map((character, i) => (
        <div
          key={character.name + i}
          style={{ position: "relative", marginBottom: "2rem" }}
        >
          <CharacterCard
            name={character.name}
            image={character.image}
            race={character.race}
            gender={character.gender}
            baseKi={character.ki}
            totalKi={character.maxKi}
            affiliation={character.affiliation}
            isCustom={true}
          />
          <button onClick={() => handleDelete(i)}>🗑️ Löschen</button>
        </div>
      ))}
    </div>
  );
};

export default NeuCharacters;

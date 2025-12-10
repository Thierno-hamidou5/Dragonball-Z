import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import CharacterCard from "../components/CharacterCard.jsx";

const placeholderImage = "/img/Jiren.webp";

/**
 * Frontend-Seite: ermoeglicht das lokale Anlegen, Bearbeiten oder
 * Loeschen eigener Charaktere (ohne Backend), dokumentiert im Stil
 * der Klassenkommentare aus dem Backend.
 */
const NeuCharacters = ({ customCharacters, setCustomCharacters }) => {
  const { state } = useLocation();
  const navigate = useNavigate();

  const isEditing = state?.edit || false;
  const characterToEdit = state?.character || null;
  const originalName = characterToEdit?.name;

  const [name, setName] = useState("");
  const [race, setRace] = useState("");
  const [ki, setKi] = useState("");

  useEffect(() => {
    if (isEditing && characterToEdit) {
      setName(characterToEdit.name);
      setRace(characterToEdit.race);
      setKi(characterToEdit.ki);
    }
  }, [isEditing, characterToEdit]);

  const handleSubmit = (e) => {
    e.preventDefault();

    const newCharacter = {
      name,
      race,
      ki,
      maxKi: ki,
      image: placeholderImage,
      gender: "",
      affiliation: "",
      id: name,
    };

    if (isEditing && characterToEdit) {
      const updatedList = customCharacters.map((c) =>
          c.name === originalName ? newCharacter : c
      );
      setCustomCharacters(updatedList);
    } else {
      const exists = customCharacters.some((c) => c.name === name);
      if (exists) {
        alert("Ein Charakter mit diesem Namen existiert bereits.");
        return;
      }
      setCustomCharacters((prev) => [...prev, newCharacter]);
    }

    setName("");
    setRace("");
    setKi("");
    navigate("/");
  };

  const handleDelete = (index) => {
    const updatedList = customCharacters.filter((_, i) => i !== index);
    setCustomCharacters(updatedList);
  };

  return (
      <div>
        <form onSubmit={handleSubmit} style={{ marginBottom: "2rem" }}>
          <h3>{isEditing ? "Charakter bearbeiten" : "Neuen Charakter hinzufuegen"}</h3>
          <input
              type="text"
              name="name"
              placeholder="Name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
          />
          <input
              type="text"
              name="race"
              placeholder="Rasse"
              value={race}
              onChange={(e) => setRace(e.target.value)}
          />
          <input
              type="number"
              name="ki"
              placeholder="Base KI"
              value={ki}
              onChange={(e) => setKi(e.target.value)}
          />
          <button type="submit">{isEditing ? "Speichern" : "Hinzufuegen"}</button>
        </form>

        {customCharacters.map((character, i) => (
            <div key={character.name + i} style={{ position: "relative", marginBottom: "2rem" }}>
              <CharacterCard
                  id={character.id}
                  name={character.name}
                  image={character.image}
                  race={character.race}
                  gender={character.gender}
                  baseKi={character.ki}
                  totalKi={character.maxKi}
                  affiliation={character.affiliation}
                  isCustom
              />
              <button type="button" onClick={() => handleDelete(i)}>
                Loeschen
              </button>
            </div>
        ))}
      </div>
  );
};

export default NeuCharacters;

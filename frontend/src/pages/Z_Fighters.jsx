import { useState, useEffect } from "react";
import axios from "axios";
import CharacterCard from "../components/CharacterCard.jsx"; // Importiert die Komponente zur Darstellung eines Charakters

const Z_Fighters = () => {
  // State zum Speichern der von der API geladenen Z Fighters
  const [apiCharacters, setApiCharacters] = useState([]);

  // URL zur API, die nur Charaktere mit der Zugehörigkeit "Z Fighter" zurückgibt
  const url =
    "https://dragonball-api.com/api/characters?affiliation=Z%20Fighter";

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Hole die Daten von der API
        const response = await axios.get(url);

        if (response.data) {
          // Speichere die Daten im State
          setApiCharacters(response.data);
        }
      } catch (error) {
        // Fehlerbehandlung bei fehlgeschlagener Anfrage
        console.error("Fehler beim Laden der Charaktere:", error);
      }
    };

    // Führe den API-Call aus
    fetchData();
  }, [url]); // Effekt wird erneut ausgeführt, wenn sich die URL ändert

  // Debug-Ausgabe in der Konsole
  console.log(apiCharacters);

  return (
    <div>
      <h2>Z Fighters</h2>

      {/* Wenn Charaktere vorhanden sind, zeige eine CharacterCard für jeden */}
      {apiCharacters.length > 0 &&
        apiCharacters.map((character, i) => (
          <CharacterCard
            key={character.id || character.name + i} // Eindeutiger Key pro Element
            name={character.name}
            image={character.image}
            race={character.race}
            gender={character.gender}
            baseKi={character.ki}
            totalKi={character.maxKi}
            affiliation={character.affiliation}
          />
        ))}
    </div>
  );
};

export default Z_Fighters; // Exportiert die Komponente zur Verwendung in anderen Teilen der App

import { useState, useEffect } from "react";
import axios from "axios";
import CharacterCard from "../CharacterCard"; // Import der Komponente für die Anzeige einzelner Charaktere

const Villains = () => {
  // State zum Speichern der Villains-Daten aus der API
  const [apiVillains, setApiVillains] = useState([]);

  // URL der API, die nur Charaktere mit der Zugehörigkeit "Villain" zurückgibt
  let url = "https://dragonball-api.com/api/characters?affiliation=Villain";

  // useEffect wird beim Initialisieren der Komponente aufgerufen
  useEffect(() => {
    const fetchData = async () => {
      try {
        // Daten von der API abrufen
        const response = await axios.get(url);

        // Wenn die Antwort Daten enthält, speichere sie im State
        if (response.data) {
          // In diesem Fall werden die Daten nicht weiter gefiltert, sondern direkt übernommen
          setApiVillains(response.data);
        }
      } catch (error) {
        // Fehlerbehandlung bei fehlgeschlagener API-Anfrage
        console.error("Fehler beim Laden der Charaktere:", error);
      }
    };

    // API-Anfrage ausführen
    fetchData();
  }, [url]); // useEffect wird erneut ausgeführt, wenn sich die URL ändert

  // Ausgabe der geladenen Daten in der Konsole zu Debugging-Zwecken
  console.log(apiVillains);

  return (
    <div>
      <h2>Villains</h2>

      {/* Wenn Villains vorhanden sind, rendere eine Liste von CharacterCard-Komponenten */}
      {apiVillains.length > 0 &&
        apiVillains.map((character, i) => (
          <CharacterCard
            key={character.id || character.name + i} // Eindeutiger Schlüssel für jede Karte
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

export default Villains; // Export der Komponente für die Verwendung in anderen Teilen der App

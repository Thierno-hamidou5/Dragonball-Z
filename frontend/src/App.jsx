import { useState, useEffect } from "react";
import { Routes, Route, Link } from "react-router-dom";
import axios from "axios";
import "./App.css";

// Importiere Unterseiten-Komponenten
import CharacterDetail from "./pages/CharacterDetail";
import CharacterCard from "./CharacterCard";
import Villains from "./pages/Villains";
import Z_Fighters from "./pages/Z_Fighters";
import NeuCharacters from "./pages/NeuCharacters";

function App() {
  // Zustand für API-Charaktere
  const [data, setData] = useState([]);
  // Zustand für benutzerdefinierte Charaktere
  const [customCharacters, setCustomCharacters] = useState([]);

  // Hole die API-Daten beim ersten Laden der Seite
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(
          "https://dragonball-api.com/api/characters"
        );

        // Wenn Daten vorhanden sind, speichere sie im Zustand
        if (response.data?.items) {
          setData(response.data.items);
        }
      } catch (error) {
        console.error("Fehler beim Laden der Charaktere:", error);
      }
    };

    fetchData(); // Starte Datenabruf
  }, []);

  return (
    <div className="App">
      {/* Navigation im oberen Bereich */}
      <nav className="App-nav">
        <Link to="/">Home</Link>
        <Link to="/villains">Villains</Link>
        <Link to="/z_fighters">Z Fighters</Link>
        <Link to="/neucharacters">Neu Character</Link>
      </nav>

      {/* Routing der verschiedenen Seiten */}
      <Routes>
        {/* Villains-Seite */}
        <Route path="/villains" element={<Villains />} />

        {/* Z Fighters-Seite */}
        <Route path="/z_fighters" element={<Z_Fighters />} />

        {/* Detailansicht eines Charakters */}
        <Route
          path="/character/:name"
          element={
            <CharacterDetail
              customCharacters={customCharacters}
              setCustomCharacters={setCustomCharacters}
            />
          }
        />

        {/* Seite zum Hinzufügen oder Bearbeiten von Charakteren */}
        <Route
          path="/neucharacters"
          element={
            <NeuCharacters
              customCharacters={customCharacters}
              setCustomCharacters={setCustomCharacters}
            />
          }
        />

        {/* Startseite – zeigt alle Charaktere an */}
        <Route
          path="/"
          element={
            <>
              <h1>Welcome to the Tournament</h1>

              {/* Logo mit Link zur API-Webseite */}
              <div className="logo-container">
                <a
                  href="https://web.dragonball-api.com/"
                  target="_blank"
                  rel="noreferrer"
                >
                  <img
                    src="/img/Logo.jpg"
                    className="logo"
                    alt="Dragon Ball Super"
                  />
                </a>
              </div>

              {/* Zeige alle API-Charaktere und eigene Charaktere */}
              {[...data, ...customCharacters].map((character, i) => (
                <CharacterCard
                  key={character.id || character.name + i} // eindeutiger Key
                  name={character.name}
                  image={character.image}
                  race={character.race}
                  gender={character.gender}
                  baseKi={character.ki}
                  totalKi={character.maxKi}
                  affiliation={character.affiliation}
                  isCustom={i >= data.length} // Kennzeichne eigene Charaktere
                />
              ))}
            </>
          }
        />
      </Routes>
    </div>
  );
}

export default App;

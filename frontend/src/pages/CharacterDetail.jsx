// Importiere benötigte Hooks und Funktionen von React Router
import { useLocation, useNavigate } from "react-router-dom";

// Die Komponente zeigt Details eines Charakters an
const CharacterDetail = ({ customCharacters, setCustomCharacters }) => {
  // Zugriff auf übergebene Daten via Routing (z. B. vom Link)
  const { state } = useLocation();
  const character = state?.character;

  // Prüfen, ob der Charakter benutzerdefiniert ist (eigener Charakter)
  const isCustom =
    state?.isCustom || customCharacters.some((c) => c.name === character?.name);

  // Navigation innerhalb der App vorbereiten
  const navigate = useNavigate();

  // Funktion zum Löschen eines eigenen Charakters
  const handleDelete = () => {
    const updatedList = customCharacters.filter(
      (c) => c.name !== character.name
    );
    setCustomCharacters(updatedList); // State aktualisieren
    window.history.back(); // Zur vorherigen Seite zurückkehren
  };

  // Navigiert zur Bearbeitungsseite und übergibt den Charakter & den Bearbeitungsstatus
  const handleEdit = () => {
    navigate("/neucharacters", { state: { character, edit: true } });
  };

  // JSX: Darstellung der Detailansicht
  return (
    <div style={{ padding: "2rem" }}>
      <h2>{character.name}</h2>
      <img
        src={character.image}
        alt={character.name}
        style={{ maxWidth: "300px" }}
      />
      <p>Rasse: {character.race}</p>
      <p>Geschlecht: {character.gender}</p>
      <p>Base KI: {character.ki}</p>
      <p>Max KI: {character.maxKi}</p>
      <p>
        Zugehörigkeit:{" "}
        {Array.isArray(character.affiliation)
          ? character.affiliation.join(", ")
          : character.affiliation}
      </p>

      {/* Nur bei eigenen Charakteren: Bearbeiten- und Löschen-Button anzeigen */}
      {isCustom && (
        <div className="action-buttons">
          <button className="edit-btn" onClick={handleEdit}>
            ✏️ Bearbeiten
          </button>
          <button className="delete-btn" onClick={handleDelete}>
            🗑️ Löschen
          </button>
        </div>
      )}
    </div>
  );
};

export default CharacterDetail;

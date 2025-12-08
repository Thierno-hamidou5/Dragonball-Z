import { Link } from "react-router-dom";

// Props: Charaktereigenschaften + Info, ob es ein eigener (custom) Charakter ist
const CharacterCard = ({
  name,
  image,
  race,
  gender,
  baseKi,
  totalKi,
  affiliation,
  isCustom, // wird genutzt, um später "Bearbeiten" anzuzeigen
}) => {
  return (
    // Die gesamte Karte ist ein Link zur Detailansicht des Charakters
    <Link
      to={`/character/${encodeURIComponent(name)}`} // URL-freundlicher Name
      className="character-link"
      state={{
        character: {
          name,
          image,
          race,
          gender,
          ki: baseKi, // baseKi wird als "ki" übergeben
          maxKi: totalKi, // totalKi als "maxKi"
          affiliation,
        },
        isCustom: isCustom, // Info: stammt der Charakter von Benutzer?
      }}
    >
      <article className="character-card">
        {/* Bildbereich */}
        <div className="character-image">
          <img src={image} alt={name} />
        </div>

        {/* Textbereich */}
        <div className="character-info">
          <div className="character-header">
            <h2>{name}</h2>
            <p>
              {race} - {gender}
            </p>
          </div>

          {/* Statistiken */}
          <div className="character-stats">
            <div className="stat">
              <p>Base KI:</p>
              <span>{baseKi}</span>
            </div>
            <div className="stat">
              <p>Total KI:</p>
              <span>{totalKi}</span>
            </div>
            <div className="stat">
              <p>Affiliation:</p>
              <span>
                {/* Falls affiliation ein Array ist, mit Komma trennen */}
                {Array.isArray(affiliation)
                  ? affiliation.join(", ")
                  : affiliation}
              </span>
            </div>
          </div>
        </div>
      </article>
    </Link>
  );
};

export default CharacterCard;

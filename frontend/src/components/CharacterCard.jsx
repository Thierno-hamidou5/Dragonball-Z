import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

import {
  addFavourite,
  removeFavourite,
  getMyFavourites,
} from "../services/favouriteService";

// Zahl wie 6000000 -> "6,000,000"
// Wenn kein echter Zahlenwert (z.B. "90 Septillion"), Rueckgabe unveraendert
const formatKi = (value) => {
  if (value === null || value === undefined || value === "") return "";

  const numeric = Number(String(value).replaceAll(",", "").trim());
  if (Number.isNaN(numeric)) return value;

  // EN-Format: 6,000,000
  return numeric.toLocaleString("en-US");
  // Falls du Schweizer / deutsches Format willst:
  // return numeric.toLocaleString("de-CH"); // 6â€™000â€™000
  // return numeric.toLocaleString("de-DE"); // 6.000.000
};

/**
 * Frontend-Komponente: zeigt eine Charakterkarte inkl. Bild,
 * Basiswerten und Favoriten-Schalter, angelehnt an die Klassendoku
 * im Backend.
 *
 * Props:
 * - id: optionale Backend-ID (fuer Fav-API)
 * - name/race/gender/affiliation: Stammdaten fuer Anzeige
 * - baseKi/totalKi: numerisch oder String, werden formatiert
 * - image: Bild-URL, faellt auf Platzhalter zurueck
 * - isCustom: markiert lokale Charaktere ohne Backend-ID
 */
const CharacterCard = ({
                         id,
                         name,
                         image,
                         race,
                         gender,
                         baseKi,
                         totalKi,
                         affiliation,
                         isCustom = false,
                       }) => {
  const displayImage = image || "/Jiren.webp";
  const targetId = id ?? encodeURIComponent(name);
  const affiliationLabel = Array.isArray(affiliation)
      ? affiliation.join(", ")
      : affiliation;

  const { isAuthenticated } = useAuth();
  const [isFavourite, setIsFavourite] = useState(false);
  const [favError, setFavError] = useState(null);
  const numericId = Number.isFinite(Number(id)) ? Number(id) : null;
  const showFav = isAuthenticated && numericId !== null;

  // Favoritenstatus laden, sobald Nutzer angemeldet ist.
  useEffect(() => {
    let active = true;
    const load = async () => {
      if (!showFav) return;
      try {
        const response = await getMyFavourites();
        const ids = (response.data || []).map((c) => c.id);
        if (active) setIsFavourite(ids.includes(numericId));
      } catch (err) {
        console.error("Failed to load favourites", err);
      }
    };
    load();
    return () => {
      active = false;
    };
  }, [showFav, id]);

  const handleToggleFavourite = async (event) => {
    event.preventDefault();
    event.stopPropagation();
    if (!showFav) return;
    setFavError(null);
    try {
      if (isFavourite) {
        await removeFavourite(numericId);
        setIsFavourite(false);
      } else {
        await addFavourite(numericId);
        setIsFavourite(true);
      }
    } catch (err) {
      console.error("Favourite toggle failed", err);
      const status = err?.response?.status;
      if (status === 401) {
        setFavError("Bitte erst einloggen.");
      } else if (status === 403) {
        setFavError("Keine Berechtigung.");
      } else if (status === 404) {
        setFavError("Charakter nicht gefunden.");
      } else {
        setFavError(err.message || "Could not update favourite");
      }
    }
  };

  return (
      <Link
          to={`/characters/${targetId}`}
          className="character-link"
          state={{
            character: {
              id,
              name,
              image: displayImage,
              race,
              gender,
              // hier geben wir weiterhin den â€žrohenâ€œ Wert weiter
              ki: baseKi,
              maxKi: totalKi,
              affiliation,
            },
            isCustom,
          }}
      >
        <article className="character-card" style={{ position: "relative" }}>
          {showFav && (
              <button
                  type="button"
                  onClick={handleToggleFavourite}
                  style={{
                    position: "absolute",
                    top: "0.5rem",
                    right: "0.5rem",
                    background: isFavourite ? "#f87171" : "#f3f4f6",
                    color: "#111",
                    border: "1px solid #e5e7eb",
                    borderRadius: "999px",
                    padding: "0.25rem 0.6rem",
                    cursor: "pointer",
                    fontWeight: 700,
                  }}
                  aria-label="Toggle favourite"
              >
                {isFavourite ? "Fav ♥" : "Fav +"}
              </button>
          )}

          <div className="character-image">
            <img src={displayImage} alt={name} />
          </div>

          <div className="character-info">
            <div className="character-header">
              <h2>{name}</h2>
              <p>
                {race} - {gender}
              </p>
            </div>

            <div className="character-stats">
              <div className="stat">
                <p>Base KI:</p>
                <span>{formatKi(baseKi)}</span>
              </div>
              <div className="stat">
                <p>Total KI:</p>
                <span>{formatKi(totalKi)}</span>
              </div>
              <div className="stat">
                <p>Affiliation:</p>
                <span>{affiliationLabel}</span>
              </div>
            </div>
          </div>

          {favError && (
              <p style={{ color: "red", marginTop: "0.5rem" }}>{favError}</p>
          )}
        </article>
      </Link>
  );
};

export default CharacterCard;

import apiClient from "./apiClient";

const BASE = "/api/users";

/**
 * Holt die Favoriten des aktuell eingeloggten Benutzers.
 * GET /api/users/favourites
 */
export function getMyFavourites() {
    return apiClient.get(`${BASE}/favourites`);
}

/**
 * Fuegt einen Charakter zur Favoritenliste hinzu.
 * POST /api/users/favourites/{characterId}
 */
export function addFavourite(characterId) {
    return apiClient.post(`${BASE}/favourites/${characterId}`);
}

/**
 * Entfernt einen Charakter aus der Favoritenliste.
 * DELETE /api/users/favourites/{characterId}
 */
export function removeFavourite(characterId) {
    return apiClient.delete(`${BASE}/favourites/${characterId}`);
}

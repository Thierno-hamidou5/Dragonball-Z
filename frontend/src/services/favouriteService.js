// Service fuer Favoriten-API: abfragen, hinzufuegen, entfernen.
import apiClient from "./apiClient";

const BASE = "/users";

/**
 * Thin wrapper around the favourites endpoints.
 * addFavourite/removeFavourite expect a numeric characterId.
 */
export function addFavourite(characterId) {
    return apiClient.post(`${BASE}/${characterId}/favourite`);
}

export function removeFavourite(characterId) {
    return apiClient.delete(`${BASE}/${characterId}/favourite`);
}

export function getMyFavourites() {
    return apiClient.get(`${BASE}/me/favourites`);
}

export default {
    addFavourite,
    removeFavourite,
    getMyFavourites,
};

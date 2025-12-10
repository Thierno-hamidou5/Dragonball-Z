import apiClient from "./apiClient";

const BASE = "/users";

export const getMyFavourites = () => apiClient.get(`${BASE}/me/favourites`);
export const fetchFavourites = getMyFavourites; // alias for existing usage
export const addFavourite = (characterId) =>
    apiClient.post(`${BASE}/${characterId}/favourite`);
export const removeFavourite = (characterId) =>
    apiClient.delete(`${BASE}/${characterId}/favourite`);
// Admin-only endpoint
export const listUsers = () => apiClient.get(`${BASE}`);

export default {
    getMyFavourites,
    addFavourite,
    removeFavourite,
    listUsers,
};

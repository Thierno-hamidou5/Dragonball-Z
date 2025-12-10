import apiClient from "./apiClient";

// Service‑Methoden für Character‑Endpunkte.
const BASE = "/api/characters";

// Alle Charaktere laden
export function getCharacters() {
    return apiClient.get(`${BASE}`);
}

// Charakter per Name laden (Backend: /api/characters/name/:name)
export function getCharacterByName(name) {
    const encoded = encodeURIComponent(name);
    return apiClient.get(`${BASE}/name/${encoded}`);
}


export function getCharacterById(id) {
    return apiClient.get(`${BASE}/${id}`);
}

export function getCharactersByRace(race) {
    const encoded = encodeURIComponent(race);
    return apiClient.get(`${BASE}/race/${encoded}`);
}

export function getCharactersByRaceIgnoreCase(race) {
    const encoded = encodeURIComponent(race);
    return apiClient.get(`${BASE}/race-ignore?race=${encoded}`);
}

export function getCharactersByPowerLevel(level) {
    return apiClient.get(`${BASE}/powerlevel/${level}`);
}

export function searchCharactersByName(nameFragment) {
    const encoded = encodeURIComponent(nameFragment);
    return apiClient.get(`${BASE}/search/${encoded}`);
}

export function countCharactersByRace(race) {
    const encoded = encodeURIComponent(race);
    return apiClient.get(`${BASE}/count/race/${encoded}`);
}

export function getTop5ByPowerGreaterThan(minPower) {
    return apiClient.get(`${BASE}/powerlevel-greater-than/${minPower}`);
}

export function existsByNameAndRace(name, race) {
    return apiClient.get(`${BASE}/exists`, {
        params: { name, race },
    });
}

export function createCharacter(data) {
    return apiClient.post(`${BASE}`, data);
}

export function updateCharacter(id, data) {
    return apiClient.put(`${BASE}/${id}`, data);
}

export function deleteCharacter(id) {
    return apiClient.delete(`${BASE}/${id}`);
}

export async function fetchCharactersByAffiliation(affiliation) {
    const response = await apiClient.get(`${BASE}`);
    const normalized = (affiliation || "").toLowerCase();
    return (response.data || []).filter((c) => {
        const aff = Array.isArray(c.affiliation)
            ? c.affiliation.join(", ")
            : c.affiliation || "";
        return aff.toLowerCase() === normalized;
    });
}

export async function fetchVillains() {
    const response = await apiClient.get(`${BASE}`);
    return (response.data || []).filter((c) => {
        const flag =
            typeof c.villain === "boolean"
                ? c.villain
                : `${c.villain}`.toLowerCase() === "true";
        const altFlag =
            typeof c.isVillain === "boolean"
                ? c.isVillain
                : `${c.isVillain}`.toLowerCase() === "true";
        return flag || altFlag;
    });
}

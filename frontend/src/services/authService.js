// Auth-Service: kapselt Login, Token-Handling und Userdaten im LocalStorage.
import apiClient from './apiClient';

const normalizeRole = (roleValue) => {
    if (!roleValue) return null;
    const trimmed = roleValue.trim();
    const withoutPrefix = trimmed.startsWith('ROLE_') ? trimmed.substring(5) : trimmed;
    return withoutPrefix.toUpperCase();
};

// Decode a JWT without verifying its signature.  The token is split
// into its three parts, the payload is Base64 decoded and parsed
// into an object.  The returned object contains the standard JWT
// claims such as 'sub' (subject) and any custom claims defined on
// the server (for example 'roles').
function decodeToken(token) {
    try {
        const payloadPart = token.split('.')[1];
        const decoded = JSON.parse(atob(payloadPart));
        return decoded;
    } catch (e) {
        console.error('Failed to decode JWT:', e);
        return {};
    }
}

export async function login(username, password) {
    // Die Backend‑Route entspricht jetzt /api/auth/login
    const response = await apiClient.post('/api/auth/login', { username, password });
    const accessToken = response.data?.token;
    if (!accessToken) {
        throw new Error('Login response missing token');
    }
    // Token decodieren und Rolle ermitteln (unverändert)
    const decoded = decodeToken(accessToken);
    const usernameFromToken = decoded.sub;
    const usernameFromResponse = response.data?.username;
    const roleFromResponse = response.data?.role;
    const rolesClaim = decoded.roles;

    let role = normalizeRole(roleFromResponse);
    if (!role && rolesClaim) {
        const firstRole = rolesClaim.split(',')[0];
        role = normalizeRole(firstRole);
    }
    if (!role) {
        role = usernameFromToken && usernameFromToken.toLowerCase() === 'admin' ? 'ADMIN' : 'PLAYER';
    }
    const userId = response.data?.userId || decoded.userId || null;
    const user = { username: usernameFromResponse || usernameFromToken, role, userId };
    localStorage.setItem('authToken', accessToken);
    localStorage.setItem('userData', JSON.stringify(user));
    return { token: accessToken, user };
}

export function logout() {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userData');
}

export function getToken() {
    return localStorage.getItem('authToken');
}

export function getUserData() {
    const data = localStorage.getItem('userData');
    if (!data) return null;
    try {
        const parsed = JSON.parse(data);
        return {
            ...parsed,
            role: normalizeRole(parsed.role),
        };
    } catch (e) {
        console.error('Failed to parse userData from localStorage:', e);
        return null;
    }
}

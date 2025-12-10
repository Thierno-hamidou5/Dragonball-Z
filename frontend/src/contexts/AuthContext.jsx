import React, { createContext, useState, useEffect, useContext } from 'react';
import { login as loginService, logout as logoutService, getToken, getUserData } from '../services/authService';

export const AuthContext = createContext();

/**
 * AuthContext/Provider: haelt User, Token und Login/Logout-Methoden
 * zentral, dokumentiert im gleichen Stil wie die Backend-Klassen.
 */
export const AuthProvider = ({ children }) => {
    // Current user (contains username and role), JWT token, and flags
    // indicating whether the user is authenticated and whether the
    // initial loading has completed.
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(null);
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [loading, setLoading] = useState(true);

    // On mount, load any persisted authentication information from
    // localStorage and update state accordingly.
    useEffect(() => {
        const storedToken = getToken();
        const storedUser = getUserData();
        if (storedToken && storedUser) {
            setToken(storedToken);
            setUser(storedUser);
            setIsAuthenticated(true);
        }
        setLoading(false);
    }, []);

    /**
     * Log in with the given credentials.  On success the token and user
     * information are stored in state and localStorage.  If the
     * underlying service throws, the state remains unchanged and the
     * error propagates.
     */
    const login = async (username, password) => {
        const { token: newToken, user: newUser } = await loginService(username, password);
        setToken(newToken);
        setUser(newUser);
        setIsAuthenticated(true);
        // Persist credentials so page reloads keep the session.
        localStorage.setItem('authToken', newToken);
        localStorage.setItem('userData', JSON.stringify(newUser));
    };

    /**
     * Log out the current user.  Clears state and removes stored
     * credentials.
     */
    const logout = () => {
        logoutService();
        setToken(null);
        setUser(null);
        setIsAuthenticated(false);
    };

    // The value exposed by the context includes the user, token and
    // authentication flags as well as the login/logout functions.
    const value = {
        user,
        token,
        isAuthenticated,
        loading,
        login,
        logout,
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

/**
 * Hook to access the authentication context. Throws an error if
 * called outside of an AuthProvider.
 */
export function useAuth() {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
}

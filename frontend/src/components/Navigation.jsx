import React from 'react';
import { NavLink } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const linkStyle = { marginRight: '1rem' };

/**
 * Frontend-Komponente: Hauptnavigation mit rollenabhaengigen Links
 * und Logout-Button, dokumentiert wie die Backend-Klassen.
 */
const Navigation = () => {
    const { isAuthenticated, user, logout } = useAuth();

    return (
        <nav
            style={{
                padding: '1rem',
                borderBottom: '1px solid #ccc',
                display: 'flex',
                gap: '1rem',
                alignItems: 'center',
            }}
        >
            {/* Home zeigt alle Charaktere */}
            <NavLink to="/" end style={linkStyle}>
                Home
            </NavLink>
            {/* Z-Fighters separat */}
            <NavLink to="/z-fighters" style={linkStyle}>
                Z Fighters
            </NavLink>
            <NavLink to="/villains" style={linkStyle}>
                Villains
            </NavLink>
            {isAuthenticated && (
                <NavLink to="/favorites" style={linkStyle}>
                    Favorites
                </NavLink>
            )}
            {isAuthenticated && user?.role === 'ADMIN' && (
                <NavLink to="/manage-characters" style={linkStyle}>
                    Manage Characters
                </NavLink>
            )}
            {!isAuthenticated ? (
                <NavLink to="/login" style={linkStyle}>
                    Login
                </NavLink>
            ) : (
                <button type="button" onClick={logout} style={{ marginLeft: 'auto' }}>
                    Logout ({user.username})
                </button>
            )}
        </nav>
    );
};

export default Navigation;

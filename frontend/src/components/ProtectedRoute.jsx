import React from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

/**
 * Route-Guard: leitet nicht eingeloggte User auf /login und prueft
 * optional Rollen, dokumentiert im selben Stil wie die Backend-Klassen.
 */
const ProtectedRoute = ({ children, requiredRole }) => {
    const { isAuthenticated, user, loading } = useAuth();
    // Waehren Laden nichts rendern, damit die Authentifizierung zuerst geklaert wird.
    if (loading) {
        return null;
    }
    if (!isAuthenticated) {
        return <Navigate to="/login" replace />;
    }
    if (requiredRole && user?.role !== requiredRole) {
        return <Navigate to="/forbidden" replace />;
    }
    return children;
};

export default ProtectedRoute;

import React, { useState } from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import Layout from "./components/Layout";
import ProtectedRoute from "./components/ProtectedRoute";
import Z_Fighters from "./pages/Z_Fighters";
import Villains from "./pages/Villains";
import CharacterDetail from "./pages/CharacterDetail";
import NeuCharacters from "./pages/NeuCharacters";
import ManageCharacters from "./pages/ManageCharacters";
import FavoritesPage from "./pages/FavoritesPage";
import Login from "./pages/Login";
import Forbidden from "./pages/Forbidden";
import Home from "./pages/Home";

/**
 * SPA-Root: definiert alle Routen und schuetzt sie per ProtectedRoute,
 * aehnlich wie die Klassenkommentare im Backend.
 */
function App() {
  const [customCharacters, setCustomCharacters] = useState([]);

  return (
      <Routes>
        <Route path="/" element={<Layout />}>
          {/* Home zeigt alle Charaktere */}
          <Route
              index
              element={
                <ProtectedRoute>
                  <Home />
                </ProtectedRoute>
              }
          />
          {/* Z-Fighters separat unter /z-fighters */}
          <Route
              path="z-fighters"
              element={
                <ProtectedRoute>
                  <Z_Fighters />
                </ProtectedRoute>
              }
          />
          {/* Villains separat unter /villains */}
          <Route
              path="villains"
              element={
                <ProtectedRoute>
                  <Villains />
                </ProtectedRoute>
              }
          />
          <Route
              path="characters/:id"
              element={
                <ProtectedRoute>
                  <CharacterDetail
                      customCharacters={customCharacters}
                      setCustomCharacters={setCustomCharacters}
                  />
                </ProtectedRoute>
              }
          />
          <Route
              path="new-characters"
              element={
                <ProtectedRoute requiredRole="ADMIN">
                  <NeuCharacters
                      customCharacters={customCharacters}
                      setCustomCharacters={setCustomCharacters}
                  />
                </ProtectedRoute>
              }
          />
          {/* Admin-only route for managing characters via the backend */}
          <Route
              path="manage-characters"
              element={
                <ProtectedRoute requiredRole="ADMIN">
                  <ManageCharacters />
                </ProtectedRoute>
              }
          />
          <Route
              path="favorites"
              element={
                <ProtectedRoute>
                  <FavoritesPage />
                </ProtectedRoute>
              }
          />
          <Route path="login" element={<Login />} />
          <Route path="forbidden" element={<Forbidden />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Route>
      </Routes>
  );
}

export default App;

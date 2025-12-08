// React StrictMode hilft bei der Identifikation potenzieller Probleme
import { StrictMode } from "react";

// createRoot ist der moderne Weg, React-Apps zu starten (seit React 18)
import { createRoot } from "react-dom/client";

// Haupt-CSS für globale Styles
import "./index.css";

// Die Hauptkomponente deiner App
import App from "./App.jsx";

// Zusätzliche App-spezifische Styles (z. B. Navigation, Layout)
import "./app.css";

// React Router zum Navigieren zwischen Seiten
import { BrowserRouter } from "react-router-dom";

// Die App wird im <div id="root"> aus index.html gerendert
createRoot(document.getElementById("root")).render(
  <StrictMode>
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </StrictMode>
);

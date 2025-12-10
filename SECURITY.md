# Sicherheitskonzept – Dragonball Gesamtsystem (Backend + Frontend)

## Ziele
- Schutz des Gesamtsystems vor unautorisierten Zugriffen und Manipulationen.
- Konsistente und sichere Verwaltung von JWT-Tokens und Benutzerrollen zwischen Backend und Frontend.
- Vermeidung von Datenlecks und Missbrauch von Passwoertern, Tokens und Favoriten-/Charakterdaten.

## Authentifizierung & Autorisierung
- **JWT**: Das Backend generiert signierte Tokens (HS256) beim Login und sendet sie an das Frontend.  
  Das Subject ist der Benutzername; das Token enthaelt `role` und `userId`.
- **Rollenmodell**:
  - **ADMIN**: Charakterverwaltung (CRUD)
  - **PLAYER**: Lesen und Favoriten pflegen
- **Token-Weitergabe**:  
  Das Frontend speichert das Token im `localStorage` und fuegt es via Axios-Interceptor an jeden API-Request an.  
  Das Backend validiert das Token bei jeder Anfrage.
- **Invalidierung**:  
  Bei **401/403** loescht das Frontend Token und Benutzerdaten und leitet zum Login weiter.

## Transport & CORS
- **HTTPS** fuer saemtlichen Verkehr zwischen Client und Server.
- Das Backend laesst nur konfigurierte Origins zu; im Dev-Modus wird durch Vite kein CORS benoetigt.

## Session-/Token-Speicherung
- Das System bleibt **stateless**: Tokens werden nicht serverseitig gespeichert.
- Persistenz im `localStorage` erlaubt Wiederanmeldung nach einem Reload, birgt aber bei XSS die Gefahr des Token-Diebstahls → gute XSS-Haertung des Frontends ist essentiell.
- Es werden **keine Cookies** fuer die Authentifizierung verwendet.

## Passwort- & Nutzerdaten
- Passwoerter werden ausschliesslich **gehasht (BCrypt)** gespeichert.
- Der Server gibt niemals Klartextpasswoerter zurueck.
- Fehlermeldungen beim Login geben keine Hinweise auf Benutzername oder Passwort.

## Endpunkt-Absicherung (Backend)
- **Frei zugaenglich**:  
  `/api/auth/**`, `/v3/api-docs/**`, `/swagger-ui/**`
- **Geschuetzt (JWT erforderlich)**:  
  alle `/api/**` Endpunkte
- **Nur ADMIN**:  
  `POST`, `PUT`, `DELETE` auf `/api/characters/**`

## Eingabevalidierung & Fehlerhandling
- **Backend**:
  - Bean-Validation
  - strukturierte `ErrorResponseDTO`
  - Serviceebene validiert Geschaeftsregeln
- **Frontend**:
  - Formvalidierung
  - Benutzerfreundliche Fehlermeldungen

## Datenbank & Referenzen
- Many-to-Many Beziehung `user_favourite`.  
  Beim Loeschen eines Charakters muessen zugehoerige Favoriten vorher entfernt werden → **transaktional**.
- Mehrstufige Operationen laufen unter `@Transactional`.
- DB-User besitzt nur minimal notwendige Rechte (**Least Privilege**).

## Logging & Monitoring
- Keine sensiblen Daten (Tokens, Passwoerter, Secrets) loggen.
- 401/403-Antworten koennen auf Angriffsversuche hinweisen.
- Stacktraces sind in Produktion deaktiviert oder minimiert.

## Secrets & Konfiguration
- JWT-Secret und DB-Credentials ueber **Environment Variablen**, `.env` oder Secret Manager.
- Keine sensiblen Werte im Repository.

## Frontend-Haertung
- Keine untrusted Skripte; lokale Fallbacks fuer Bilder.
- UI zeigt Funktionen nur, wenn die Rolle passt; die eigentliche Autorisierung passiert serverseitig.
- Keine Verwendung von `dangerouslySetInnerHTML`.

## API-Nutzung (Frontend)
- Zentrale Axios-Instanz (`apiClient`) mit:
  - Request-Interceptor (Token hinzufuegen)
  - Response-Interceptor (Fehler global behandeln)
- Vite dev-proxy verhindert CORS-Konflikte.

## Tests & Qualitaet
### Backend:
- JUnit, Mockito, H2
- Tests fuer:
  - Authentifizierung
  - Tokenvalidierung
  - Rollenpruefung
  - Zugriff auf offene/geschuetzte Endpunkte

### Frontend:
- Vitest, React Testing Library
- Tests fuer:
  - Rendering
  - `ProtectedRoute`
  - Favoritenverwaltung
  - CRUD-Formulare

### End-to-End:
- Playwright oder Cypress fuer den gesamten Flow:
  - Login
  - Favoriten
  - Admin-CRUD

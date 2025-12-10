# Dragonball Datenbank - Gesamtprojekt

Dieses Repository enthaelt sowohl das Backend (Spring Boot) als auch das Frontend (React) einer Dragonball Charakter-Datenbank. Ziel ist es, bestehende Dragonball-Figuren zu durchsuchen, zu verwalten und personalisierte Favoritenlisten zu pflegen. Administrierende koennen neue Charaktere anlegen, bearbeiten und loeschen.

## Inhaltsuebersicht
- Projektuebersicht
- Architekturdiagramme
- Technologie-Stack
- Setup & Ausfuehrung
- Ordnerstruktur

## Projektuebersicht
Die Anwendung besteht aus einem Backend (Java, Spring Boot), das ueber eine REST-API Charakterdaten bereitstellt und Nutzerdaten inkl. JWT-Authentifizierung verwaltet, und einem Frontend (React, Vite), das diese API konsumiert, Daten darstellt und Benutzereingaben entgegennimmt. Die Kommunikation erfolgt per HTTP mit JSON.

## Architekturdiagramme
### JWT-Authentifizierungs-Flow
```
Frontend                               Backend
   |-- POST /auth/login {username, password} -->|
   |<-- 200 OK { token, username, role }       |
   | (speichert Token in localStorage)         |
   |-- GET /api/characters (Authorization: Bearer <token>) -->|
   |<-- 200 OK + Character List                |
```

### Backend Layer-Architektur
```
Controller Layer:
  AuthController
  CharacterController
  UserController
      |
Service Layer:
  UserService
  CharacterService
  JwtService
      |
Repository Layer:
  UserRepository
  CharacterRepository
      |
Datenbank (PostgreSQL/H2):
  app_user
  character
  user_favourite
  character_transformations
```

### Frontend-Komponenten-Hierarchie
```
App.jsx
├─ Layout.jsx
│  ├─ Navigation.jsx
│  ├─ <Outlet /> (React Router)
│  └─ Footer.jsx
└─ Pages:
   ├─ Home.jsx
   ├─ Z_Fighters.jsx
   ├─ Villains.jsx
   ├─ CharacterDetail.jsx
   ├─ FavoritesPage.jsx
   ├─ ManageCharacters.jsx  (nur Admin)
   ├─ Login.jsx
   └─ Forbidden.jsx
```

## Technologie-Stack
| Technologie        | Version | Verwendung                     |
|--------------------|---------|--------------------------------|
| Java               | 17      | Backend-Sprache                |
| Spring Boot        | 3.3.x   | Backend-Framework              |
| Spring Security    | 6.x     | Authentifizierung & AuthZ      |
| JWT (jjwt)         | 0.12.x  | Token-basierte Auth            |
| JPA/Hibernate      | 6.x     | ORM fuer Datenbank             |
| PostgreSQL         | 16      | Relationale Datenbank          |
| H2                 | 2.x     | In-Memory DB fuer Tests        |
| Maven              | 3.x     | Build-Tool & Dependencies      |
| Node / npm         | 18+/10+ | Frontend-Tooling               |
| React + Router     | 18.x    | SPA & Routing                  |
| Vite               | 5.x     | Dev-Server/Bundling            |
| Axios              | 1.x     | HTTP-Client mit Interceptor    |
| Vitest/RTL         | 1.x     | Frontend-Tests                 |

## Setup & Ausfuehrung
### Backend starten
```bash
cd backend
mvn spring-boot:run
```
Swagger UI: http://localhost:8080/swagger-ui/index.html

### Frontend starten
```bash
cd frontend
npm install
npm run dev
```
Anwendung: http://localhost:5173

### Login-Daten (Beispiel)
- Admin: Benutzername `admin`, Passwort `admin123`
- Player: Benutzername `player`, Passwort `player123`

## Ordnerstruktur
```
ddragonball_Z_repo/
├── backend/                          # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/wiss/dragonball/backend/
│   │   │   │   ├── config/
│   │   │   │   ├── controller/
│   │   │   │   ├── dto/
│   │   │   │   ├── entity/
│   │   │   │   ├── exception/
│   │   │   │   ├── mapper/
│   │   │   │   ├── repository/
│   │   │   │   ├── security/
│   │   │   │   └── service/
│   │   │   └── resources/
│   │   └── test/
│   ├── img/
│   ├── pom.xml
│   └── target/
│
├── docs/                            # Projektdokumentation
│   ├── dokumentation.md
│   ├── Dragonball Dokumentation.pdf
│   └── img/
│
├── frontend/                         # React Frontend (Vite)
│   ├── public/
│   │   ├── img/
│   │   └── index.html
│   ├── src/
│   │   ├── assets/
│   │   ├── components/
│   │   ├── contexts/
│   │   ├── pages/
│   │   ├── services/
│   │   ├── App.jsx
│   │   ├── main.jsx
│   │   └── index.css
│   ├── tests/
│   ├── .env
│   ├── package.json
│   ├── vite.config.js
│   └── node_modules/
│
├── SECURITY.md                       # Sicherheitskonzept
├── README.md                         # Dieses Dokument
└── .gitignore

```

## 📘 Projektdokumentation
Für alle weiteren Details – inklusive Architektur, Datenmodell, API-Spezifikation,  
Sicherheitskonzept, Testprotokollen und Entwicklungsprozess – siehe die vollständige Dokumentation:
➡️ [PDF Dokumentation](docs/Dragonball_Dokumentation.pdf)

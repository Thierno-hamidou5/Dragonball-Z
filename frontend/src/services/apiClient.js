import axios from "axios";

// Base-URL ohne /api-Praefix, damit auch /auth und /users erreicht werden.
//
// Falls VITE_API_BASE_URL gesetzt ist, nutzt Axios diese Adresse fuer
// Anfragen (z.B. http://localhost:8080). Andernfalls wird ein leerer
// String verwendet, sodass relative Pfade gegen das Origin des Frontends
// laufen. Damit funktionieren die API-Aufrufe auch im Deployment, ohne
// dass localhost verwendet werden muss.
const API_BASE_URL = import.meta.env?.VITE_API_BASE_URL || "";

const apiClient = axios.create({
    baseURL: API_BASE_URL,
    timeout: 10000,
    headers: {
        "Content-Type": "application/json",
    },
});

// Attach JWT from localStorage (key: authToken) to every request.
apiClient.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("authToken");
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
            console.debug("Sending request with bearer token");
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// Handle auth errors globally.
apiClient.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response) {
            const status = error.response.status;
            if (status === 401) {
                localStorage.removeItem("authToken");
                localStorage.removeItem("userData");
                window.location.href = "/login";
            }
            if (status === 403) {
                console.warn("Missing permission for this action");
            }
        }
        return Promise.reject(error);
    }
);

export default apiClient;

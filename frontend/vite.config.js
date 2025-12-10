import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  test: {
    environment: "jsdom",
    setupFiles: "./setupTests.js",
  },
  server: {
    proxy: {
      // Forward API requests to the Spring Boot backend in dev to avoid 404s
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
      },
    },
  },
});

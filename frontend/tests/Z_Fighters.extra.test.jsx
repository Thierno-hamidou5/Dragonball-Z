import { render, screen, waitFor } from "@testing-library/react";
import { describe, test, expect, vi } from "vitest";
import { MemoryRouter } from "react-router-dom";
import axios from "axios";
import Z_Fighters from "../src/pages/Z_Fighters";

// Fake-Daten für den Mock
const mockCharacters = [
  {
    id: "1",
    name: "Goku",
    image: "/img/goku.jpg",
    race: "Saiyan",
    gender: "Male",
    ki: "5000",
    maxKi: "10000",
    affiliation: "Z Fighter",
  },
];

// Mock von axios
vi.mock("axios");

describe("Z_Fighters", () => {
  test("lädt und zeigt Z-Fighter Charaktere", async () => {
    axios.get.mockResolvedValueOnce({ data: mockCharacters });

    render(
      <MemoryRouter>
        <Z_Fighters />
      </MemoryRouter>
    );

    // Warten bis der Charakter-Name erscheint
    await waitFor(() => {
      expect(screen.getByText("Goku")).toBeInTheDocument();
    });
  });
});

// Ob ein bestimmter Charakter (z. B. Goku) geladen und angezeigt wird.

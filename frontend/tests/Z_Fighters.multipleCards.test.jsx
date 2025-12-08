import { render, screen, waitFor } from "@testing-library/react";
import { describe, it, expect } from "vitest";
import Z_Fighters from "../src/pages/Z_Fighters";
import { MemoryRouter } from "react-router-dom";

describe("Z_Fighters", () => {
  it("rendert mehrere Charakterkarten", async () => {
    render(
      <MemoryRouter>
        <Z_Fighters />
      </MemoryRouter>
    );

    // Warten bis mindestens 2 Charaktere angezeigt werden
    await waitFor(() => {
      const cards = screen.getAllByRole("heading", { level: 2 });
      expect(cards.length).toBeGreaterThanOrEqual(2);
    });
  });
});

// prüft, Ob mehrere Charakterkarten (mindestens 2) gerendert werden. 
// Das bedeutet: es wird erwartet, dass mehrere Z Fighters existieren.

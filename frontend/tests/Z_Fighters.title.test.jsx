import { render, screen } from "@testing-library/react";
import { describe, it, expect } from "vitest";
import Z_Fighters from "../src/pages/Z_Fighters";
import { MemoryRouter } from "react-router-dom";

describe("Z_Fighters", () => {
  it("zeigt den Titel 'Z Fighters' korrekt an", () => {
    render(
      <MemoryRouter>
        <Z_Fighters />
      </MemoryRouter>
    );

    const title = screen.getByRole("heading", { level: 2 });
    expect(title.textContent.toLowerCase()).toContain("z fighters");
  });
});

// prüft, Ob der Titel der Seite ein <h2>-Element mit dem Text Z Fighters enthält (ohne Rücksicht auf Gross-/Kleinschreibung).

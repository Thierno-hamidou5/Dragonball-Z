// tests/Z_Fighters.image.test.jsx
import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import Z_Fighters from "../src/pages/Z_Fighters"; // Passe ggf. den Pfad an
import { BrowserRouter } from "react-router-dom";

describe("Z_Fighters", () => {
  it("zeigt ein Charakterbild", async () => {
    render(
      <BrowserRouter>
        <Z_Fighters />
      </BrowserRouter>
    );

    // Warte, bis ein Bild mit Alt-Text z. B. "Goku" erscheint
    const image = await screen.findByAltText(/goku/i);
    expect(image).toBeInTheDocument();
    expect(image.tagName).toBe("IMG");
  });
});

// prüft, Ob ein Charakterbild (z. B. das <img>-Element mit alt="Goku") sichtbar ist.

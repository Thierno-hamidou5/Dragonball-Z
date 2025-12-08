import { render, screen } from "@testing-library/react";
import { describe, test, expect } from "vitest";
import Z_Fighters from "../src/pages/Z_Fighters";

describe("Z Fighters Page", () => {
  test("zeigt Titel 'Z Fighters'", () => {
    render(<Z_Fighters />);
    const heading = screen.getByRole("heading", { level: 2 });
    expect(heading.textContent.toLowerCase()).toContain("z fighters");
  });
});

// Prüft, Ob die Z_Fighters-Komponente den Titel Z Fighters korrekt anzeigt.




import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, it, expect, beforeEach, vi } from "vitest";
import { AuthProvider, useAuth } from "../contexts/AuthContext";

// Mock the authService functions used by AuthContext.  We stub
// `login` to resolve immediately with a fake token and user data,
// and leave the other functions as no-ops.
vi.mock("../services/authService", () => {
    return {
        login: vi.fn().mockResolvedValue({
            token: "fake.jwt.token",
            user: { username: "player", role: "PLAYER" },
        }),
        logout: vi.fn(),
        getToken: vi.fn().mockReturnValue(null),
        getUserData: vi.fn().mockReturnValue(null),
    };
});

describe("AuthContext", () => {
    // Clear localStorage before each test to avoid leakage.
    beforeEach(() => {
        localStorage.clear();
    });

    it("should log in and expose user role", async () => {
        // Create a simple component to interact with the context.
        function LoginTester() {
            const { user, isAuthenticated, login } = useAuth();
            return (
                <div>
                    <span data-testid="role">{user?.role || "none"}</span>
                    <span data-testid="auth">{isAuthenticated ? "true" : "false"}</span>
                    <button
                        onClick={() => login("player", "player123")}
                        data-testid="loginBtn"
                    >
                        Login
                    </button>
                </div>
            );
        }

        render(
            <AuthProvider>
                <LoginTester />
            </AuthProvider>
        );

        // Initially the user is not authenticated and has no role.
        expect(screen.getByTestId("role").textContent).toBe("none");
        expect(screen.getByTestId("auth").textContent).toBe("false");

        // Trigger the login.  userEvent simulates the click.
        const user = userEvent.setup();
        await user.click(screen.getByTestId("loginBtn"));

        // Wait for the context state to update.
        await waitFor(() => {
            expect(screen.getByTestId("auth").textContent).toBe("true");
            expect(screen.getByTestId("role").textContent).toBe("PLAYER");
        });

        // Verify that the token was stored in localStorage.
        expect(localStorage.getItem("authToken")).toBe("fake.jwt.token");
        // Also check that user data was persisted.
        const storedUser = JSON.parse(localStorage.getItem("userData"));
        expect(storedUser).toEqual({ username: "player", role: "PLAYER" });
    });
});

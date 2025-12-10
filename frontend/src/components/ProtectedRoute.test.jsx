import { render, screen } from "@testing-library/react";
import { describe, it, expect, beforeEach, vi } from "vitest";
import { MemoryRouter, Routes, Route } from "react-router-dom";
import ProtectedRoute from "../components/ProtectedRoute";

// We will mock the useAuth hook returned by the context.  To do
// this we create a mutable mock function and have the context
// module's useAuth return whatever our mock returns.
const mockUseAuth = vi.fn();

vi.mock("../contexts/AuthContext", () => {
    return {
        useAuth: () => mockUseAuth(),
    };
});

describe("ProtectedRoute", () => {
    beforeEach(() => {
        // Reset the mock before each test.
        mockUseAuth.mockReset();
    });

    it("redirects unauthenticated users to /login", () => {
        // Simulate an unauthenticated user.
        mockUseAuth.mockReturnValue({
            isAuthenticated: false,
            user: null,
            loading: false,
        });

        render(
            <MemoryRouter initialEntries={["/protected"]}>
                <Routes>
                    <Route
                        path="/protected"
                        element={
                            <ProtectedRoute>
                                <div>Protected Content</div>
                            </ProtectedRoute>
                        }
                    />
                    <Route path="/login" element={<div>Login Page</div>} />
                </Routes>
            </MemoryRouter>
        );

        // Because the user is not authenticated, the login page should be shown.
        expect(screen.getByText("Login Page")).toBeInTheDocument();
    });

    it("redirects users with insufficient role to /forbidden", () => {
        // Simulate an authenticated player trying to access an admin route.
        mockUseAuth.mockReturnValue({
            isAuthenticated: true,
            user: { role: "PLAYER" },
            loading: false,
        });

        render(
            <MemoryRouter initialEntries={["/admin"]}>
                <Routes>
                    <Route
                        path="/admin"
                        element={
                            <ProtectedRoute requiredRole="ADMIN">
                                <div>Admin Content</div>
                            </ProtectedRoute>
                        }
                    />
                    <Route path="/forbidden" element={<div>Forbidden Page</div>} />
                </Routes>
            </MemoryRouter>
        );

        // The player does not have admin privileges, so the forbidden page should be shown.
        expect(screen.getByText("Forbidden Page")).toBeInTheDocument();
    });

    it("allows access when the user has the required role", () => {
        // Simulate an authenticated admin.
        mockUseAuth.mockReturnValue({
            isAuthenticated: true,
            user: { role: "ADMIN" },
            loading: false,
        });

        render(
            <MemoryRouter initialEntries={["/admin"]}>
                <Routes>
                    <Route
                        path="/admin"
                        element={
                            <ProtectedRoute requiredRole="ADMIN">
                                <div>Admin Content</div>
                            </ProtectedRoute>
                        }
                    />
                </Routes>
            </MemoryRouter>
        );

        // The admin has the required role, so the protected content should be rendered.
        expect(screen.getByText("Admin Content")).toBeInTheDocument();
    });
});
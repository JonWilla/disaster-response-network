import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/api";

function LoginPage() {
    const navigate = useNavigate();

    const [email, setEmail] =
        useState("jonny.test@example.com");

    const [password, setPassword] =
        useState("postgres");

    const [error, setError] =
        useState("");

    const handleSubmit = async (event) => {
        event.preventDefault();

        setError("");

        try {
            const response =
                await api.post(
                    "/auth/login",
                    {
                        email,
                        password,
                    }
                );

            localStorage.setItem(
                "token",
                response.data.token
            );

            navigate("/dashboard");
        } catch {
            setError(
                "Login failed. Check email and password."
            );
        }
    };

    return (
        <div className="login-page">
            <form
                className="login-card"
                onSubmit={handleSubmit}
            >
                <h1>Disaster Response Network</h1>

                <p>
                    Emergency Operations Dashboard
                </p>

                <input
                    type="email"
                    value={email}
                    placeholder="Email"
                    onChange={(event) =>
                        setEmail(event.target.value)
                    }
                />

                <input
                    type="password"
                    value={password}
                    placeholder="Password"
                    onChange={(event) =>
                        setPassword(event.target.value)
                    }
                />

                <button type="submit">
                    Sign In
                </button>

                {error && (
                    <p className="error">
                        {error}
                    </p>
                )}
            </form>
        </div>
    );
}

export default LoginPage;
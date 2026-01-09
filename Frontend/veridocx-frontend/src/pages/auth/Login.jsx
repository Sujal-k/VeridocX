import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import "../../styles/auth.css";

const Login = () => {
  const navigate = useNavigate();
  const { login } = useAuth();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      await login(email, password);
      navigate("/dashboard");   // go dashboard
    } catch (err) {
      setError("Invalid email or password");
    }

    setLoading(false);
  };

  return (
    <div className="auth-wrapper">
      <div className="main-glow"></div>
      <div className="accent-glow"></div>

      <div className="login-container">
        <div className="glass-card">
          <div className="brand-header">
            <div className="neon-logo">V</div>
            <h1 className="project-title">VERIDOCX</h1>
            <span className="badge">SECURE ACCESS</span>
          </div>

          <div className="form-content">
            <h2>Welcome Back</h2>
            <p>Enter your credentials to access the vault</p>

            {error && <p style={{ color: "#ff6b6b" }}>{error}</p>}

            <form onSubmit={handleSubmit}>
              <div className="input-box">
                <input
                  type="email"
                  placeholder="Email Address"
                  required
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
              </div>

              <div className="input-box">
                <input
                  type="password"
                  placeholder="Password"
                  required
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </div>

              <button className="neon-button" disabled={loading}>
                <span>{loading ? "Signing in..." : "SIGN IN"}</span>
                <div className="button-glow"></div>
              </button>
            </form>
          </div>

          <div className="card-footer">
            <p>
              New to Veridocx? <a href="/register">Create Account</a>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;

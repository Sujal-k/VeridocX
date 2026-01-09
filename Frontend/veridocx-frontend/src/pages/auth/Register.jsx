import React, { useState } from "react";
import { registerApi } from "../../api/authApi";
import "../../styles/auth.css";

const Register = () => {

  const [form, setForm] = useState({
    name: "",
    email: "",
    password: ""
  });

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await registerApi(form);
      const token = res.data.token;
      localStorage.setItem("token", token);
      alert("Account created successfully!");
      window.location.href = "/login";
    } catch (err) {
      console.error(err);
      alert("Registration failed");
    }
  };

  return (
    <div className="auth-wrapper">
      <div className="main-glow"></div>

      <div className="glass-card">
        <div className="brand-header">
          <div className="neon-logo">V</div>
          <h1 className="project-title">VERIDOCX</h1>
          <span className="badge">CREATE ACCOUNT</span>
        </div>

        <form className="form-content" onSubmit={handleSubmit}>
          <div className="input-box">
            <input
              type="text"
              name="name"
              placeholder="Full Name"
              value={form.name}
              onChange={handleChange}
              required
            />
          </div>

          <div className="input-box">
            <input
              type="email"
              name="email"
              placeholder="Email Address"
              value={form.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="input-box">
            <input
              type="password"
              name="password"
              placeholder="Password"
              value={form.password}
              onChange={handleChange}
              required
            />
          </div>

          <button type="submit" className="neon-button">
            SIGN UP
          </button>
        </form>

        <div className="card-footer">
          <p>
            Already have an account? <a href="/login">Login</a>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Register;

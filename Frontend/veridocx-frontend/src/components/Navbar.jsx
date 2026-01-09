import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Navbar() {
  const { logout, user } = useAuth();

  return (
    <nav style={styles.nav}>
      <h2 style={styles.logo}>VeriDocX</h2>

      {user && (
        <div style={styles.menu}>
          <Link to="/" style={styles.link}>Dashboard</Link>
          <Link to="/documents" style={styles.link}>Documents</Link>
          <Link to="/verify" style={styles.link}>Verify</Link>

          <button onClick={logout} style={styles.logout}>
            Logout
          </button>
        </div>
      )}
    </nav>
  );
}

const styles = {
  nav: {
    padding: "14px 25px",
    background: "#fff",
    borderBottom: "1px solid #eee",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
  },
  logo: { margin: 0, color: "#111" },
  menu: { display: "flex", gap: "18px", alignItems: "center" },
  link: { textDecoration: "none", color: "#333", fontSize: "14px" },
  logout: {
    padding: "6px 14px",
    borderRadius: "6px",
    border: "none",
    cursor: "pointer",
    background: "#e74c3c",
    color: "#fff",
  },
};

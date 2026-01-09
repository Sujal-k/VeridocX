import { createContext, useContext, useState } from "react";
import api from "../api/axiosInstance";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(
    localStorage.getItem("token") || null
  );

  const login = async (email, password) => {
    const res = await api.post("/api/auth/login", { email, password });

    setToken(res.data.token);
    localStorage.setItem("token", res.data.token);

    setUser(res.data.user || { email });
  };

  const logout = () => {
    localStorage.removeItem("token");
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, token, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);

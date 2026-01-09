import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ children }) => {
  const token = localStorage.getItem("token");

  // if user not logged in → redirect
  if (!token) {
    return <Navigate to="/login" replace />;
  }

  // otherwise allow access
  return children;
};

export default ProtectedRoute;

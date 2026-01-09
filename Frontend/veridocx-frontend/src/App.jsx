import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import AppRouter from "./router/AppRouter";
import { AuthProvider } from "./context/AuthContext.jsx";
import './App.css';


function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <AppRouter />
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { Dashboard } from '../pages/dashboard/Dashboard';
import Login from '../pages/auth/Login';
import Register from '../pages/auth/Register';
import Verification from '../pages/verification/Verification';
import Documents from '../pages/documents/Documents';
import Layout from '../components/Layout'; // This is your sidebar wrapper
import ProtectedRoute from "../components/auth/ProtectedRoute";
import SharedDocuments from "../pages/documents/SharedDocuments";



export default function AppRouter() {
  return (
    <Routes>
      {/* 1. PUBLIC ROUTES (Full Screen - No Sidebar) */}
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />

      {/* 2. PROTECTED ROUTES (Inside Layout - With Sidebar) */}
     <Route element={<Layout />}>
  
  <Route
    path="/dashboard"
    element={
      <ProtectedRoute>
        <Dashboard />
      </ProtectedRoute>
    }
  />

  <Route
    path="/documents"
    element={
      <ProtectedRoute>
        <Documents />
      </ProtectedRoute>
    }
  />

  <Route
    path="/shared"
    element={
      <ProtectedRoute>
        <SharedDocuments />
      </ProtectedRoute>
    }
  />

  <Route
    path="/verification"
    element={
      <ProtectedRoute>
        <Verification />
      </ProtectedRoute>
    }
  />

</Route>

      {/* Default Redirect */}
      <Route path="/" element={<Navigate to="/login" />} />
    </Routes>
  );
}
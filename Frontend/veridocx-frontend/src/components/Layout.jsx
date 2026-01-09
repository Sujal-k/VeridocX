import React from 'react';
import { Outlet, Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { LayoutDashboard, FileUp, FolderOpen, LogOut } from 'lucide-react';
import '../styles/layout.css';

const Layout = () => {
  const { logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="app-container">
      <aside className="sidebar">
        <div className="sidebar-header">
          <div className="logo-box">V</div>
           <h2 className="logo-name">Veridocx</h2>
        </div>
        
        <nav className="sidebar-nav">
          <Link to="/dashboard"><LayoutDashboard size={20}/> Dashboard</Link>
          <Link to="/verification"><FileUp size={20}/> Verify Document</Link>
          <Link to="/documents"><FolderOpen size={20}/> My Vault</Link>
          <Link to="/shared" >Shared With Me</Link>

        </nav>

        <button onClick={handleLogout} className="logout-btn">
          <LogOut size={20}/> Logout
        </button>
      </aside>

      <main className="content-area">
        <Outlet />
      </main>
    </div>
  );
};

export default Layout;
import React from 'react';
import { useAuth } from '../../context/AuthContext';
import { 
  ShieldCheck, 
  AlertTriangle, 
  FileText, 
  Activity, 
  Clock, 
  TrendingUp 
} from 'lucide-react';
import '../../styles/dashboard.css';

export function Dashboard() {
  const { token } = useAuth();

  // Mock stats for that "High-End" look
  const stats = [
    { label: "Documents Scanned", value: "1,284", icon: <FileText color="#38bdf8"/>, trend: "+12%" },
    { label: "Tampering Detected", value: "14", icon: <AlertTriangle color="#f87171"/>, trend: "4% total" },
    { label: "System Integrity", value: "99.9%", icon: <ShieldCheck color="#4ade80"/>, trend: "Stable" },
  ];

  // if (!token) {
  //   return (
  //       <div className="flex-center-full">
  //           <div className="loader-container">
  //               <div className="spinner"></div>
  //               <h2 className="redirect-text">Redirecting to secure login...</h2>
  //           </div>
  //       </div>
  //   );
  // }

  return (
    <div className="dashboard-content">
      <header className="dashboard-hero">
        <div className="hero-text">
          <h1>System <span className="text-gradient">Overview</span></h1>
          <p>Real-time document integrity monitoring and forensics.</p>
        </div>
        <div className="system-badge">
          <Activity size={16} className="pulse-icon" />
          Live Security Feed
        </div>
      </header>

      {/* Top Stats Row */}
      <div className="stats-grid">
        {stats.map((item, index) => (
          <div key={index} className="stat-glass-card">
            <div className="stat-header">
              <div className="stat-icon-box">{item.icon}</div>
              <span className="trend-tag">{item.trend}</span>
            </div>
            <div className="stat-body">
              <h3>{item.value}</h3>
              <p>{item.label}</p>
            </div>
          </div>
        ))}
      </div>

      <div className="dashboard-main-grid">
        {/* Recent Activity Table */}
        <div className="main-card table-card">
          <div className="card-header">
            <h3><Clock size={18} /> Recent Scan Logs</h3>
            <button className="view-all-btn">View Vault</button>
          </div>
          <table className="custom-table">
            <thead>
              <tr>
                <th>Document</th>
                <th>Timestamp</th>
                <th>Status</th>
                <th>Confidence</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>passport_scan_v2.pdf</td>
                <td>2 mins ago</td>
                <td><span className="badge-success">Verified</span></td>
                <td>99.2%</td>
              </tr>
              <tr>
                <td>invoice_0091.png</td>
                <td>1 hour ago</td>
                <td><span className="badge-danger">Tampered</span></td>
                <td>14.5%</td>
              </tr>
              <tr>
                <td>license_final.jpg</td>
                <td>3 hours ago</td>
                <td><span className="badge-success">Verified</span></td>
                <td>97.8%</td>
              </tr>
            </tbody>
          </table>
        </div>

        {/* Security Insight Card */}
        <div className="main-card insight-card">
          <div className="card-header">
            <h3><TrendingUp size={18} /> Threat Analysis</h3>
          </div>
          <div className="chart-placeholder">
            {/* You can later integrate Recharts here */}
            <div className="visual-bars">
              <div className="bar" style={{height: '60%'}}></div>
              <div className="bar" style={{height: '80%'}}></div>
              <div className="bar" style={{height: '40%'}}></div>
              <div className="bar active" style={{height: '90%'}}></div>
              <div className="bar" style={{height: '50%'}}></div>
            </div>
            <p>Scanning activity has increased by 15% in the last 24 hours.</p>
          </div>
        </div>
      </div>
    </div>
  );
}
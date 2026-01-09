import React, { useState } from "react";
import { uploadDocument } from "../../api/documentApi";
import { verifyDocument } from "../../api/verificationApi.js";
import { useAuth } from "../../context/AuthContext";
import "../../styles/verification.css";
import { getByDocument } from "../../api/verificationApi";


const Verification = () => {
  const { user } = useAuth();

  const [isScanning, setIsScanning] = useState(false);
  const [status, setStatus] = useState("PENDING");
  const [hash, setHash] = useState("");

  const handleUpload = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    try {
      setIsScanning(true);
      setStatus("PROCESSING...");

      // 1️⃣ Upload file first
const uploadRes = await uploadDocument(file);
const document = uploadRes.data;

const verifyRes = await verifyDocument({
  documentId: document.id,
  userId: document.userId,
  fileUrl: `http://document-service:8083/api/v1/documents/internal/download/${document.id}`
});

setStatus(verifyRes.data.status);
setHash(verifyRes.data.checksum);

    } catch (err) {
      console.error(err);
      setStatus("FAILED");
    }

    setIsScanning(false);
  };

  return (
    <div className="page-container">
      <h2 className="section-title">Document Integrity Check</h2>

      <div className={`upload-zone ${isScanning ? "scanning" : ""}`}>
        <div className="scan-line"></div>

        <div className="upload-content">
          <i className="upload-icon">📄</i>
          <p>Drag & Drop document or <span>Browse</span></p>
          <small>Supported: PDF, PNG, JPG (Max 10MB)</small>
        </div>

        <input type="file" onChange={handleUpload} className="file-input" />
      </div>

      <div className="results-grid">
        <div className="status-card">
          <h4>Security Status</h4>
          <div className="status-indicator">{status}</div>
        </div>

        <div className="status-card">
          <h4>Metadata Hash</h4>
          <code>{hash || "-----"}</code>
        </div>
      </div>
    </div>
  );
};

export default Verification;

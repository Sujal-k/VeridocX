import React, { useEffect, useState } from "react";
import {
  FileText,
  CheckCircle2,
  XCircle,
  Search,
  ShieldCheck,
  Trash2,
  Share2
} from "lucide-react";

import {
  getUserDocuments,
  deleteDocument,
  shareDocument
} from "../../api/documentApi";

import {
  verifyDocument,
  getByDocument
} from "../../api/verificationApi";

import "../../styles/documents.css";

export default function Documents() {
  const [documents, setDocuments] = useState([]);
  const [loading, setLoading] = useState(false);
  const [query, setQuery] = useState("");

  useEffect(() => {
    loadDocs();
  }, []);

  const loadDocs = async () => {
    const res = await getUserDocuments();
    const docs = res.data;

    const enriched = await Promise.all(
      docs.map(async (d) => {
        try {
          const vr = await getByDocument(d.id);
          const list = vr.data;

          return {
            ...d,
            verificationStatus: list.length
              ? list[list.length - 1].status
              : null
          };
        } catch {
          return { ...d, verificationStatus: null };
        }
      })
    );

    setDocuments(enriched);
  };

  const handleVerify = async (doc) => {
    try {
      setLoading(true);

      const res = await verifyDocument({
        documentId: doc.id,
        userId: doc.userId,
        fileUrl: `http://document-service:8083/api/v1/documents/internal/download/${doc.id}`
      });

      setDocuments((prev) =>
        prev.map((d) =>
          d.id === doc.id
            ? { ...d, verificationStatus: res.data.status }
            : d
        )
      );
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (doc) => {
  if (!window.confirm("Delete this document?")) return;

  try {
    await deleteDocument(doc.id);
    setDocuments(prev => prev.filter(d => d.id !== doc.id));
  } catch (e) {
    alert("Delete failed");
    console.error(e);
  }
};

 const handleShare = async (doc) => {
  const targetUserId = prompt("Enter user ID to share with:");

  if (!targetUserId) return;

  try {
    await shareDocument(doc.id, targetUserId);
    alert("Document shared successfully");
  } catch (e) {
    console.error(e);
    alert("Share failed");
  }
};

  const filteredDocs = documents.filter((d) =>
    d.filename.toLowerCase().includes(query.toLowerCase())
  );

  return (
    <div className="vault-container">
      <header className="vault-header">
        <div>
          <h1>
            My <span className="text-gradient">Vault</span>
          </h1>
          <p>Review and verify uploaded documents</p>
        </div>

        <div className="search-bar">
          <Search size={18} />
          <input
            placeholder="Search documents..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
        </div>
      </header>

      <div className="vault-card">
        <div className="vault-grid header">
          <div>DOCUMENT</div>
          <div>VERSION</div>
          <div>SIZE</div>
          <div>STATUS</div>
          <div>ACTIONS</div>
        </div>

        {filteredDocs.map((doc) => (
          <div className="vault-grid row" key={doc.id}>
            <div className="file-cell">
              <FileText size={18} />
              {doc.filename}
            </div>

            <div>v{doc.version}</div>

            <div>{(doc.fileSize / 1024).toFixed(1)} KB</div>

            <div>
              {doc.verificationStatus === "VALID" && (
                <span className="status-pill verified">
                  <CheckCircle2 size={14} /> Valid
                </span>
              )}

              {doc.verificationStatus === "TAMPERED" && (
                <span className="status-pill tampered">
                  <XCircle size={14} /> Tampered
                </span>
              )}

              {!doc.verificationStatus && (
                <span className="status-pill pending">Pending</span>
              )}
            </div>

            <div className="actions">
  <button className="icon-btn" onClick={() => handleVerify(doc)}>
    <ShieldCheck size={18} />
  </button>

  <button className="icon-btn" onClick={() => handleShare(doc)}>
    <Share2 size={18} />
  </button>

  <button className="icon-btn delete" onClick={() => handleDelete(doc)}>
    <Trash2 size={18} />
  </button>
</div>

          </div>
        ))}
      </div>
    </div>
  );
}

import React, { useEffect, useState } from "react";
import { FileText } from "lucide-react";
import { getSharedDocuments } from "../../api/documentApi";
import "../../styles/shared.css";

export default function SharedWithMe() {
  const [docs, setDocs] = useState([]);

  useEffect(() => {
    loadDocs();
  }, []);

  const loadDocs = async () => {
    try {
      const res = await getSharedDocuments();
      setDocs(res.data);
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <div className="shared-container">
      <div className="shared-header">
        <h1>Shared <span>With Me</span></h1>
        <p>Documents other users have shared with you</p>
      </div>

      <div className="shared-list">
        {docs.map((d) => (
          <div key={d.id} className="shared-card">
            <FileText size={20} />
            <span>{d.filename}</span>
          </div>
        ))}

        {!docs.length && (
          <p className="empty">No documents shared yet.</p>
        )}
      </div>
    </div>
  );
}

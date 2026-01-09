import React, { useState } from "react";
import api from "../api/axiosInstance";

const UploadDocument = () => {
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!file) {
      alert("Please select a file");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
      setLoading(true);

      const token = localStorage.getItem("token");

      const res = await api.post(
        "/api/v1/documents/upload",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: `Bearer ${token}`
          }
        }
      );

      alert("Uploaded successfully!");
      console.log(res.data);

    } catch (err) {
      console.error(err);
      alert("Upload failed");
    }

    setLoading(false);
  };

  return (
    <div className="auth-wrapper">
      <div className="glass-card" style={{ maxWidth: 500 }}>
        <h2>Upload Document</h2>

        <form onSubmit={handleSubmit}>
          <input
            type="file"
            onChange={(e) => setFile(e.target.files[0])}
          />

          <button className="neon-button" disabled={loading}>
            {loading ? "Uploading..." : "Upload"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default UploadDocument;

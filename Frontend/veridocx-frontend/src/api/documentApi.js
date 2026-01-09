import api from "./axiosInstance";

export const getUserDocuments = () => {
  return api.get("/api/v1/documents/my");
};

export const shareDocument = (id, targetUserId) => {
  return api.post(
    `/api/v1/documents/share/${id}?targetUserId=${targetUserId}`
  );
};


export const deleteDocument = (id) => {
  return api.delete(`/api/v1/documents/${id}`);
};

export const uploadDocument = (file) => {
  const formData = new FormData();
  formData.append("file", file);

  return api.post("/api/v1/documents/upload", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
      Authorization: `Bearer ${localStorage.getItem("token")}`
    }
  });
};

export const getSharedDocuments = () => {
  return api.get("/api/v1/documents/shared");
};


import api from "./axiosInstance";

export const verifyDocument = (payload) => {
  return api.post("/api/v1/verify", payload);
};

export const getByDocument = (documentId) => {
  return api.get(`/api/v1/verify/document/${documentId}`);
};

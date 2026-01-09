import axios from "./axiosInstance";

export const loginApi = (data) =>
  axios.post("/api/auth/login", data);

export const registerApi = (data) =>
  axios.post("/api/auth/register", data);

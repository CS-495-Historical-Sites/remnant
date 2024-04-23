import LocationSuggestion from "../models/LocationSuggestion";
import LocationEditSuggestion from "../models/LocationSuggestion";

import { API_BASE_URL } from "../ServerUtil";

export const GetAddLocationSuggestions = async (
  token: string,
  setToken: (token: string) => void,
): Promise<LocationSuggestion[]> => {
  const response = await fetch(
    `${API_BASE_URL}/api/suggestions/locations/add`,
    {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    },
  );
  if (response.status === 401) {
    setToken("");
    throw new Error("Unauthorized access");
  }
  return response.json();
};

export const GetLocationEditSuggestions = async (
  token: string,
  setToken: (token: string) => void,
): Promise<LocationEditSuggestion[]> => {
  const response = await fetch(
    `${API_BASE_URL}/api/suggestions/locations/edit`,
    {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    },
  );
  if (response.status === 401) {
    setToken("");
    throw new Error("Unauthorized access");
  }
  return response.json();
};

export const GetLocationEditSuggestion = async (
  suggestionId: string,
  token: string,
  setToken: (token: string) => void,
): Promise<LocationEditSuggestion> => {
  const response = await fetch(
    `${API_BASE_URL}/api/suggestions/locations/edit/${suggestionId}`,
    {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    },
  );
  if (response.status === 401) {
    setToken("");
    throw new Error("Unauthorized access");
  }
  return response.json();
};

export const GetLocationAddSuggestion = async (
  suggestionId: string,
  token: string,
  setToken: (token: string) => void,
): Promise<LocationSuggestion> => {
  const response = await fetch(
    `${API_BASE_URL}/api/suggestions/locations/add/${suggestionId}`,
    {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    },
  );
  if (response.status === 401) {
    setToken("");
    throw new Error("Unauthorized access");
  }
  return response.json();
};

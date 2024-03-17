import LocationSuggestion from "../models/LocationSuggestion";
import LocationEditSuggestion from "../models/LocationSuggestion";

export const GetAddLocationSuggestions = async (
  token: string,
  setToken: (token: string) => void,
): Promise<LocationSuggestion[]> => {
  const response = await fetch(
    `http://localhost:8080/api/suggestions/location_add_suggestions`,
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
    `http://localhost:8080/api/suggestions/location_edit_suggestions`,
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
    `http://localhost:8080/api/suggestions/location_edit_suggestions/${suggestionId}`,
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

import LocationDetails from "../models/Location";

import { API_BASE_URL } from "../ServerUtil";

export const GetLocationDetails = async (
  locationId: string,
  token: string,
  setToken: (token: string) => void,
): Promise<LocationDetails> => {
  const response = await fetch(`${API_BASE_URL}/api/locations/${locationId}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  if (response.status === 401) {
    setToken("");
    throw new Error("Unauthorized access");
  }
  return response.json();
};

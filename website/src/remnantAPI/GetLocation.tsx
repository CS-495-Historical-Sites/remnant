import LocationDetails from "../models/Location";

export const GetLocationDetails = async (
  locationId: string,
  token: string,
  setToken: (token: string) => void,
): Promise<LocationDetails> => {
  const response = await fetch(
    `http://localhost:8080/api/locations/${locationId}`,
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

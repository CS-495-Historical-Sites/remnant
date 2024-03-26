export const UpdateLocationSuggestion = async (
  suggestionId: string,
  token: string,
  type: string,
  status: string,
): Promise<Response> => {
  return await fetch(
    `http://localhost:8080/api/suggestions/locations/${type}/${suggestionId}/approval`,
    {
      method: "PATCH",

      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },

      body: JSON.stringify({ status: status }),
    },
  );
};

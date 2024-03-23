export const UpdateLocationEditSuggestion = async (
    suggestionId: string,
    token: string,
    status: string
  ): Promise<Response> => {
    return await fetch(
      `http://localhost:8080/api/suggestions/locations/edit/${suggestionId}/approval`,
      {
        method: "PATCH",
  
        headers: {    
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json",
        },

        body: JSON.stringify({ "status": status }),
      },
    );
  };
  

  
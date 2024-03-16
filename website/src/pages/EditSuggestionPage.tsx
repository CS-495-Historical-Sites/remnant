import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  Typography,
  Button,
  Paper,
  CircularProgress,
  Box,
} from "@mui/material";

import LocationEditSuggestions from "../models/LocationSuggestion";

interface UserProps {
  setToken: (token: string) => void;
  token: string;
}

export const EditSuggestionPage: React.FC<UserProps> = ({
  setToken,
  token,
}) => {
  const { suggestionId } = useParams<{ suggestionId: string }>();
  const [suggestion, setSuggestion] = useState<LocationEditSuggestions>();
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchSuggestionDetails = async () => {
      try {
        const getSuggestionDetails = async (suggestionId: string) => {
          // Implement your logic to fetch suggestion details from the server
          // For example:
          const response = await fetch(
            `http://localhost:8080/api/suggestions/location_edit_suggestions/${suggestionId}`,
            {
              method: "GET",
              headers: {
                Authorization: `Bearer ${token}`,
              },
            },
          );
          if (!response.ok) {
            throw new Error("Failed to fetch suggestion details");
          }
          const data = await response.json();
          return data;
        };

        const data = await getSuggestionDetails(suggestionId!);

        setSuggestion(data);
      } catch (error) {
        console.error("Failed to fetch suggestion details", error);
      } finally {
        setIsLoading(false);
      }
    };

    if (suggestionId) {
      fetchSuggestionDetails();
    }
  }, [suggestionId]);

  const handleApprove = async () => {
    // try {
    //   await approveSuggestion(suggestionId!);
    //   navigate('/suggestions'); // Redirect to suggestions list or dashboard after approval
    // } catch (error) {
    //   console.error("Failed to approve suggestion", error);
    // }
  };

  const handleDeny = async () => {
    // try {
    //   await denySuggestion(suggestionId!);
    //   navigate('/suggestions'); // Redirect to suggestions list or dashboard after denial
    // } catch (error) {
    //   console.error("Failed to deny suggestion", error);
    // }
  };

  if (isLoading) {
    return <CircularProgress />;
  }

  return (
    <Paper elevation={3} style={{ padding: "20px", margin: "20px" }}>
      <Typography variant="h4" gutterBottom>
        Edit Suggestion Details
      </Typography>
      {suggestion ? (
        <Box>
          <Typography variant="h5">{suggestion.name}</Typography>
          <Typography variant="subtitle1">
            Suggested by User ID: {suggestion.user} on{" "}
            {new Date(suggestion.suggestion_time).toLocaleDateString()}
          </Typography>
          <Typography variant="body1" paragraph>
            Short Description: {suggestion.short_description}
          </Typography>
          <Typography variant="body2" paragraph>
            Detailed Description: {suggestion.long_description}
          </Typography>
          <div>
            <Button
              variant="contained"
              color="primary"
              onClick={handleApprove}
              style={{ marginRight: "10px" }}
            >
              Approve
            </Button>
            <Button variant="contained" color="secondary" onClick={handleDeny}>
              Deny
            </Button>
          </div>
        </Box>
      ) : (
        <Typography variant="body1">Suggestion not found.</Typography>
      )}
    </Paper>
  );
};

export default EditSuggestionPage;

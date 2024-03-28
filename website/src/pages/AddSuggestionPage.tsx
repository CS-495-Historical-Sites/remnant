import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  Typography,
  Button,
  Paper,
  CircularProgress,
  Box,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
} from "@mui/material";

import LocationDetails from "../models/Location";
import LocationSuggestion from "../models/LocationSuggestion";
import { UpdateLocationSuggestion } from "../remnantAPI/UpdateLocationSuggestions";
import { GetLocationAddSuggestion } from "../remnantAPI/GetLocationSuggestions";

interface UserProps {
  setToken: (token: string) => void;
  token: string;
}

const AddSuggestionPage: React.FC<UserProps> = ({ setToken, token }) => {
  const { suggestionId } = useParams<{ suggestionId: string }>();
  const [suggestion, setSuggestion] = useState<LocationSuggestion>();
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      if (suggestionId) {
        try {
          const suggestionData = await GetLocationAddSuggestion(
            suggestionId,
            token,
            setToken,
          );
          setSuggestion(suggestionData);
        } catch (error) {
          console.error("Failed to fetch data", error);
        } finally {
          setIsLoading(false);
        }
      }
    };
    fetchData();
  }, [suggestionId, token]);

  const handleApprove = async () => {
    try {
      await UpdateLocationSuggestion(suggestionId!, token, "add", "approved");
      navigate("/admin/suggestions");
    } catch (error) {
      console.error("Failed to approve suggestion", error);
    }
  };

  const handleDeny = async () => {
    try {
      await UpdateLocationSuggestion(suggestionId!, token, "add", "denied");
      navigate("/admin/suggestions");
    } catch (error) {
      console.error("Failed to deny suggestion", error);
    }
  };
  if (isLoading) {
    return <CircularProgress />;
  }

  return (
    <Paper elevation={3} style={{ padding: "20px", margin: "20px" }}>
      <Typography variant="h4" gutterBottom>
        Details
      </Typography>
      {suggestion ? (
        <Box>
          <Typography variant="h5" gutterBottom>
            Location Suggestion: {suggestion.name}
          </Typography>

          <Typography variant="body1">
            Suggested by User ID: {suggestion.user} on{" "}
            {new Date(suggestion.suggestion_time).toLocaleDateString()}
          </Typography>

          <Typography variant="body1">
            Description: {suggestion.short_description}
          </Typography>
          <Typography variant="body1">
            {/* show lat and long */}
            Latitude: {suggestion.latitude.toFixed(4)}
            <br />
            Longitude: {suggestion.longitude.toFixed(4)}
          </Typography>

          <img
            src={suggestion.image_url}
            alt={suggestion.wikidata_image_name}
            style={{ width: "200px", height: "200px" }}
          />

          {/* Approval and Denial Buttons */}
          <div style={{ marginTop: "20px" }}>
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

export default AddSuggestionPage;

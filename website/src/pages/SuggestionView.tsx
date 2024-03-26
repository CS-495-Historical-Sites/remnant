import React, { useState, useEffect } from "react";
import {
  Typography,
  List,
  ListItem,
  ListItemText,
  Paper,
  Divider,
} from "@mui/material";
import { useNavigate } from "react-router-dom"; // Import useNavigate

import {
  GetAddLocationSuggestions,
  GetLocationEditSuggestions,
} from "../remnantAPI/GetLocationSuggestions";

import LocationSuggestion from "../models/LocationSuggestion";
import LocationEditSuggestion from "../models/LocationSuggestion";

interface UserProps {
  setToken: (token: string) => void;
  token: string;
}

export const SuggestionsView: React.FC<UserProps> = ({ setToken, token }) => {
  const [addSuggestions, setAddSuggestions] = useState<LocationSuggestion[]>(
    [],
  );
  const [editSuggestions, setEditSuggestions] = useState<
    LocationEditSuggestion[]
  >([]);

  const navigate = useNavigate(); // Initialize useNavigate

  useEffect(() => {
    const fetchLocationAddSuggestions = async () => {
      const data = await GetAddLocationSuggestions(token, setToken);

      setAddSuggestions(data);
    };

    const fetchLocationEditSuggestions = async () => {
      const data = await GetLocationEditSuggestions(token, setToken);

      setEditSuggestions(data);
    };

    fetchLocationAddSuggestions();
    fetchLocationEditSuggestions();
  }, [token, setToken]);


  const handleAddSuggestionClick = (suggestionId: number) => {
    // Navigate to the edit suggestion page with the suggestionId
    navigate(`/admin/add-suggestion/${suggestionId}`);
  };

  const handleEditSuggestionClick = (suggestionId: number) => {
    // Navigate to the edit suggestion page with the suggestionId
    navigate(`/admin/edit-suggestion/${suggestionId}`);
  };

  return (
    <Paper elevation={3} style={{ margin: "20px", padding: "20px" }}>
      <Typography variant="h4" gutterBottom>
        Location Suggestions
      </Typography>

      <Typography variant="h5" gutterBottom>
        Add Suggestions
      </Typography>
      <List>
        {addSuggestions.map((suggestion) => (
          <ListItem
            key={suggestion.id}
            divider
            button
            onClick={() => handleAddSuggestionClick(suggestion.id)}
          >
            <ListItemText
              primary={suggestion.name}
              secondary={`Suggested by User ID: ${suggestion.user} on ${new Date(suggestion.suggestion_time).toLocaleDateString()} - ${suggestion.short_description}`}
            />
          </ListItem>
        ))}
      </List>
      <Divider style={{ margin: "20px 0" }} />
      <Typography variant="h5" gutterBottom>
        Edit Suggestions
      </Typography>
      <List>
        {editSuggestions.map((suggestion) => (
          <ListItem
            key={suggestion.id}
            divider
            button
            onClick={() => handleEditSuggestionClick(suggestion.id)}
          >
            <ListItemText
              primary={suggestion.name}
              secondary={`Suggested by User ID: ${suggestion.user} on ${new Date(suggestion.suggestion_time).toLocaleDateString()} - ${suggestion.short_description}`}
            />
          </ListItem>
        ))}
      </List>
    </Paper>
  );
};

export default SuggestionsView;

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
    const fetchSuggestions = async (
      endpoint: string,
      setSuggestionsFunc: React.Dispatch<
        React.SetStateAction<LocationSuggestion[]>
      >,
    ) => {
      const response = await fetch(
        `http://localhost:8080/api/suggestions/${endpoint}`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      if (response.status === 401) {
        setToken("");
        return;
      }
      const data: LocationSuggestion[] = await response.json();
      setSuggestionsFunc(data);
    };

    fetchSuggestions("location_add_suggestions", setAddSuggestions);
    fetchSuggestions("location_edit_suggestions", setEditSuggestions);
  }, [token, setToken]);

  const handleSuggestionClick = (suggestionId: number) => {
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
            onClick={() => handleSuggestionClick(suggestion.id)}
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
            onClick={() => handleSuggestionClick(suggestion.id)}
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

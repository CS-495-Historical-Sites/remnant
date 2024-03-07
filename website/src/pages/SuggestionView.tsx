import React, { useState, useEffect } from "react";
import {
  Button,
  Typography,
  List,
  ListItem,
  ListItemText,
  Paper,
} from "@mui/material";

import LocationSuggestion from "../models/LocationSuggestion";

interface UserProps {
  setToken: (token: string) => void;
  token: string;
}

export const SuggestionsView: React.ComponentType<UserProps> = ({
  setToken,
  token,
}) => {
  const [suggestions, setSuggestions] = useState<LocationSuggestion[]>([]);

  useEffect(() => {
    const getSuggestions = async () => {
      const response = await fetch(
        `http://localhost:8080/api/location_suggestions`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      if (response.status == 401) {
        setToken("");
        return;
      }

      const suggestions: LocationSuggestion[] = await response.json();
      setSuggestions(suggestions);
    };
    getSuggestions();
  }, [token]);

  return (
    <Paper elevation={3} style={{ margin: "20px", padding: "20px" }}>
      <Typography variant="h4" gutterBottom>
        Location Suggestions
      </Typography>
      <List>
        {suggestions.map((suggestion) => (
          <ListItem key={suggestion.id} divider>
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

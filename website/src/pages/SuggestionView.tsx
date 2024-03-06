import React, { useState, useEffect } from 'react';
import { Typography, List, ListItem, ListItemText, Paper } from '@mui/material';

import LocationSuggestion from "../models/LocationSuggestion";

interface UserProps {
    setToken: (token: string) => void;
    token?: string;
  }

export const SuggestionsView: React.ComponentType<UserProps> = ({ setToken, token }) => {
  const [suggestions, setSuggestions] = useState<LocationSuggestion[]>([]);


    useEffect(() => {
    const getSuggestions = async () => {
      const response = await fetch(`http://localhost:8080/api/location_suggestions`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
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
    <Paper elevation={3} style={{ margin: '20px', padding: '20px' }}>
      <Typography variant="h4" gutterBottom>
        Location Suggestions
      </Typography>
      <List>
        {suggestions.map(s => (
          <ListItem key={s.id} divider>
            <ListItemText
              primary={s.name}
              secondary={`Suggested by User ID: ${s.user} on ${new Date(s.suggestion_time).toLocaleDateString()} - ${s.short_description}`}
            />
          </ListItem>
        ))}
      </List>
    </Paper>
  );
}

export default SuggestionsView;

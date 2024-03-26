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
import LocationEditSuggestions from "../models/LocationSuggestion";
import { UpdateLocationEditSuggestion } from "../remnantAPI/UpdateLocationSuggestions";
import { GetLocationEditSuggestion } from "../remnantAPI/GetLocationSuggestions";
import { GetLocationDetails } from "../remnantAPI/GetLocation";

interface UserProps {
  setToken: (token: string) => void;
  token: string;
}

const EditSuggestionPage: React.FC<UserProps> = ({ setToken, token }) => {
  const { suggestionId } = useParams<{ suggestionId: string }>();
  const [suggestion, setSuggestion] = useState<LocationEditSuggestions>();
  const [currentDetails, setCurrentDetails] = useState<LocationDetails>();
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      if (suggestionId) {
        try {
          const suggestionData = await GetLocationEditSuggestion(
            suggestionId,
            token,
            setToken,
          );
          setSuggestion(suggestionData);
          const currentData = await GetLocationDetails(
            suggestionData.location_id.toString(),
            token,
            setToken,
          );
          setCurrentDetails(currentData);
        } catch (error) {
          console.error("Failed to fetch data", error);
        } finally {
          setIsLoading(false);
        }
      }
    };
    fetchData();
  }, [suggestionId, token]);

  // Implement comparison logic here (as an example, this could be more elaborate)
  const renderDifferences = () => {
    if (!suggestion || !currentDetails) return null;
    // Example comparison, extend according to your data structure
    const diffs = [
      {
        label: "Name",
        current: currentDetails.name,
        suggested: suggestion.name,
      },
      {
        label: "Short Description",
        current: currentDetails.short_description,
        suggested: suggestion.short_description,
      },
    ].filter((diff) => diff.current !== diff.suggested);

    return diffs.length ? (
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Field</TableCell>
            <TableCell>Current Value</TableCell>
            <TableCell>Suggested Value</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {diffs.map((diff, index) => (
            <TableRow key={index}>
              <TableCell>{diff.label}</TableCell>
              <TableCell>{diff.current}</TableCell>
              <TableCell>{diff.suggested}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    ) : (
      <Typography>No differences found.</Typography>
    );
  };
  const handleApprove = async () => {
    try {
      await UpdateLocationEditSuggestion(suggestionId!, token, "approved");
      navigate("/admin/suggestions");
    } catch (error) {
      console.error("Failed to approve suggestion", error);
    }
  };

  const handleDeny = async () => {
    try {
      await UpdateLocationEditSuggestion(suggestionId!, token, "denied");
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
        Edit Suggestion Details
      </Typography>
      {suggestion && currentDetails ? (
        <Box>
          <Typography variant="h5" gutterBottom>
            Edit Suggestion for {currentDetails.name}
          </Typography>

          <Typography variant="body1">
            Suggested by User ID: {suggestion.user} on{" "}
            {new Date(suggestion.suggestion_time).toLocaleDateString()}
          </Typography>

          {renderDifferences()}

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

export default EditSuggestionPage;

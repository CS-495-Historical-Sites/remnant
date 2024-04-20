// third party
import React, { useEffect } from "react";
// materialui
import { Typography } from "@mui/material";

import { API_BASE_URL } from "../ServerUtil";

export const ConfirmationEmailLandingPage: React.ComponentType = () => {
  const [isLoading, setIsLoading] = React.useState(true);
  const [confirmedSuccessful, setConfirmedSuccessful] = React.useState(false);
  const [error, setError] = React.useState<string | null>(null);

  let urlElements = window.location.href.split("/");
  let confirmation_token = urlElements[urlElements.length - 1];

  useEffect(() => {
    confirmEmail();
  }, []);

  async function confirmEmail() {
    try {
      const response = await fetch(
        `${API_BASE_URL}/api/confirmation/${confirmation_token}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
        },
      );

      if (!response.ok) {
        setIsLoading(false);
        setError("Invalid confirmation token or server error");
        return;
      }
      // @ts-ignore
      const result = await response.json();
      setConfirmedSuccessful(true);
    } catch (error) {
      setIsLoading(false);
      setError("Failed to connect to the server");
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <div className="wrap">
      {isLoading ? (
        <Typography variant="h4">Confirming email...</Typography>
      ) : (
        <Typography variant="h4" align="center">
          {confirmedSuccessful ? "Email confirmed" : "Email not confirmed"}
        </Typography>
      )}
      {error && (
        <Typography variant="h4" align="center">
          {error}
        </Typography>
      )}
    </div>
  );
};

export default ConfirmationEmailLandingPage;

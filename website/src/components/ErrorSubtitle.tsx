import React from "react";

import { Typography } from "@mui/material";

interface ErrorSubtitleProps {
  error: string;
}

class ErrorSubtitle extends React.Component<ErrorSubtitleProps, {}> {
  render() {
    return (
      <Typography variant="subtitle1" className="error-subtitle" color="error">
        {this.props.error}
      </Typography>
    );
  }
}

export default ErrorSubtitle;

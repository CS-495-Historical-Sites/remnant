// Import necessary components from MaterialUI
import { Typography, Button, Box } from "@mui/material";
import { Link } from "react-router-dom"; // Assuming you're using react-router for navigation

function Home() {
  return (
    <div>
      <Box display="flex" flexDirection="column" alignItems="center" justifyContent="center" height="100%">
        <Typography variant="h1" className="title" gutterBottom>
          Remnant
        </Typography>
        <Typography variant="h6" style={{ textAlign: 'center', margin: '20px' }}>
          Explore historical sites around you. Track your "steps" through history with proximity notifications, view landmarks' details, and contribute to the exploration community.
        </Typography>
        <Button component={Link} to="/register" variant="contained" color="primary" style={{ margin: '10px' }}>
          Sign Up
        </Button>
        <Button component={Link} to="/login" variant="outlined" color="primary">
          Login
        </Button>
      </Box>
    </div>
  );
}

export default Home;

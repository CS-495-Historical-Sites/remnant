import { Typography, Button, Box, Paper, Grid } from '@mui/material';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function AdminView() {
  const navigate = useNavigate();

  // Functions to navigate to different admin pages
  const goToUserManagement = () => navigate('/admin/users');
  const goToSiteManagement = () => navigate('/admin/sites');
  const goToSuggestionsManagement = () => navigate('/admin/suggestions');



  // Redirect to the /admin page if not already there
  useEffect(() => {
    if (window.location.pathname !== '/admin') {
      navigate('/admin');
    }
  }, [navigate]);

  return (
    <Box sx={{ flexGrow: 1, p: 2 }}>
      <Typography variant="h3" className="title" gutterBottom>
        Admin Dashboard
      </Typography>
      <Grid container spacing={3}>
        <Grid item xs={12} sm={4}>
          <Paper elevation={3} sx={{ p: 2 }}>
            <Typography variant="h5" gutterBottom>User Management</Typography>
            <Typography variant="body1" gutterBottom>Manage user accounts, roles, and permissions.</Typography>
            <Button variant="contained" color="primary" onClick={goToUserManagement}>Manage Users</Button>
          </Paper>
        </Grid>
        <Grid item xs={12} sm={4}>
          <Paper elevation={3} sx={{ p: 2 }}>
            <Typography variant="h5" gutterBottom>Site Management</Typography>
            <Typography variant="body1" gutterBottom>Review and edit historical site listings.</Typography>
            <Button variant="contained" color="primary" onClick={goToSiteManagement}>Manage Sites</Button>
          </Paper>
        </Grid>
        <Grid item xs={12} sm={4}>
          <Paper elevation={3} sx={{ p: 2 }}>
            <Typography variant="h5" gutterBottom>Suggestions Management</Typography>
            <Typography variant="body1" gutterBottom>Approve or reject new site suggestions from users.</Typography>
            <Button variant="contained" color="primary" onClick={goToSuggestionsManagement}>Manage Suggestions</Button>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
}

export default AdminView;

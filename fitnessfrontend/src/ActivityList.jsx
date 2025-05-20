import React, { useEffect, useState, useContext } from 'react';
import axios from 'axios';
import { AuthContext } from 'react-oauth2-code-pkce';
import { useNavigate } from 'react-router-dom';
import {
  Button,
  Card,
  CardContent,
  Typography,
  Grid,
  CircularProgress,
  Box,
} from '@mui/material';

const ActivityList = () => {
  const { token } = useContext(AuthContext);
  const [activities, setActivities] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchActivities = async () => {
      try {
        const res = await axios.get('http://localhost:9090/api/activity', {
          headers: { Authorization: `Bearer ${token}` },
        });
        setActivities(res.data);
      } catch (err) {
        console.error('Error fetching activities:', err);
      } finally {
        setLoading(false);
      }
    };

    if (token) fetchActivities();
  }, [token]);

  if (loading) return <CircularProgress />;

  return (
    <Box sx={{ p: 2 }}>
      <Box display="flex" justifyContent="flex-end" mb={2}>
        <Button
          variant="contained"
          color="primary"
          onClick={() => navigate('/activity/add')}
        >
          Add New Activity
        </Button>
      </Box>

      <Grid container spacing={2}>
        {activities.map((activity) => (
          <Grid item xs={12} sm={6} md={4} key={activity.id}>
            <Card
              onClick={() => navigate(`/activity/${activity.id}`)}
              sx={{ borderRadius: 3, backgroundColor: '#f3f4f6', cursor: 'pointer' }}
            >
              <CardContent>
                <Typography variant="h6">{activity.activityType}</Typography>
                <Typography variant="body2">Duration: {activity.duration} min</Typography>
                <Typography variant="body2">Calories: {activity.caloriesBurned}</Typography>
                <Typography variant="body2">
                  Start: {new Date(activity.startTime).toLocaleString()}
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default ActivityList;

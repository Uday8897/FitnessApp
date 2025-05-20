import React, { useEffect, useState, useContext } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { AuthContext } from 'react-oauth2-code-pkce';
import {
  Typography,
  CircularProgress,
  Box,
  List,
  ListItem,
  ListItemText,
  Paper,
} from '@mui/material';

const ActivityRecommendation = () => {
  const { token } = useContext(AuthContext);
  const { id } = useParams();
  const [activity, setActivity] = useState(null);
  const [recommendation, setRecommendation] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDetails = async () => {
      try {
        const activityRes = await axios.get(`http://localhost:9090/api/activity/${id}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        const recRes = await axios.get(`http://localhost:9090/api/recommendations/activity/${id}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setActivity(activityRes.data);
        setRecommendation(recRes.data);
      } catch (err) {
        console.error('Error fetching data:', err);
      } finally {
        setLoading(false);
      }
    };

    if (token && id) fetchDetails();
  }, [token, id]);

  if (loading) return <CircularProgress />;

  return (
    <Box sx={{ p: 3 }}>
      {activity && (
        <Paper elevation={3} sx={{ p: 2, mb: 3 }}>
          <Typography variant="h6">{activity.activityType}</Typography>
          <Typography>Duration: {activity.duration} minutes</Typography>
          <Typography>Calories: {activity.caloriesBurned}</Typography>
          <Typography>Start: {new Date(activity.startTime).toLocaleString()}</Typography>
        </Paper>
      )}

      {recommendation && (
        <Paper elevation={3} sx={{ p: 2 }}>
          <Typography variant="h6">AI Recommendations</Typography>
          <Typography variant="body1" sx={{ mb: 2 }}>{recommendation.recommendations}</Typography>
          <RenderList title="Improvements" items={recommendation.improvements} />
          <RenderList title="Suggestions" items={recommendation.suggestions} />
          <RenderList title="Safety Tips" items={recommendation.safety} />
        </Paper>
      )}
    </Box>
  );
};

const RenderList = ({ title, items }) => {
  if (!items || items.length === 0) return null;

  return (
    <>
      <Typography variant="subtitle1" sx={{ mt: 2 }}>{title}</Typography>
      <List dense>
        {items.map((item, idx) => (
          <ListItem key={idx}>
            <ListItemText primary={`• ${item}`} />
          </ListItem>
        ))}
      </List>
    </>
  );
};

export default ActivityRecommendation;

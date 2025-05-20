import React, { useState, useContext } from 'react';
import axios from 'axios';
import {
  TextField,
  Button,
  Box,
  Typography,
  MenuItem,
  Select,
  InputLabel,
  FormControl,
  Snackbar,
  Alert
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from 'react-oauth2-code-pkce';

const ACTIVITY_TYPES = [
  'RUNNING', 'WALKING', 'CYCLING', 'SWIMMING',
  'WEIGHT_TRAINING', 'YOGA', 'HIIT', 'CARDIO', 'OTHER'
];

const ActivityForm = () => {
  const navigate = useNavigate();
  const { token } = useContext(AuthContext);
  const [form, setForm] = useState({
    activityType: '',
    duration: '',
    caloriesBurned: '',
    startTime: '',
    distance: '',
    heartBeat: ''
  });

  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const payload = {
      userId: localStorage.getItem('userId'),
      activityType: form.activityType,
      duration: parseInt(form.duration),
      caloriesBurned: parseInt(form.caloriesBurned),
      startTime: form.startTime,
      additionalMetrics: {
        distance: parseFloat(form.distance),
        heartBeat: parseInt(form.heartBeat)
      }
    };

    try {
      await axios.post('http://localhost:9090/api/activity', payload, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setSnackbar({ open: true, message: 'Activity added successfully!', severity: 'success' });

      // Redirect after short delay
      setTimeout(() => navigate('/activity'), 1500);
    } catch (err) {
      console.error('Error adding activity:', err);
      setSnackbar({ open: true, message: 'Failed to add activity.', severity: 'error' });
    }
  };

  const handleSnackbarClose = () => {
    setSnackbar({ ...snackbar, open: false });
  };

  return (
    <Box sx={{ maxWidth: 500, mx: 'auto', mt: 4 }}>
      <Typography variant="h5" gutterBottom>Add New Activity</Typography>
      <form onSubmit={handleSubmit}>
        <FormControl fullWidth margin="normal">
          <InputLabel id="activityType-label">Activity Type</InputLabel>
          <Select
            labelId="activityType-label"
            name="activityType"
            value={form.activityType}
            onChange={handleChange}
            label="Activity Type"
          >
            {ACTIVITY_TYPES.map((type) => (
              <MenuItem key={type} value={type}>
                {type.replace('_', ' ')}
              </MenuItem>
            ))}
          </Select>
        </FormControl>

        <TextField
          fullWidth
          label="Duration (minutes)"
          name="duration"
          type="number"
          value={form.duration}
          onChange={handleChange}
          margin="normal"
        />
        <TextField
          fullWidth
          label="Calories Burned"
          name="caloriesBurned"
          type="number"
          value={form.caloriesBurned}
          onChange={handleChange}
          margin="normal"
        />
        <TextField
          fullWidth
          label="Start Time"
          name="startTime"
          type="datetime-local"
          value={form.startTime}
          onChange={handleChange}
          margin="normal"
          InputLabelProps={{ shrink: true }}
        />
        <TextField
          fullWidth
          label="Distance (km)"
          name="distance"
          type="number"
          value={form.distance}
          onChange={handleChange}
          margin="normal"
        />
        <TextField
          fullWidth
          label="Heart Beat (bpm)"
          name="heartBeat"
          type="number"
          value={form.heartBeat}
          onChange={handleChange}
          margin="normal"
        />
        <Button type="submit" variant="contained" color="primary" sx={{ mt: 2 }}>
          Submit
        </Button>
      </form>

      <Snackbar
        open={snackbar.open}
        autoHideDuration={3000}
        onClose={handleSnackbarClose}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      >
        <Alert onClose={handleSnackbarClose} severity={snackbar.severity} sx={{ width: '100%' }}>
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default ActivityForm;

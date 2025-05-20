import React from "react";
import { Container, Typography, Box, Grid, Paper } from "@mui/material";
import { motion } from "framer-motion";

import fitnessimg1 from "./assets/fitnessimg1.jpg";
import fitnessimg2 from "./assets/fitnessimg2.jpg";
import fitnessimg3 from "./assets/fitnessimg3.jpg";

const fitnessImages = [fitnessimg1, fitnessimg2, fitnessimg3];

const quotes = [
  "Push yourself, because no one else is going to do it for you.",
  "The body achieves what the mind believes.",
  "Sweat is just fat crying.",
  "Don't limit your challenges. Challenge your limits.",
];

const Dashboard = () => {
  const randomQuote = quotes[Math.floor(Math.random() * quotes.length)];

  return (
    <Container sx={{ mt: 6 }}>
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 1 }}
      >
        <Typography variant="h3" align="center" gutterBottom>
          Fitness Tracker
        </Typography>
      </motion.div>

      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 1, duration: 1.5 }}
      >
        <Typography
          variant="h5"
          align="center"
          gutterBottom
          sx={{ fontStyle: "italic", mb: 5 }}
        >
          {randomQuote}
        </Typography>
      </motion.div>

      <Grid container spacing={4}>
        {fitnessImages.map((img, index) => (
          <Grid item xs={12} sm={6} md={4} key={index}>
            <motion.div
              whileHover={{ scale: 1.05 }}
              transition={{ type: "spring", stiffness: 300 }}
            >
              <Paper
                elevation={6}
                sx={{
                  height: 250,
                  backgroundImage: `url(${img})`,
                  backgroundSize: "cover",
                  backgroundPosition: "center",
                  borderRadius: 3,
                }}
              />
            </motion.div>
          </Grid>
        ))}
      </Grid>

      <Box sx={{ mt: 6, textAlign: "center" }}>
        <motion.div
          initial={{ scale: 0.8, opacity: 0 }}
          animate={{ scale: 1, opacity: 1 }}
          transition={{ delay: 2, duration: 1 }}
        >
          <Typography variant="body1" color="text.secondary">
            Track your gym, running, and walking activities with ease. Stay
            motivated and keep moving forward!
          </Typography>
        </motion.div>
      </Box>
    </Container>
  );
};

export default Dashboard;

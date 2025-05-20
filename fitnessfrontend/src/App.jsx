import React, { useEffect, useState, useContext } from "react";
import userIcon from "./assets/images.png";

import {
  BrowserRouter as Router,
  Route,
  Routes,
  Link,
} from "react-router-dom";
import { useDispatch } from "react-redux";
import { AuthContext } from "react-oauth2-code-pkce";
import { setCredentials } from "./store/authSlice";
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  Container,
  Tooltip,
  Avatar,
} from "@mui/material";
import ActivityForm from "./ActivityForm";
import ActivityList from "./ActivityList";
import ActivityRecommendation from "./ActivityRecommendation";
import Dashboard from "./Dashboard";

// ✅ Navbar Component with User Info
const Navbar = ({ logOut, user }) => {
  const userName = user?.preferred_username || "User";
  const email = user?.email || "Not available";

  return (
    <AppBar position="static" sx={{ mb: 4 }}>
      <Toolbar>
        <Typography variant="h6" sx={{ flexGrow: 1 }}>
          Fitness Tracker
        </Typography>
        <Button color="inherit" component={Link} to="/">
          Dashboard
        </Button>
        <Button color="inherit" component={Link} to="/activity">
          Activities
        </Button>

        <Tooltip
          title={
            <Box color="blue" sx={{ p: 1 }}>
              <Typography color="initial">UserName:{userName}</Typography>
              <br />
             
          <Typography color="initial">Email:{email}</Typography>

            </Box>
          }
          arrow
        >
          <Box sx={{ display: "flex", alignItems: "center", mx: 2, cursor: "pointer" }}>
        <Avatar sx={{ width: 32, height: 32 }}>
  <img src={userIcon} alt="User" style={{ width: "100%", height: "100%" }} />
</Avatar>

          </Box>
        </Tooltip>

        <Button color="inherit" onClick={logOut}>
          Logout
        </Button>
      </Toolbar>
    </AppBar>
  );
};

// ✅ Main App Component
const App = () => {
  const { token, tokenData, logIn, logOut } = useContext(AuthContext);
  const dispatch = useDispatch();
  const [authReady, setAuthReady] = useState(false);

  useEffect(() => {
    if (token) {
      dispatch(setCredentials({ token, user: tokenData }));
      setAuthReady(true);
    } else {
      console.log("No token found");
    }
  }, [token, tokenData, dispatch]);

  // ✅ Keycloak Sign Up
  const signUp = () => {
    const clientId = "oauth2-pkce-client"; // Replace with your actual client ID
    const realm = "fitnessApp"; // Replace with your Keycloak realm
    const keycloakUrl = "https://localhost:8181"; // Replace with your Keycloak domain
    const redirectUri = "http://localhost:5173"; // Replace with your redirect URI

    const signUpUrl = `${keycloakUrl}/realms/${realm}/protocol/openid-connect/auth?` +
      `client_id=${clientId}&response_type=code&scope=openid&redirect_uri=${redirectUri}&kc_action=register`;

    window.location.href = signUpUrl;
  };

  return (
    <Router>
      {token ? (
        <>
          <Navbar logOut={logOut} user={tokenData} />
          <Container>
            <Routes>
              <Route path="/" element={<Dashboard />} />
              <Route path="/activity" element={<ActivityList />} />
              <Route path="/activity/:id" element={<ActivityRecommendation />} />
              <Route path="/activity/add" element={<ActivityForm />} />
            </Routes>
          </Container>
        </>
      ) : (
        <Container sx={{ mt: 5, textAlign: "center" }}>
          <Dashboard />
          <Box sx={{ mt: 3 }}>
            <Button
              variant="contained"
              size="large"
              sx={{ mr: 2 }}
              onClick={logIn}
            >
              Sign In
            </Button>
            <Button
              variant="outlined"
              size="large"
              onClick={signUp}
            >
              Sign Up
            </Button>
          </Box>
        </Container>
      )}
    </Router>
  );
};

export default App;

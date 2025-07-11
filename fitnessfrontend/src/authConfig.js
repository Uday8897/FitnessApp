const authConfig = {
  clientId: "oauth2-pkce-client",
  authorizationEndpoint: "http://localhost:8181/realms/fitnessApp/protocol/openid-connect/auth",
  tokenEndpoint: "http://localhost:8181/realms/fitnessApp/protocol/openid-connect/token",
  redirectUri: "http://localhost:5173",
  scope: "openid email profile offline_access",
  onRefreshTokenExpire: (event) =>
    event.logIn(),
};
export default authConfig;
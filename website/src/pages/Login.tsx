// third party
import React from "react";
import { useNavigate } from "react-router-dom";

// material-ui
import { Typography, TextField, Link, Box, Button } from "@mui/material";

// local
import UserLoginInformation from "../models/UserLoginInformation.tsx";
import ErrorSubtitle from "../components/ErrorSubtitle.tsx";



interface LoginProps {
  setToken: (token: string) => void;
}

interface LoginResponse {
  message: string;
  access_token: string;
}

export const Login: React.FC<LoginProps> = ({ setToken }) => {
  const [userInformation, setUserInformation] =
    React.useState<UserLoginInformation>({
      email: "",
      password: "",
    });

  const [err, setErr] = React.useState(false);
  const [errMessage, setErrMessage] = React.useState("");

  const navigate = useNavigate();

  async function handleFormSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const response = await fetch(`${process.env.REACT_APP_API_URL}/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Methods": "GET, POST",
        "Access-Control-Allow-Credentials": "true",
      },
      body: JSON.stringify(userInformation),
    });

    const result: LoginResponse = await response.json();

    if (result.message === "login successful") {
      setToken(result.access_token);
      navigate("/profile");
      return;
    }

    // handle error
    setErr(true);
    setErrMessage(result.message);
  }

  return (
    <div className="wrap">
      <div className="signin-container" style={{ marginTop: "150px" }}>
        <Typography
          variant="h2"
          style={{ fontWeight: "600", fontSize: "2.5rem" }}
        >
          Sign in
        </Typography>
        <Box
          component="form"
          onSubmit={handleFormSubmit}
          style={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "center",
            padding: "20px",
          }}
          id="signin-form"
        >
          <TextField
            sx={{ m: 1 }}
            label="Email"
            onChange={(e) =>
              setUserInformation({ ...userInformation, email: e.target.value })
            }
            required
            key="email"
          />
          <TextField
            sx={{ m: 1 }}
            id="outlined-password-input"
            label="Password"
            type="password"
            autoComplete="current-password"
            onChange={(e) =>
              setUserInformation({
                ...userInformation,
                password: e.target.value,
              })
            }
            required
            key="password"
          />
          <div className="forgot-password-container">
            <Typography
              variant="subtitle1"
              align="right"
              style={{ float: "right" }}
            >
              <Link href="/forgot-password">Forgot password?</Link>
            </Typography>
          </div>
          <Button
            type="submit"
            variant="contained"
            color="primary"
            sx={{ m: 0.5 }}
          >
            Login
          </Button>
          <Typography id="subtitle-signin-text" variant="subtitle1">
            Don't have an account?{" "}
            <Link id="subtitle-signin-text-signup" href="/signup">
              Register
            </Link>
          </Typography>
        </Box>
        {err ? <ErrorSubtitle error={errMessage} /> : null}
      </div>
    </div>
  );
};

export default Login;
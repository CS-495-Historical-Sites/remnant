// third party
import React from "react";
import { useNavigate } from "react-router-dom";

// materialui
import TextField from "@mui/material/TextField";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Link from "@mui/material/Link";

// local
import ErrorSubtitle from "../components/ErrorSubtitle";
import UserRegistrationInformation from "../models/UserRegistrationInformation";

import { passwordValidationMessage } from "../validators/passwordValidationMessage";

function allFieldsValid(
  userRegistrationInformation: UserRegistrationInformation,
): boolean {
  return (
    passwordValidationMessage(userRegistrationInformation.password) ===
    "Password is valid"
  );
}

export const Register: React.FC = () => {
  const [userRegistrationInformation, setUserRegistrationInformation] =
    React.useState<UserRegistrationInformation>({
      username: "",
      email: "",
      password: "",
    });

  const [err, setErr] = React.useState(false);
  const [errMessage, setErrMessage] = React.useState("");

  const navigate = useNavigate();

  async function handleFormSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!allFieldsValid(userRegistrationInformation)) {
      return;
    }

    const response = await fetch(`http://localhost:8080/api/user/register`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Methods": "GET, POST",
        "Access-Control-Allow-Credentials": "true",
      },
      body: JSON.stringify(userRegistrationInformation),
    });

    const result = await response.json();

    console.log(result);

    if (response.status === 422) {
      setErr(true);
      setErrMessage(result.message);
    } else if (response.status === 200) {
      navigate("/login");
    }
  }

  return (
    <div>
      <div className="signup-container" style={{ marginTop: "150px" }}>
        <Typography
          variant="h2"
          style={{ fontWeight: "600", fontSize: "2.5rem" }}
        >
          Sign Up
        </Typography>
        <Typography variant="subtitle1" style={{ marginTop: "10px" }}>
          Your account is free
        </Typography>

        <div>
          <div className="signup-container">
            <Box
              component="form"
              onSubmit={handleFormSubmit}
              // add space between the form fields
              style={{
                display: "flex",
                flexDirection: "column",
                justifyContent: "center",
                alignItems: "center",
                padding: "20px",
              }}
            >
              <TextField
                sx={{ m: 1 }}
                label="Username"
                onChange={(e) =>
                  setUserRegistrationInformation({
                    ...userRegistrationInformation,
                    username: e.target.value,
                  })
                }
                required
                key="username"
              />
              <TextField
                sx={{ m: 1 }}
                label="Email"
                onChange={(e) =>
                  setUserRegistrationInformation({
                    ...userRegistrationInformation,
                    email: e.target.value,
                  })
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
                  setUserRegistrationInformation({
                    ...userRegistrationInformation,
                    password: e.target.value,
                  })
                }
                error={
                  userRegistrationInformation.password !== "" &&
                  passwordValidationMessage(
                    userRegistrationInformation.password,
                  ) !== "Password is valid"
                }
                helperText={passwordValidationMessage(
                  userRegistrationInformation.password,
                )}
                required
                key="password"
              />
              <Button type="submit" variant="contained" color="primary">
                Register
              </Button>
              {err ? <ErrorSubtitle error={errMessage} /> : null}
            </Box>
            <Typography id="subtitle-signin-text" variant="subtitle1">
              Already have an account?{" "}
              <Link id="subtitle-signin-text-signup" href="/login">
                Login
              </Link>
            </Typography>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Register;

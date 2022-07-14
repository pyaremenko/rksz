import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useForm, Controller } from "react-hook-form";
import { TextField, Button, Typography, Paper, Grid } from "@mui/material";

import { AuthContext, Credentials } from "./AuthProvider";

function LoginForm() {
  interface LocationState {
    from: {
      pathname: string;
    };
  }

  const navigate = useNavigate();
  const state = useLocation().state as LocationState;
  const pathname = state?.from?.pathname ?? "/goods";

  const { signin, serverError } = React.useContext(AuthContext);

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<Credentials>();

  const onSubmit = ({ login, password }: Credentials) => {
    signin({ login, password }, () => {
      navigate(pathname, { replace: true });
    });
  };

  return (
    <Grid container item lg={3} md={4} sm={6} xs={10}>
      <Paper
        elevation={10}
        sx={{
          width: "100%",
          padding: 1,
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-around",
        }}
      >
        <form onSubmit={handleSubmit(onSubmit)}>
          <Controller
            render={({ field }) => {
              return (
                <TextField
                  {...field}
                  label="Login"
                  variant="filled"
                  error={!(errors.login == null)}
                  helperText={errors.login?.message}
                  fullWidth
                  sx={{ margin: "3px auto" }}
                />
              );
            }}
            control={control}
            rules={{ required: "Login is required" }}
            defaultValue=""
            name="login"
          />
          <Controller
            render={({ field }) => {
              return (
                <TextField
                  {...field}
                  label="Password"
                  type="password"
                  variant="filled"
                  error={!(errors.password == null)}
                  helperText={errors.password?.message}
                  fullWidth
                  sx={{ margin: "3px auto" }}
                />
              );
            }}
            control={control}
            rules={{ required: "Password is required" }}
            name="password"
            defaultValue=""
          />
          <Typography color={"#ff0000"} align="center">
            {serverError}
          </Typography>
          <Button
            type="submit"
            variant="contained"
            fullWidth
            sx={{ margin: "3px auto" }}
          >
            Log in
          </Button>
        </form>
      </Paper>
    </Grid>
  );
}

export default LoginForm;

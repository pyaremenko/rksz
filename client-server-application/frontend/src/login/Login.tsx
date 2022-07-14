import React from "react";
import { Navigate } from "react-router-dom";

import LoginForm from "./components/LoginForm";
import { AuthContext } from "./components/AuthProvider";

import "./Login.css";

function Login() {
  const { user } = React.useContext(AuthContext);
  if (user) {
    return <Navigate to={"/goods"} replace />;
  }

  return (
    <div className="login-page-container">
      <LoginForm />
    </div>
  );
}

export default Login;

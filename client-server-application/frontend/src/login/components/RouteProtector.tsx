import React from "react";
import { useLocation, Navigate } from "react-router-dom";

import { AuthContext } from "./AuthProvider";

function RouteProtector({ children }: { children: JSX.Element }) {
  const { user } = React.useContext(AuthContext);
  const location = useLocation();

  if (!user) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return children;
}

export default RouteProtector;

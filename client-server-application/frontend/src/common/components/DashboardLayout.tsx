import React from "react";
import { Outlet, useNavigate } from "react-router-dom";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import CssBaseline from "@mui/material/CssBaseline";
import Divider from "@mui/material/Divider";
import Drawer from "@mui/material/Drawer";
import IconButton from "@mui/material/IconButton";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import MenuIcon from "@mui/icons-material/Menu";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import CategoryIcon from "@mui/icons-material/Category";
import InventoryIcon from "@mui/icons-material/Inventory";
import LogoutIcon from "@mui/icons-material/Logout";

import { AuthContext } from "../../login/components/AuthProvider";

function DashboardLayout() {
  const drawerWidth = 240;

  const [mobileOpen, setMobileOpen] = React.useState(false);

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen);
  };

  let navigate = useNavigate();
  const { logout } = React.useContext(AuthContext);

  interface SideBarItem {
    name: string;
    path: string;
    icon: JSX.Element;
  }

  let itemList: SideBarItem[] = [
    { name: "Groups", path: "/groups", icon: <CategoryIcon /> },
    { name: "Goods", path: "/goods", icon: <InventoryIcon /> },
  ];

  let getNameByPath = (): string => {
    let item = itemList.filter(item => item.path === window.location.pathname);
    return item.length === 0 ? "My warehouse" : item[0].name;
  }

  const content = (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "space-between",
        height: "100%",
      }}
    >
      <div>
        <Toolbar />
        <Divider />
        <List>
          {itemList.map((item, index) => (
            <ListItem key={item.name} disablePadding>
              <ListItemButton
                onClick={(event) => {
                  handleDrawerToggle();
                  navigate(item.path);
                }}
              >
                <ListItemIcon>{item.icon}</ListItemIcon>
                <ListItemText primary={item.name} />
              </ListItemButton>
            </ListItem>
          ))}
        </List>
      </div>
      <div>
        <ListItem key="Log out" disablePadding>
          <ListItemButton
            onClick={(event) => {
              handleDrawerToggle();
              logout(() => {
                navigate("/login");
              });
            }}
          >
            <ListItemIcon>
              <LogoutIcon />
            </ListItemIcon>
            <ListItemText primary="Log out" />
          </ListItemButton>
        </ListItem>
      </div>
    </div>
  );

  return (
    <>
      <Box sx={{ display: "flex"}}>
        <CssBaseline />
        <AppBar
          position="fixed"
          sx={{
            width: { sm: `calc(100% - ${drawerWidth}px)` },
            ml: { sm: `${drawerWidth}px` },
          }}
        >
          <Toolbar>
            <IconButton
              color="inherit"
              aria-label="open drawer"
              edge="start"
              onClick={handleDrawerToggle}
              sx={{ mr: 2, display: { sm: "none" } }}
            >
              <MenuIcon />
            </IconButton>
            <Typography variant="h6" noWrap component="div">
              { getNameByPath() }
            </Typography>
          </Toolbar>
        </AppBar>
        <Box
          component="nav"
          sx={{ width: { sm: drawerWidth }, flexShrink: { sm: 0 } }}
          aria-label="mailbox folders"
        >
          <Drawer
            variant="temporary"
            open={mobileOpen}
            onClose={handleDrawerToggle}
            ModalProps={{
              keepMounted: true, // Better open performance on mobile.
            }}
            sx={{
              display: { xs: "block", sm: "none" },
              "& .MuiDrawer-paper": {
                boxSizing: "border-box",
                width: drawerWidth,
              },
            }}
          >
            {content}
          </Drawer>
          <Drawer
            variant="permanent"
            sx={{
              display: { xs: "none", sm: "block" },
              "& .MuiDrawer-paper": {
                boxSizing: "border-box",
                width: drawerWidth,
              },
            }}
            open
          >
            {content}
          </Drawer>
        </Box>
        <Box style={{width: "100%"}}>
          <Toolbar />
          <Outlet />
        </Box>
      </Box>
    </>
  );
}

export default DashboardLayout;
